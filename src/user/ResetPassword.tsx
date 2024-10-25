import React, { useEffect, useState } from "react";
import { getToken, removeToken, setToken } from "../service/localStorageService";
import { useNavigate } from "react-router-dom"; // Import useNavigate for routing

function ResetPassword() {
    const [password, setPassword] = useState("");
    const [passwordAgain, setPasswordAgain] = useState("");
    const [email, setEmail] = useState("");
    const [errorPassword, setErrorPassword] = useState("");
    const [errorPasswordAgain, setErrorPasswordAgain] = useState("");
    const [alert, setAlert] = useState("");
    const [status, setStatus] = useState<null | number>(null);
    const [isLoading, setIsLoading] = useState(false); // Loading state
    const navigate = useNavigate(); // Initialize navigate

    useEffect(() => {
        const emailParam = window.location.pathname.split("/")[2];
        setEmail(emailParam);
    }, []);

    const checkPassword = (password: string) => {
        const passwordRegex = /^(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{6,}$/;
        if (!passwordRegex.test(password)) {
            setErrorPassword("Password must be at least 6 characters and include at least 1 special character (!@#$%^&*).");
            return false;
        }
        setErrorPassword("");
        return true;
    };

    const checkPasswordAgain = (password: string, passwordAgainValue: string) => {
        if (passwordAgainValue !== password) {
            setErrorPasswordAgain("Passwords do not match.");
            return false;
        }
        setErrorPasswordAgain("");
        return true;
    };

    const handlePasswordAgain = (e: React.ChangeEvent<HTMLInputElement>) => {
        const value = e.target.value;
        setPasswordAgain(value);
        checkPasswordAgain(password, value);
    };

    const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const value = e.target.value;
        setPassword(value);
        checkPassword(value);
    };

    const handleRefreshToken = async () => {
        const refreshToken = getToken(); // Get refresh token from local storage or cookie
        if (!refreshToken) {
            console.error("No refresh token found");
            navigate("/login"); // Navigate to login if no token
            return null;
        }
        try {
            const response = await fetch("http://localhost:9090/auth/refresh", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ token: refreshToken }),
            });
            if (response.ok) {
                const data = await response.json();
                if (data.code === 1000) {
                    setToken(data.result.token); // Save new access token
                    return true; // Indicate success
                } else {
                    console.error("Failed to refresh token");
                    navigate("/login");
                }
            } else {
                console.error("Failed to refresh token");
                navigate("/login");
            }
        } catch (error) {
            console.error("Error refreshing token:", error);
            navigate("/login");
        }
        return null; // Indicate failure
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsLoading(true); // Start loading

        const isPasswordValid = checkPassword(password);
        const isPasswordAgainValid = checkPasswordAgain(password, passwordAgain);

        // Early return if validation fails
        if (!isPasswordValid || !isPasswordAgainValid) {
            setIsLoading(false); // Stop loading
            return;
        }

        const tokenRefreshed = await handleRefreshToken(); // Call refresh token logic

        if (tokenRefreshed) {
            try {
                const url = `http://localhost:9090/users/reset-password?email=${email}`;
                const response = await fetch(url, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: `Bearer ${getToken()}`,
                    },
                    body: JSON.stringify({ password }),
                });
                const data = await response.json();

                if (response.ok) {
                    if (data.code === 1000) {
                        removeToken(); // Remove the token
                        setStatus(1); // Success
                        setTimeout(() => navigate("/login"), 3000); // Redirect after 3 seconds
                    } else {
                        setStatus(0);
                        setAlert("Reset password failed: " + data.result);
                    }
                } else {
                    setStatus(0);
                    setAlert("Error: " + (data.message || "An unexpected error occurred."));
                }
            } catch (error) {
                console.error("Error occurred during password reset:", error);
                setAlert("An error occurred during password reset.");
            } finally {
                setIsLoading(false); // Stop loading
            }
        } else {
            setIsLoading(false); // Stop loading if refresh token failed
        }
    };

    const handleGoToLogin = () => {
        navigate("/login"); // Navigate to login page
    };

    return (
        <>
            <div className="d-flex justify-content-center align-items-center vh-100">
                <div className="card p-4" style={{ width: "350px" }}>
                    <div className="card-body text-center">
                        <h3 className="card-title">Reset Password</h3>
                        <form onSubmit={handleSubmit} className="form">
                            <div className="mb-3">
                                <input
                                    type="password"
                                    id="newPassword"
                                    className="form-control"
                                    placeholder="New Password"
                                    value={password}
                                    onChange={handlePasswordChange}
                                    required
                                />
                                <div style={{ color: 'red' }}>{errorPassword}</div>
                            </div>
                            <div className="mb-3">
                                <input
                                    type="password"
                                    id="confirmPassword"
                                    className="form-control"
                                    placeholder="Confirm Password"
                                    value={passwordAgain}
                                    onChange={handlePasswordAgain}
                                    required
                                />
                                <div style={{ color: 'red' }}>{errorPasswordAgain}</div>
                            </div>
                            <button type="submit" className="btn btn-dark w-100" disabled={isLoading}>
                                {isLoading ? "Processing..." : "Reset Password"}
                            </button>
                        </form>
                    </div>
                </div>
            </div>

            <div className="activate-container">
                {status === 1 ? (
                    <div className="popup success">
                        <div className="popup-content">
                            <div className="popup-icon success-icon">✔</div>
                            <h2>Success</h2>
                            <p>Your password has been reset successfully.</p>
                            <button className="popup-button" onClick={handleGoToLogin}>
                                Go to Login Page
                            </button>
                        </div>
                    </div>
                ) : status === 0 ? (
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
}

export default ResetPassword;
