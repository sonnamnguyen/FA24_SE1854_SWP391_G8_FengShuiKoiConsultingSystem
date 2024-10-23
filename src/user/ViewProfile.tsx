import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "../css/viewprofile.css";
import User from "../models/User";
import { getToken, setToken } from "../service/localStorageService";
import { Box, Button, TextField, Typography } from "@mui/material";

// Define UserDetails interface to align with the expected API response
interface UserDetails {
  username: string;
  firstName: string;
  lastName: string;
  dob: string;
  roles?: { name: string }[];
  noPassword?: boolean;
  email?: string;
  phoneNumber?: string;
}

const ViewProfile: React.FC = () => {
  const [userDetails, setUserDetails] = useState<UserDetails | null>(null); 
  const [password, setPassword] = useState("");
  const navigate = useNavigate(); 

  useEffect(() => {
    const fetchData = async () => {
      const tokenRefreshed = await handleRefreshToken(); 

      if (tokenRefreshed) {
        await getUserDetails(getToken()!); 
      }
    };
    fetchData();
  }, []);

  const handleRefreshToken = async () => {
    const refreshToken = getToken();
    if (!refreshToken) {
      console.error("No refresh token found");
      navigate("/login");
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
          return true;
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
    return null;
  };

  const getUserDetails = async (accessToken: string) => {
    try {
      const response = await fetch("http://localhost:9090/users/my-info", {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${accessToken}`,
        },
      });

      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }

      const data = await response.json();
      setUserDetails(data.result); // Set user details
    } catch (error) {
      console.error("Error fetching user details:", error);
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

      await getUserDetails(getToken()!); // Refresh user details after password creation
    } catch (error) {
      console.error("Error creating password:", error);
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
                Tôi đang có dự định xây dựng một <mark>hồ cá Koi</mark> tại nhà và mong muốn nhận
                được <mark>tư vấn về phong thủy</mark> liên quan đến loài cá này. Tôi đã nghe nói rằng
                cá Koi không chỉ có giá trị thẩm mỹ cao mà còn mang lại <mark>tài lộc, may mắn và sự
                thịnh vượng</mark> nếu được bố trí đúng phong thủy.
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
                src="https://bootdey.com/img/Content/avatar/avatar7.png"
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
