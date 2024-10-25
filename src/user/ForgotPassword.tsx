import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import { getToken, setToken } from "../service/localStorageService";

const ForgotPassword: React.FC = () => {
    const [email, setEmail] = useState<string>("");
    const [status, setStatus] = useState<string>("");
    const [alert, setAlert] = useState<string>("");
    const [errorEmail, setErrorEmail] = useState<string>("");
    const navigate = useNavigate();

    const handleGoToLogin = () => {
        navigate("/login");
    };

    const checkEmail = (email: string): boolean => {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
            setErrorEmail("Please enter a valid email address");
            return false;
        } else {
            setErrorEmail("");
            return true;
        }
    };

 

    
    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        setErrorEmail("");

        const isEmailValid = checkEmail(email);
        if (!isEmailValid) {
            return;
        }


        const data = { email: email };
        

        try {
            const response = await fetch(`http://localhost:9090/auth/token-email`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(data),
            });

            if (!response.ok) {
                throw new Error("Failed to fetch token");
            }

            const result = await response.json();

            if (result.code !== 1000) {
                setAlert(result.message);
                return;
            }

            setToken(result.result.token);
        } catch (error) {
            setAlert("Error: ");
        }
        try {
            const response = await fetch(`http://localhost:9090/users/forgot-password?email=${email}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${getToken()}`,
                },
                body: JSON.stringify(data),
            });

            if (response.ok) {
                const result = await response.json();
                if (result.code === 1000) {
                    setStatus("A reset password link has been sent to your email.");
                } else {
                    setAlert("Reset password failed: " + result.result);
                }
            } else {
                setAlert("Invalid account.");
            }
        } catch (error) {
            setAlert("An error occurred during the password reset process.");
        }
    };

    return (
        <>
            <div className="d-flex justify-content-center align-items-center vh-100">
                <div className="card p-4" style={{ width: "350px" }}>
                    <div className="card-body text-center">
                        <h3 className="card-title">Forgot Password</h3>
                        <form onSubmit={handleSubmit} className="form">
                            <div className="mb-3">
                                <input
                                    type="email"
                                    id="email"
                                    className="form-control"
                                    placeholder="Email"
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                    required
                                />
                                <div style={{ color: 'red' }}>{errorEmail}</div>
                            </div>
                            <button type="submit" className="btn btn-dark w-100">Recover Password</button>
                        </form>
                    </div>
                </div>
            </div>

            <div className="activate-container">
                {status ? (
                    <div className="popup success">
                        <div className="popup-content">
                            <div className="popup-icon success-icon">✔</div>
                            <h2>Success</h2>
                            <p>{status}</p>
                            <button className="popup-button" onClick={handleGoToLogin}>
                                Go to Login Page
                            </button>
                        </div>
                    </div>
                ) : alert ? (
                    <div className="popup error">
                        <div className="popup-content">
                            <div className="popup-icon error-icon">✖</div>
                            <h2>Failure</h2>
                            <p>{alert}</p>
                            <button className="popup-button" onClick={handleGoToLogin}>
                                Go to Login Page
                            </button>
                        </div>
                    </div>
                ) : (
                    <div className="loading-message">
                        <p>Processing...</p>
                    </div>
                )}
            </div>
        </>
    );
};

export default ForgotPassword;
