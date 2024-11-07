import React, { useState } from "react";
import { Form, Input, Button, Checkbox, notification } from "antd";
import { LockOutlined, UserOutlined, GoogleOutlined } from "@ant-design/icons";
import { useNavigate } from "react-router-dom";
import "../css/LoginForm.css"; // Link to the updated CSS file
import { OAuthConfig } from "../configuration/OAuthConfigType";
import { setToken } from "../service/localStorageService";
import imgLogin from "../img/Login.webp"; // Path to the background image
import { jwtDecode } from "jwt-decode";

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

    try {
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

      setToken(result.result?.token);
      const decodedToken = jwtDecode<JwtPayload>(result.result?.token);
      if (decodedToken.scope === "ROLE_USER") {
        navigate("/");
      } else if (decodedToken.scope === "ROLE_ADMIN") {
        navigate("/admin-page");
      } else {
        apii.error({
          message: "Error",
          description: "Invalid role",
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
    <div className="login-container">
      <div className="login-form">
        <h2>Sign in</h2>
        <p>Welcome back to FengShuiKoiSysTem! Please enter your details below to sign in.</p>
        {contextHolder}
        <Form
          name="normal_login"
          className="login-form"
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
            />
          </Form.Item>

          <Form.Item>
            <Form.Item name="remember" valuePropName="checked" noStyle>
              <Checkbox>Remember me</Checkbox>
            </Form.Item>
            <a className="login-form-forgot" onClick={() => navigate("/forgot-password")}>
              Forgot password?
            </a>
          </Form.Item>

          <Form.Item>
            <Button type="primary" htmlType="submit" className="login-form-button">
              Log in
            </Button>
          </Form.Item>

          <div className="login-divider">
            <span>OR</span>
          </div>

          <Form.Item>
            <Button
              icon={<GoogleOutlined />}
              className="social-login-button"
              onClick={handleContinueWithGoogle}
            >
              Continue with Google
            </Button>
          </Form.Item>

          <Form.Item>
            <span> Do not have an account?  </span>
            <a onClick={() => navigate("/register")} style={{ cursor: "pointer" }}>
              Sign up now
            </a>
          </Form.Item>
        </Form>
      </div>
      <div className="login-image">
        <img src={imgLogin} alt="Koi Fish Background" />
      </div>
    </div>
  );
};

export default Login;
