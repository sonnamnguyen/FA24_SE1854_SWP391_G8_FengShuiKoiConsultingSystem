import {
  Alert,
  Box,
  Button,
  Card,
  CardActions,
  CardContent,
  Divider,
  Snackbar,
  TextField,
  Typography,
} from "@mui/material";
import GoogleIcon from "@mui/icons-material/Google";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getToken, setToken } from "../service/localStorageService";
import { OAuthConfig } from "../configuration/OAuthConfigType";
import Navbar from "../layouts/header-footer/Navbar";
import { jwtDecode } from "jwt-decode";


interface JwtPayload {
  iss: string;
  sub: string;
  exp: number;
  iat: number;
  jti: string;
  scope: string; 
}
export default function Login() {
  const navigate = useNavigate();
  
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [snackBarOpen, setSnackBarOpen] = useState<boolean>(false);
  const [snackBarMessage, setSnackBarMessage] = useState<string>("");
  const [snackType, setSnackType] = useState<"error" | "success">("error");

  const handleContinueWithGoogle = () => {
    const { redirectUri, authUri, clientId } = OAuthConfig;
    const targetUrl = `${authUri}?redirect_uri=${encodeURIComponent(
      redirectUri
    )}&response_type=code&client_id=${clientId}&scope=openid%20email%20profile`;

    console.log(targetUrl);
    window.location.href = targetUrl;
  };

  // useEffect(() => {
  //   const accessToken = getToken();
  //   if (accessToken) {
  //     console.log("Access Token: ", accessToken);
  //     navigate("/");
  //   }
  // }, [navigate]);

  const handleCloseSnackBar = (
    event: React.SyntheticEvent | Event,
    reason?: string
  ) => {
    if (reason === "clickaway") return;
    setSnackBarOpen(false);
  };

  const showError = (message: string) => {
    setSnackType("error");
    setSnackBarMessage(message);
    setSnackBarOpen(true);
  };

  const showSuccess = (message: string) => {
    setSnackType("success");
    setSnackBarMessage(message);
    setSnackBarOpen(true);
  };

  const handleLogin = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
  
    if (!email || !password) {
      showError("Username and password cannot be empty");
      return;
    }
  
    const data = { email, password };
  
    try {
      const response = await fetch(`http://localhost:9090/auth/token`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
      });
  
      const result = await response.json();
  
      if (result.code !== 1000) {
        throw new Error(result.message);
      }
  
      setToken(result.result?.token);
      const decodedToken = jwtDecode<JwtPayload>(result.result?.token);
      console.log(decodedToken);
  
      if (decodedToken.scope === "ROLE_USER") {
        navigate("/");
      } else if (decodedToken.scope === "ROLE_ADMIN") {
        navigate("/admin-page");
      } else {
        showError("Invalid role");
      }
    } catch (error) {
      showError("hehe");
    }
  };
  


  return (
    <>
      <Snackbar
        open={snackBarOpen}
        onClose={handleCloseSnackBar}
        autoHideDuration={6000}
        anchorOrigin={{ vertical: "top", horizontal: "right" }}
      >
        <Alert
          onClose={handleCloseSnackBar}
          severity={snackType}
          variant="filled"
          sx={{ width: "100%" }}
        >
          {snackBarMessage}
        </Alert>
      </Snackbar>
      <Box
        display="flex"
        flexDirection="column"
        alignItems="center"
        justifyContent="center"
        height="100vh"
        bgcolor={"#f0f2f5"}
      >
        <Card
          sx={{
            minWidth: 250,
            maxWidth: 400,
            boxShadow: 4,
            borderRadius: 4,
            padding: 4,
          }}
        >
          <CardContent>
          <Typography variant="h5" component="h1" gutterBottom textAlign="center">
              LOGIN 
            </Typography>
            <Box component="form" onSubmit={handleLogin} sx={{ mt: 2 }}>
              <TextField
                label="Username"
                variant="outlined"
                fullWidth
                margin="normal"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
              <TextField
                label="Password"
                type="password"
                variant="outlined"
                fullWidth
                margin="normal"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
              <Button
                type="submit"
                variant="contained"
                color="primary"
                size="large"
                fullWidth
                sx={{ mt: 2 }}
              >
                Login
              </Button>
            </Box>
          </CardContent>
          <CardActions>
            <Box display="flex" flexDirection="column" width="100%" gap="25px">
              <Button
                type="button"
                variant="contained"
                color="secondary"
                size="large"
                onClick={handleContinueWithGoogle}
                fullWidth
                sx={{ gap: "10px" }}
              >
                <GoogleIcon />
                Continue with Google
              </Button>
              <Divider />
              <Button
                type="button"
                variant="contained"
                color="success"
                size="large"
                onClick={() => navigate("/register")}
              >
                Create an account
              </Button>
            </Box>
          </CardActions>

          <Box display="flex" justifyContent="center" mt={2}>
            <Button
              type="button"
              color="inherit"
              onClick={() => navigate("/forgot-password")}
            >
              Forgot password?
            </Button>
          </Box>
        </Card>
      </Box>
    </>
  );
}
