import React, { useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faUser, faLock, faEnvelope } from "@fortawesome/free-solid-svg-icons";
import "../css/NewLoginCss.css"; // Đảm bảo rằng CSS đã được thêm đúng
import { jwtDecode } from "jwt-decode";
import { setToken } from "../service/localStorageService";
import { OAuthConfig } from "../configuration/OAuthConfigType";
import { useNavigate } from "react-router-dom";
interface JwtPayload {
  iss: string;
  sub: string;
  exp: number;
  iat: number;
  jti: string;
  scope: string;
}
const LoginSignUp = () => {
  const [isSignUp, setIsSignUp] = useState(false);

  // Chuyển sang trang đăng ký
  const handleSignUpClick = () => {
    setIsSignUp(true);
  };

  // Chuyển sang trang đăng nhập
  const handleLoginClick = () => {
    setIsSignUp(false);
  };

  return (
    <div className={`wrapper ${isSignUp ? "active" : ""}`}>
      <span className="bg-animate"></span>
      <span className="bg-animate2"></span>

      {/* Login Form */}
      <div className={`form-box login ${isSignUp ? "" : "active"}`}>
        <h2
          className="animation"
          style={{ "--data": 0 } as React.CSSProperties}
        >
          Login
        </h2>
        <form action="#">
          <div
            className="input-box animation"
            style={{ "--data": 1 } as React.CSSProperties}
          >
            <input type="text" placeholder="" />
            <label>Username</label>
            <FontAwesomeIcon icon={faUser} />
          </div>

          <div
            className="input-box animation"
            style={{ "--data": 3 } as React.CSSProperties}
          >
            <input type="password" placeholder="" />
            <label>Password</label>
            <FontAwesomeIcon icon={faLock} />
          </div>

          <button
            type="submit"
            className="btn animation"
            style={{ "--data": 4 } as React.CSSProperties}
          >
            Login
          </button>

          <div
            className="reg-link animation"
            style={{ "--data": 4 } as React.CSSProperties}
          >
            <p>
              Don't have an account?{" "}
              <a href="#" className="signup-link" onClick={handleSignUpClick}>
                Sign Up
              </a>
            </p>
          </div>
        </form>
      </div>

      {/* Welcome text for login */}
      <div className={`info-text login ${isSignUp ? "" : "active"}`}>
        <h2
          className="animation"
          style={{ "--data": 0 } as React.CSSProperties}
        >
          Welcome Back!
        </h2>
        <p className="animation" style={{ "--data": 1 } as React.CSSProperties}>
          Lorem ipsum dolor sit amet consectetur adipisicing.
        </p>
      </div>

      {/* Sign-up Form */}
      <div className={`form-box signup ${isSignUp ? "active" : ""}`}>
        <h2
          className="animation"
          style={{ "--data": 17 } as React.CSSProperties}
        >
          Sign Up
        </h2>
        <form action="#">
          <div
            className="input-box animation"
            style={{ "--data": 18 } as React.CSSProperties}
          >
            <input type="text" placeholder="" />
            <label>Username</label>
            <FontAwesomeIcon icon={faUser} />
          </div>

          <div
            className="input-box animation"
            style={{ "--data": 19 } as React.CSSProperties}
          >
            <input type="email" placeholder="" />
            <label>Email</label>
            <FontAwesomeIcon icon={faEnvelope} />
          </div>

          <div
            className="input-box animation"
            style={{ "--data": 20 } as React.CSSProperties}
          >
            <input type="password" placeholder="" />
            <label>Password</label>
            <FontAwesomeIcon icon={faLock} />
          </div>

          <button
            type="submit"
            className="btn animation"
            style={{ "--data": 21 } as React.CSSProperties}
          >
            Sign Up
          </button>

          <div
            className="reg-link animation"
            style={{ "--data": 22 } as React.CSSProperties}
          >
            <p>
              Already have an account?{" "}
              <a href="#" className="login-link" onClick={handleLoginClick}>
                Login
              </a>
            </p>
          </div>
        </form>
      </div>

      {/* Welcome text for sign-up */}
      <div className={`info-text signup ${isSignUp ? "active" : ""}`}>
        <h2
          className="animation"
          style={{ "--data": 22 } as React.CSSProperties}
        >
          Welcome Back!
        </h2>
        <p
          className="animation"
          style={{ "--data": 23 } as React.CSSProperties}
        >
          Lorem ipsum dolor sit amet consectetur adipisicing.
        </p>
      </div>
    </div>
  );
};

export default LoginSignUp;