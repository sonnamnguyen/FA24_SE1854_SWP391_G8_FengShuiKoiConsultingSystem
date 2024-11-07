import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "../css/viewprofile.css";
import { Box, Button, TextField, Typography } from "@mui/material";
import api from "../axious/axious";

interface UserDetails {
  username: string;
  firstName: string;
  lastName: string;
  dob: string;
  roles?: { name: string }[];
  noPassword?: boolean;
  email?: string;
  phoneNumber?: string;
  avatar?: string;
}

const ViewProfile: React.FC = () => {
  const [userDetails, setUserDetails] = useState<UserDetails | null>(null);
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    getUserDetails(); 
  }, []);

  const getUserDetails = async () => {
    try {
      const response = await api.get("/users/my-info");
      if (response.data.code !== 1000) {
        throw new Error(`Error: ${response.data.message}`);
      }
      setUserDetails(response.data.result);
    } catch (error) {
      console.error("Error fetching user details:", error);
    }
  };

  const addPassword = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    const body = { password };

    try {
      const response = await api.post("/users/create-password", body); // Body passed directly
      if (response.data.code !== 1000) {
        throw new Error(response.data.message);
      }
      await getUserDetails(); // Refresh user details after password creation
      alert("Password created successfully!");
    } catch (error) {
      console.error("Error creating password:", error);
      alert("Failed to create password. Please try again.");
    }
  };

  return (
    <section className="section about-section gray-bg" id="about">
      <div className="container">
        <div className="row align-items-center flex-row-reverse">
          <div className="col-lg-6">
            <div className="about-text go-to">
              <h3 className="dark-color">About Me</h3>
              <h6 className="theme-color lead">A Member &amp; in FengShuiKoi</h6>
              <p>
                I am planning to build a <mark>Koi fish pond</mark> at my house and would like to receive <mark>Feng Shui consultation</mark> regarding this fish species. I have heard that Koi fish not only have high aesthetic value but also bring <mark>prosperity, luck, and wealth</mark> if placed in the right Feng Shui setup.
              </p>

              <div className="row about-list">
                <div className="col-md-6">
                  <div className="media">
                    <label>Birthday</label>
                    <p>{userDetails?.dob ? new Date(userDetails.dob).toLocaleDateString() : "N/A"}</p>
                  </div>
                  <div className="media">
                    <label>Phone</label>
                    <p>{userDetails?.phoneNumber || "N/A"}</p>
                  </div>
                </div>
                <div className="col-md-6">
                  <div className="media">
                    <label>E-mail</label>
                    <p>{userDetails?.email || "N/A"}</p>
                  </div>
                  <div className="media">
                    <label>Residence</label>
                    <p>Canada</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div className="col-lg-6">
            <div className="about-avatar">
              <img
                src={userDetails?.avatar}
                title="Profile Avatar"
                alt="Profile Avatar"
              />
            </div>
          </div>
        </div>

        {userDetails?.noPassword && (
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
            <Button type="submit" variant="contained" color="primary" size="large" fullWidth>
              Create Password
            </Button>
          </Box>
        )}
      </div>
    </section>
  );
};

export default ViewProfile;
