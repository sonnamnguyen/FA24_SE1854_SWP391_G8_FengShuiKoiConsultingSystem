import React, { useState } from "react";
import { Form, Input, Button, Checkbox, notification } from "antd";
import { LockOutlined, UserOutlined, GoogleOutlined } from "@ant-design/icons";
import { useNavigate } from "react-router-dom";
import "../css/LoginForm.css"; // Link to the updated CSS file
import { OAuthConfig } from "../configuration/OAuthConfigType";
import { setToken } from "../service/localStorageService";
import imgLogin from "../img/Login.webp"; // Path to the background image
import { jwtDecode } from "jwt-decode";

// FCM token
import { getToken } from 'firebase/messaging';
import { messaging } from "../firebase/firebase";

interface JwtPayload {
  iss: string;
  sub: string;
  exp: number;
  iat: number;
  jti: string;
  scope: string;
}

const Login = () => {
  const navigate = useNavigate();
  const [apii, contextHolder] = notification.useNotification();

  const handleContinueWithGoogle = () => {
    const { redirectUri, authUri, clientId } = OAuthConfig;
    const targetUrl = `${authUri}?redirect_uri=${encodeURIComponent(
      redirectUri
    )}&response_type=code&client_id=${clientId}&scope=openid%20email%20profile`;

    window.location.href = targetUrl;
  };

  const handleLogin = async (values: { email: string; password: string }) => {
    const { email, password } = values;

    // Tạo FCM token trước khi đăng nhập
    let fcmToken;
    try {
      fcmToken = await getToken(messaging, {
        vapidKey: "BEWVqlpy-txsAzvUdwjM9CAo26aoa08pcLh7GNAxP4x2LAh9SVTJd8IP5na9Biup_b46livPPyT-U5gKZWLou-Q",
      });
      console.log("FCM token:", fcmToken);
    } catch (error) {
      console.error("FCM token error:", error);
    }

    try {
      // Gửi yêu cầu đăng nhập
      const response = await fetch(`http://localhost:9090/auth/token`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password }),
      });

      const result = await response.json();

      if (result.code !== 1000) {
        apii.error({
          message: "Error",
          description: result.message,
        });
        throw new Error(result.message);
      }

      // Lưu token sau khi đăng nhập thành công
      setToken(result.result?.token);
      const decodedToken = jwtDecode<JwtPayload>(result.result?.token);

      // Kiểm tra quyền hạn và chuyển hướng người dùng
      if (decodedToken.scope === "ROLE_USER") {
        navigate("/");
      } else if (decodedToken.scope === "ROLE_ADMIN") {
        navigate("/admin-page");
      } else {
        apii.error({
          message: "Error",
          description: "Invalid role",
        });
        return;
      }

      // Gửi FCM token lên server để liên kết với tài khoản của người dùng (sau khi xác thực thành công)
      if (fcmToken) {
        await fetch(`http://localhost:9090/user/save-fcm-token`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${result.result?.token}`,
          },
          body: JSON.stringify({ fcmToken }),
        });
      }
    } catch (error) {
      apii.error({
        message: "Login Error",
        description: "An error occurred during login",
      });
    }
  };

  return (
    <div className="login-page-container">
      <div className="login-page-form">
        <h2 className="login-page-title">Sign in</h2>
        <p className="login-page-description">
          Welcome back to FengShuiKoiSysTem! Please enter your details below to
          sign in.
        </p>
        {contextHolder}
        <Form
          name="normal_login"
          className="login-page-form-content"
          initialValues={{ remember: true }}
          onFinish={handleLogin}
        >
          <Form.Item
            name="email"
            rules={[{ required: true, message: "Please input your Email!" }]}
          >
            <Input
              prefix={<UserOutlined className="site-form-item-icon" />}
              placeholder="Email"
              className="login-page-input"
            />
          </Form.Item>
          <Form.Item
            name="password"
            rules={[{ required: true, message: "Please input your Password!" }]}
          >
            <Input
              prefix={<LockOutlined className="site-form-item-icon" />}
              type="password"
              placeholder="Password"
              className="login-page-input"
            />
          </Form.Item>

          <Form.Item className="login-page-remember">
            <Form.Item name="remember" valuePropName="checked" noStyle>
              <Checkbox>Remember me</Checkbox>
            </Form.Item>
            <a
              className="login-page-forgot"
              onClick={() => navigate("/forgot-password")}
            >
              Forgot password?
            </a>
          </Form.Item>

          <Form.Item>
            <Button
              type="primary"
              htmlType="submit"
              className="login-page-submit-button"
            >
              Log in
            </Button>
          </Form.Item>

          <div className="login-page-divider">
            <span>OR</span>
          </div>

          <Form.Item>
            <Button
              icon={<GoogleOutlined />}
              className="login-page-social-button"
              onClick={handleContinueWithGoogle}
            >
              Continue with Google
            </Button>
          </Form.Item>

          <Form.Item className="login-page-signup-link">
            <span> Do not have an account? </span>
            <a
              onClick={() => navigate("/register")}
              className="login-page-signup-link-text"
            >
              Sign up now
            </a>
          </Form.Item>
        </Form>
      </div>
    </div>
  );
};

export default Login;
