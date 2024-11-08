import React, { ChangeEvent, useState, useEffect } from "react";
import { Link, NavLink } from "react-router-dom";
import { logOut } from "../../service/authentication";
import api from "../../axious/axious";
import User from "../../models/User";

interface NavbarProps {
  searchData: string;
  setSearchData: (search: string) => void;
}

const Navbar: React.FC<NavbarProps> = ({ searchData, setSearchData }) => {
  const [searchTerm, setSearchTerm] = useState("");
  const [userDetails, setUserDetails] = useState<User | null>(null); // Added userDetails state
  
  // Fetch user details on component mount
  useEffect(() => {
    const fetchUserDetails = async () => {
      try {
        const response = await api.get("/users/my-info");
        if (response.data.code !== 1000) {
          throw new Error(`Error! Code: ${response.data.code}`);
        }
        setUserDetails(response.data.result);  // Assuming result contains user info
      } catch (error) {
        console.error("Error fetching user details:", error);
      }
    };

    fetchUserDetails();
  }, []); // Empty dependency array ensures this runs only once on mount

  const onSearchInputChange = (e: ChangeEvent<HTMLInputElement>) => {
    setSearchTerm(e.target.value);
  };

  const handleLogout = (event: React.MouseEvent<HTMLAnchorElement>) => {
    event.preventDefault();
    logOut();
    setUserDetails(null); // Clear user details on logout
    window.location.href = "/login";
  };

  const handleSearch = () => {
    setSearchData(searchTerm);
  };

  return (
    <nav className="navbar navbar-expand-lg bg-body-tertiary">
      <div className="container-fluid">
        <a className="navbar-brand" href="#">FengShuiKoiSystem</a>
        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarSupportedContent"
          aria-controls="navbarSupportedContent"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className="collapse navbar-collapse" id="navbarSupportedContent">
          <ul className="navbar-nav me-auto mb-2 mb-lg-0">
            <li className="nav-item">
              <NavLink className="nav-link active" aria-current="page" to="#">
                Blog
              </NavLink>
            </li>
            <li className="nav-item">
              <NavLink className="nav-link" to="#">
                Destiny
              </NavLink>
            </li>
            <li className="nav-item">
              <NavLink className="nav-link disabled" aria-disabled="true" to= "">
                KoiFish
              </NavLink>
            </li>
            <li className="nav-item">
              <NavLink className="nav-link" to="/consultation-request">
                Book a consultation
              </NavLink>
            </li>
          </ul>
          <form className="d-flex" role="search" onSubmit={(e) => e.preventDefault()}>
            <input
              className="form-control me-2"
              type="search"
              placeholder="Search"
              aria-label="Search"
              value={searchTerm}
              onChange={onSearchInputChange}
            />
            <button className="btn btn-outline-success" type="submit" onClick={handleSearch}>
              Search
            </button>
          </form>
          <li className="nav-item dropdown">
            <a
              className="nav-link dropdown-toggle"
              href="#"
              role="button"
              data-bs-toggle="dropdown"
              aria-expanded="false"
            >
              {userDetails ? (
                <div>
                  <img
                    src={userDetails.avatar || "https://via.placeholder.com/40"} 
                    alt="Profile"
                    className="rounded-circle"
                    style={{ width: "40px", height: "40px" }}
                  />
                  <span className="ms-2">{userDetails.username}</span>
                </div>
              ) : (
                <img
                  src="https://via.placeholder.com/40" 
                  alt="Profile"
                  className="rounded-circle"
                  style={{ width: "40px", height: "40px" }}
                />
              )}
            </a>
            <ul className="dropdown-menu dropdown-menu-end">
              {!userDetails ? (
                <li>
                  <NavLink className="dropdown-item" to="/login">
                    Login
                  </NavLink>
                </li>
              ) : (
                <>
                  <li>
                    <NavLink className="dropdown-item" to="/update-profile">
                      Update Profile
                    </NavLink>
                  </li>
                  <li>
                    <NavLink className="dropdown-item" to="/view-profile">
                      View Profile
                    </NavLink>
                  </li>
                  <li>
                    <NavLink className="dropdown-item" to="/view-history">
                      History
                    </NavLink>
                  </li>
                  <li>
                    <NavLink className="dropdown-item" to="/logout" onClick={handleLogout}>
                      Logout
                    </NavLink>
                  </li>
                  
                </>
              )}
            </ul>
          </li>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
