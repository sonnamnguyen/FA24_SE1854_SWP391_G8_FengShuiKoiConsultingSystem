import React, { useState } from "react";
import { Box, Button, TextField, Typography } from "@mui/material";
import api from "../axious/axious";
import Navbar from "../layouts/header-footer/Navbar";
import Footer from "../layouts/header-footer/Footer";

const UpdatePassword: React.FC = () => {
  const [currentPassword, setCurrentPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);
  const [searchData, setSearchData] = useState<string>("");

  const handleUpdatePassword = async (
    event: React.FormEvent<HTMLFormElement>
  ) => {
    event.preventDefault();

    if (newPassword !== confirmPassword) {
      setError("New password and confirmation do not match.");
      return;
    }

    try {
      const response = await api.post("/users/update-password", {
        currentPassword,
        newPassword,
      });

      if (response.data.code !== 1000) {
        throw new Error(response.data.message);
      }

      setSuccessMessage("Password updated successfully!");
      setError(null);
      setCurrentPassword("");
      setNewPassword("");
      setConfirmPassword("");
    } catch (error) {
      console.error("Error updating password:", error);
      setError(
        "Failed to update password. Please ensure your current password is correct."
      );
      setSuccessMessage(null);
    }
  };

  return (
    <div>
      <Navbar searchData={searchData} setSearchData={setSearchData} />

      <Box
        component="form"
        onSubmit={handleUpdatePassword}
        sx={{
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          gap: 2,
          padding: 3,
          maxWidth: 400,
          margin: "150px auto",
        }}
      >
        <Typography variant="h5">Update Password</Typography>

        {error && <Typography color="error">{error}</Typography>}
        {successMessage && (
          <Typography color="primary">{successMessage}</Typography>
        )}

        <TextField
          label="Current Password"
          type="password"
          value={currentPassword}
          onChange={(e) => setCurrentPassword(e.target.value)}
          fullWidth
          required
        />

        <TextField
          label="New Password"
          type="password"
          value={newPassword}
          onChange={(e) => setNewPassword(e.target.value)}
          fullWidth
          required
        />

        <TextField
          label="Confirm New Password"
          type="password"
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
          fullWidth
          required
        />

        <Button type="submit" variant="contained" color="primary" fullWidth>
          Update Password
        </Button>
      </Box>
      <Footer></Footer>
    </div>
  );
};

export default UpdatePassword;
