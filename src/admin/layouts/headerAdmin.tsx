import React, { useEffect, useState } from 'react';
import { NavLink } from 'react-router-dom';
import { logOut } from '../../service/authentication';
import api from '../../axious/axious';
import User from '../../models/User';

const NavbarAdmin: React.FC = () => {
  const [userDetails, setUserDetails] = useState<User | null>(null);

  // Handle user logout
  const handleLogout = (event: React.MouseEvent<HTMLAnchorElement>) => {
    event.preventDefault();
    logOut();
    window.location.href = "/login"; // Redirect to login after logout
  };

  // Fetch user details on component mount
  useEffect(() => {
    const fetchUserDetails = async () => {
      try {
        const response = await api.get("/users/my-info");
        if (response.data.code !== 1000) {
          throw new Error(`Error! Code: ${response.data.message}`);
        }
        setUserDetails(response.data.result); // Set user details if fetched successfully
      } catch (error) {
        console.error("Error fetching user details:", error);
      }
    };

    fetchUserDetails();
  }, []);

  return (
    <nav className="navbar navbar-expand bg-light navbar-light sticky-top px-4 py-0">
      <NavLink to="/" className="navbar-brand d-flex d-lg-none me-4">
        <h2 className="text-primary mb-0">
          <i className="fa fa-hashtag"></i>
        </h2>
      </NavLink>

      <div className="navbar-nav align-items-center ms-auto">
        <div className="nav-item dropdown">
          <a href="#" className="nav-link dropdown-toggle" data-bs-toggle="dropdown">
        
            <img
              className="rounded-circle me-lg-2"
              src={userDetails?.avatar || "/path/to/default-avatar.png"} 
              alt="Avatar"
              style={{ width: "40px", height: "40px" }}
            />
            <span className="d-none d-lg-inline-flex">{userDetails?.username || "Guest"}</span>
          </a>
          <div className="dropdown-menu dropdown-menu-end bg-light border-0 rounded-0 rounded-bottom m-0">
            {userDetails ? (
              <>
                <NavLink className="dropdown-item" to="/view-profile">
                  View Profile
                </NavLink>
                <NavLink className="dropdown-item" to="/update-profile">
                  Update Profile
                </NavLink>
                <NavLink className="dropdown-item" to="/logout" onClick={handleLogout}>
                  Logout
                </NavLink>
              </>
            ) : (
              <NavLink className="dropdown-item" to="/login">
                Login
              </NavLink>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
};

export default NavbarAdmin;
