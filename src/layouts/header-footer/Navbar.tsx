import React, { ChangeEvent, useState, useEffect } from "react";
import { Link, NavLink, useLocation } from "react-router-dom";
import { logOut } from "../../service/authentication";
import { getToken } from "../../service/localStorageService"; // Import getToken function
import "../css/NavbarUser.css";
import logo from "../../img/logo.png";

interface NavbarProps {
  searchData: string;
  setSearchData: (search: string) => void;
}

const Navbar: React.FC<NavbarProps> = ({ searchData, setSearchData }) => {
  const [searchTerm, setSearchTerm] = useState("");
  const [isSticky, setIsSticky] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState<boolean>(false); // State to track login status

  // Sử dụng useLocation để theo dõi khi đường dẫn thay đổi
  const location = useLocation();

  // Kiểm tra trạng thái đăng nhập khi component được tải
  useEffect(() => {
    const token = getToken(); // Sử dụng getToken để kiểm tra trạng thái đăng nhập
    setIsLoggedIn(!!token); // Nếu có token thì set isLoggedIn = true, ngược lại là false
  }, []);

  // Cuộn trang lên đầu mỗi khi location thay đổi
  useEffect(() => {
    window.scrollTo(0, 0); // Cuộn lên đầu trang
  }, [location]); // Mỗi khi location thay đổi, hiệu ứng này sẽ được kích hoạt

  // Handle scroll for sticky header
  useEffect(() => {
    const handleScroll = () => {
      if (window.scrollY > 0) {
        setIsSticky(true);
      } else {
        setIsSticky(false);
      }
    };

    // Add scroll event listener
    window.addEventListener("scroll", handleScroll);

    // Cleanup the event listener when the component unmounts
    return () => {
      window.removeEventListener("scroll", handleScroll);
    };
  }, []);

  const onSearchInputChange = (e: ChangeEvent<HTMLInputElement>) => {
    setSearchTerm(e.target.value);
  };

  const handleLogout = (event: React.MouseEvent<HTMLAnchorElement>) => {
    event.preventDefault();
    logOut();
    setIsLoggedIn(false); // Cập nhật trạng thái đăng xuất
    window.location.href = "/";
  };

  const handleSearch = () => {
    setSearchData(searchTerm);
  };

  return (
    <nav
      className={`navbar navbar-expand-lg text-light fixed-top ${
        isSticky ? "sticky" : ""
      }`}
    >
      <div className="container-fluid">
        <Link className="navbar-brand" to="/">
          <img alt="Logo" src={logo} />
        </Link>
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
            <li className="nav-item dropdown">
              <NavLink
                className="nav-link dropdown-toggle menunav text-dark"
                to="#"
                role="button"
                data-bs-toggle="dropdown"
                aria-expanded="false"
              >
                Blog
              </NavLink>
              <ul className="dropdown-menu bg-light">
                <li>
                  <NavLink className="dropdown-item text-dark" to="/post-list">
                    Blogs
                  </NavLink>
                </li>
                <li>
                  <NavLink
                    className="dropdown-item text-dark"
                    to="/create-post"
                  >
                    Create Blogs
                  </NavLink>
                </li>
                <li>
                  <NavLink
                    className="dropdown-item text-dark"
                    to="/my-post-list"
                  >
                    My Blogs
                  </NavLink>
                </li>
              </ul>
            </li>
            <li className="nav-item dropdown">
              <NavLink
                className="nav-link dropdown-toggle"
                data-bs-toggle="dropdown"
                to="#"
              >
                Feng Shui Tools
              </NavLink>
              <ul className="dropdown-menu">
                <li>
                  <NavLink className="dropdown-item" to="/auto-consultation">
                    Feng Shui Consultation Tool
                  </NavLink>
                </li>
                <li>
                  <NavLink
                    className="dropdown-item"
                    to="/compatibility_caculation"
                  >
                    Compatibility Caculation Tool
                  </NavLink>
                </li>
              </ul>
            </li>
            <li className="nav-item">
              <NavLink className="nav-link menunav text-dark" to="/about-us">
                About Us
              </NavLink>
            </li>
            <li className="nav-item">
              <NavLink className="nav-link menunav text-dark" to="/koi-fishs">
                Koi Fishs
              </NavLink>
            </li>
            <li className="nav-item">
              <NavLink className="nav-link menunav text-dark" to="/koi-ponds">
                Koi Ponds
              </NavLink>
            </li>
            <li className="nav-item">
              <NavLink
                className="nav-link menunav text-dark"
                to="/consultation-request"
              >
                Book a Consultation
              </NavLink>
            </li>
          </ul>
          <form
            className="d-flex"
            role="search"
            onSubmit={(e) => e.preventDefault()}
          >
            <input
              className="form-control me-2"
              type="search"
              placeholder="Search"
              aria-label="Search"
              value={searchTerm}
              onChange={onSearchInputChange}
            />
            <button className="btnSeeMore" type="submit" onClick={handleSearch}>
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
              <img
                src="https://img.freepik.com/free-vector/hand-drawn-koi-fish-logo-template_23-2149554855.jpg"
                alt="Profile"
                className="rounded-circle"
                style={{ width: "40px", height: "40px" }}
              />
            </a>
            <ul className="dropdown-menu dropdown-menu-end bg-light">
              {!isLoggedIn && ( // Hiển thị mục "Login" nếu chưa đăng nhập
                <li>
                  <NavLink className="dropdown-item text-dark" to="/login">
                    Login
                  </NavLink>
                </li>
              )}
              {isLoggedIn && ( // Hiển thị mục "Logout" và các mục khác nếu đã đăng nhập
                <>
                  <li>
                    <NavLink
                      className="dropdown-item text-dark"
                      to="/logout"
                      onClick={handleLogout}
                    >
                      Logout
                    </NavLink>
                  </li>
                  <li>
                    <hr className="dropdown-divider" />
                  </li>
                  <li>
                    <NavLink
                      className="dropdown-item text-dark"
                      to="/update-profile"
                    >
                      Update Profile
                    </NavLink>
                  </li>
                  <li>
                    <NavLink
                      className="dropdown-item text-dark"
                      to="/update-password"
                    >
                      Update Password
                    </NavLink>
                  </li>
                  <li>
                    <NavLink
                      className="dropdown-item text-dark"
                      to="/view-profile"
                    >
                      View Profile
                    </NavLink>
                  </li>
                  <li>
                    <NavLink
                      className="dropdown-item text-dark"
                      to="/view-history"
                    >
                      History
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
