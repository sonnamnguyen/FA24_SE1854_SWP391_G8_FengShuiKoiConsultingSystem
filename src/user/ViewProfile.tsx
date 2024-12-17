import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "../css/viewprofile.css";
import { Box, Button, TextField, Typography } from "@mui/material";
import api from "../axious/axious";
import Navbar from "../layouts/header-footer/Navbar";
import Footer from "../layouts/header-footer/Footer";

interface UserDetails {
  userName: string;
  fullName: string;
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
  const [destinys, setDestiny] = useState("");
  const [searchData, setSearchData] = useState<string>("");

  useEffect(() => {
    const fetchDetails = async () => {
      await getUserDetails(); // Get user details on component mount
    };
    fetchDetails();
  }, []);

  useEffect(() => {
    if (userDetails?.dob) {
      getDestiny(); // Call getDestiny if dob is available
    }
  }, [userDetails?.dob]); // Dependency ensures this runs when dob is set

  const getDestiny = async () => {
    if (!userDetails?.dob) return;
    try {
      const year = new Date(userDetails.dob).getFullYear();
      const response = await api.get(`/destinys/autoConsultation/${year}`);
      if (response.data.code !== 1000 || !response.data.result.destiny) {
        throw new Error(`Error: ${response.data.message}`);
      }
      setDestiny(response.data.result.destiny);
    } catch (error) {
      console.error("Error fetching destiny:", error);
    }
  };

  const getUserDetails = async () => {
    try {
      const response = await api.get("/users/my-info");
      if (response.data.code !== 1000) {
        throw new Error(`Error: ${response.data.message}`);
      }
      setUserDetails(response.data.result);
      console.log(userDetails?.fullName);
    } catch (error) {
      console.error("Error fetching user details:", error);
    }
  };

  const addPassword = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const body = { password };

    try {
      const response = await api.post("/users/create-password", body);
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

  const navigateToUpdateProfile = () => {
    navigate("/update-profile");
  };

  return (
    <div>
      <Navbar searchData={searchData} setSearchData={setSearchData} />
      <div className="Pcontainer">
        <div className="col-lg-6 right-contain">
          <div className="about-avatar">
            <img
              src={userDetails?.avatar}// Use a default avatar if none is provided
              title="Profile Avatar"
              alt="Profile Avatar"
            />
          </div>
          <div>
            <button className="btnSeeMore" onClick={navigateToUpdateProfile}>
              Update Your Profile
            </button>
          </div>
        </div>
        <div className="col-lg-6 left-contain">
          <div className="about-text go-to">
            <h3 className="dark-color">Your Profile</h3>
            <div className="row about-list">
              <div className="col-lg-6">
                <div className="media">
                  <label>Birthday</label>
                  <p>
                    {userDetails?.dob
                      ? new Date(userDetails.dob).toLocaleDateString()
                      : "N/A"}
                  </p>
                </div>
                <div className="media">
                  <label>Phone</label>
                  <p>{userDetails?.phoneNumber || "N/A"}</p>
                </div>
                <div className="media">
                  <label>Destiny</label>
                  <p>{destinys || "N/A"}</p>
                </div>
              </div>
              <div className="col-lg-6">
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
      </div>
      <Footer></Footer>
    </div>
  );
};

export default ViewProfile;
