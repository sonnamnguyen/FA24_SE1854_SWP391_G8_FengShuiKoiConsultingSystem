import { useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import { Box, CircularProgress, Typography } from "@mui/material";
import { setToken } from "../service/localStorageService";

// Define the expected shape of the API response
interface AuthResponse {
  result?: {
    token: string;
  };
  message?: string;
  code?: number;
}

export default function Authenticate() {
  const navigate = useNavigate();
  const [isLoggedin, setIsLoggedin] = useState(false);

  useEffect(() => {
    // Extract the authorization code from the URL
    const authCodeRegex = /code=([^&]+)/;
    const isMatch = window.location.href.match(authCodeRegex);

    if (isMatch) {
      const authCode = isMatch[1];

      // Fetch token from the backend
      fetch(`http://localhost:9090/auth/outbound/authentication?code=${authCode}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
      })
        .then((response) => {
          if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
          }
          return response.json() as Promise<AuthResponse>;
        })
        .then((data) => {
          if (data.result?.token) {
            // Store the token in local storage
            setToken(data.result.token);
            setIsLoggedin(true);
          } else {
            console.error("Authentication failed:", data.message);
          }
        })
        .catch((error) => {
          console.error("Error during authentication:", error);
        });
    } else {
      console.error("Authorization code not found in the URL.");
    }
  }, []);

  // Redirect to home if login is successful
  useEffect(() => {
    if (isLoggedin) {
      navigate("/");
    }
  }, [isLoggedin, navigate]);

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        gap: "30px",
        justifyContent: "center",
        alignItems: "center",
        height: "100vh",
      }}
    >
      <CircularProgress />
      <Typography>Authenticating...</Typography>
    </Box>
  );
}
