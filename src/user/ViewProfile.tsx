import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom"; // Đảm bảo bạn đã import useNavigate
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
  const navigate = useNavigate(); // Hook to navigate to different routes
  const [destinys, setDestiny] = useState("");
  const [searchData, setSearchData] = useState<string>("");

  useEffect(() => {
    const fetchDetails = async () => {
      await getUserDetails(); // Lấy thông tin người dùng khi component mount
      if (userDetails?.dob) {
        await getDestiny(); // Gọi getDestiny nếu ngày sinh có sẵn
      }
    };
    fetchDetails();
  }, []); // Empty dependency array ensures this runs once after mount

  const getDestiny = async () => {
    if (!userDetails?.dob) return;
    try {
      // Extract the year from dob
      const year = new Date(userDetails.dob).getFullYear();
      const response = await api.get(`/destinys/autoConsultation/${year}`);
      if (response.data.code !== 1000) {
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

  // Hàm điều hướng đến trang cập nhật hồ sơ
  const navigateToUpdateProfile = () => {
    navigate("/update-profile"); // Điều hướng đến trang /update-profile
  };

  return (
    <div>
      <Navbar searchData={searchData} setSearchData={setSearchData} />
      <div className="Pcontainer">
        <div className="col-lg-6 right-contain">
          <div className="about-avatar">
            <img
              src={userDetails?.avatar}
              title="Profile Avatar"
              alt="Profile Avatar"
            />
          </div>
          <div>
            {/* Thêm sự kiện onClick để điều hướng khi nhấn nút */}
            <button className="btnSeeMore" onClick={navigateToUpdateProfile}>
              Update Your Profile
            </button>
          </div>
        </div>
        <div className="col-lg-6 left-contain">
          <div className="about-text go-to">
            <h3 className="dark-color">Your Profile</h3>
            <h6 className="theme-color lead">
              {userDetails?.fullName || "User not found"}
            </h6>
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
