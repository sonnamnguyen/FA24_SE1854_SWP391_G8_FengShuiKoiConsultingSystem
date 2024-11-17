import React, { useEffect, useState } from "react";
import "../css/ActivateAccount.css"; 

function ActivateAccount() {
    const [email, setEmail] = useState("");
    const [code, setCode] = useState("");
    const [status, setStatus] = useState("");
    const [alert, setAlert] = useState("");

    useEffect(() => {
        const emailParam = window.location.pathname.split("/")[2]; 
        const codeParam = window.location.pathname.split("/")[3]; 

        if (emailParam && codeParam) {
            // Instead of setting state, directly use the parameters in handleActivate
            const handleActivate = async () => {
                try {
                    const url: string = `http://localhost:9090/users/activate?email=${encodeURIComponent(emailParam)}&code=${encodeURIComponent(codeParam)}`;
                    console.log("Activation URL:", url);

                    const response = await fetch(url, {
                        method: "GET",
                        headers: {
                            "Content-Type": "application/json",
                        },
                    });

                    if (response.ok) {
                        const data = await response.json();
                        console.log(data);
                        if (data.code === 1000) {
                            setStatus("ACTIVE");
                        } else {
                            setAlert("Activation failed: " + data.result);
                        }
                    } else {
                        setAlert("Account or activation code is invalid.");
                    }
                } catch (error) {
                    setAlert("An error occurred during activation.");
                }
            };

            handleActivate();
        }
    }, []);
    
    const handleGoToLogin = () => {
        window.location.href = "/login";
    };

    return (
        <div className="activate-container">
            {status ? (
                <div className="popup success">
                    <div className="popup-content">
                        <div className="popup-icon success-icon">✔</div>
                        <h2>Success</h2>
                        <p>Your account has been successfully activated.</p>
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
                    <p>Activating ...</p>
                </div>
            )}
        </div>
    );
}

export default ActivateAccount;
