import React from 'react';
import { NavLink } from 'react-router-dom';
import { logOut } from '../../service/authentication';

const NavbarAdmin: React.FC = () => {
    const handleLogout = (event: React.MouseEvent<HTMLAnchorElement>) => {
        event.preventDefault();
        logOut();
        window.location.href = "/login";
      };

      
  return (
    <nav className="navbar navbar-expand bg-light navbar-light sticky-top px-4 py-0">
      <a href="index.html" className="navbar-brand d-flex d-lg-none me-4">
        <h2 className="text-primary mb-0">
          <i className="fa fa-hashtag"></i>
        </h2>
      </a>
      {/* <a href="#" className="sidebar-toggler flex-shrink-0">
        <i className="fa fa-bars"></i>
      </a> */}
      
      <div className="navbar-nav align-items-center ms-auto">
       
       
        
        <div className="nav-item dropdown">
          <a href="#" className="nav-link dropdown-toggle" data-bs-toggle="dropdown">
            <img className="rounded-circle me-lg-2" src="img/user.jpg" alt="" style={{ width: "40px", height: "40px" }} />
            <span className="d-none d-lg-inline-flex">John Doe</span>
          </a>
          <div className="dropdown-menu dropdown-menu-end bg-light border-0 rounded-0 rounded-bottom m-0">
            <li>
              <NavLink className="dropdown-item" to="/login">
                Login
              </NavLink>
            </li>
            <li>
              <NavLink className="dropdown-item" to="/logout" onClick={handleLogout}>
                Logout
              </NavLink>
            </li>
            <li>
              <hr className="dropdown-divider" />
            </li>
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
          </div>
        </div>
      </div>
    </nav>
  );
};

export default NavbarAdmin;
