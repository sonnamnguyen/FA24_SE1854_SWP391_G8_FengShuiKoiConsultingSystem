import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getToken } from "../service/localStorageService";
import {
  Alert,
  Box,
  Button,
  Card,
  CircularProgress,
  Snackbar,
  TextField,
  Typography,
} from "@mui/material";
import Navbar from "../layouts/header-footer/Navbar";

interface UserDetails {
  username: string;
  firstName: string;
  lastName: string;
  dob: string;
  roles?: { name: string }[];
  noPassword?: boolean;
}

export default function Home() {
  const navigate = useNavigate();
  const [userDetails, setUserDetails] = useState<UserDetails | null>(null);
  const [password, setPassword] = useState<string>("");
  const [snackBarOpen, setSnackBarOpen] = useState<boolean>(false);
  const [snackBarMessage, setSnackBarMessage] = useState<string>("");
  const [snackType, setSnackType] = useState<"error" | "success">("error");
  const [searchData, setSearchData] = useState<string>("");

  const handleCloseSnackBar = (event: React.SyntheticEvent | Event, reason?: string) => {
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

  const getUserDetails = async (accessToken: string) => {
    try {
      const response = await fetch("http://localhost:9090/users/my-info", {
        method: "GET",
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      });

      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }

      const data = await response.json();
      setUserDetails(data.result);
    } catch (error) {
      console.error("Error fetching user details:", error);
      showError("Failed to fetch user details. Please try again.");
    }
  };

  const addPassword = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    const body = { password };

    try {
      const response = await fetch("http://localhost:9090/users/create-password", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${getToken()}`,
        },
        body: JSON.stringify(body),
      });

      const data = await response.json();
      if (data.code !== 1000) throw new Error(data.message);

      await getUserDetails(getToken()!);
      showSuccess(data.message);
    } catch (error) {
      showError(error instanceof Error ? error.message : "Failed to create password.");
    }
  };

  useEffect(() => {
    const accessToken = getToken();

    if (!accessToken) {
      navigate("/login");
      return;
    }

    getUserDetails(accessToken);
  }, [navigate]);

  return (
    <>
      <Navbar searchData={searchData} setSearchData={setSearchData} />
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
      {userDetails ? (
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
              minWidth: 400,
              maxWidth: 500,
              boxShadow: 4,
              borderRadius: 2,
              padding: 4,
            }}
          >
            <Box
              sx={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                width: "100%",
              }}
            >
              <Typography variant="h5">Welcome back to FengShui System, {userDetails.username}</Typography>
              <Typography variant="h6" className="name">{`${userDetails.firstName} ${userDetails.lastName}`}</Typography>
              <Typography className="email">{userDetails.dob}</Typography>
              <ul>
                User's roles:
                {userDetails.roles?.map((item, index) => (
                  <li className="email" key={index}>
                    {item.name}
                  </li>
                ))}
              </ul>
              {userDetails.noPassword && (
                <Box
                  component="form"
                  onSubmit={addPassword}
                  sx={{
                    display: "flex",
                    flexDirection: "column",
                    gap: "10px",
                    width: "100%",
                  }}
                >
                  <Typography>Do you want to create a password?</Typography>
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
                  >
                    Create Password
                  </Button>
                </Box>
              )}
            </Box>
          </Card>
        </Box>
      ) : (
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
          <Typography>Loading ...</Typography>
        </Box>
      )}
    </>
  );
}
