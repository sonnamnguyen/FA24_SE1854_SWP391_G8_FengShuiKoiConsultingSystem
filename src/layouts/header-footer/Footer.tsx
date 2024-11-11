import React from "react";
import { Link } from "react-router-dom";
import "../css/FooterUser.css";

function Footer() {
  return (
    // Footer
    <footer className="text-center text-lg-start bg-body-tertiary text-muted">
      {/* Section: Social media */}
      <section className="d-flex justify-content-center justify-content-lg-between p-4 border-bottom">
        {/* Left */}
        <div className="me-5 d-none d-lg-block">
          <span>Get connected with us on social networks:</span>
        </div>
        {/* Right */}
        <div>
          <Link to="/" className="me-4 text-reset">
            <i className="fab fa-facebook-f"></i>
          </Link>
          <Link to="/" className="me-4 text-reset">
            <i className="fab fa-twitter"></i>
          </Link>
          <Link to="/" className="me-4 text-reset">
            <i className="fab fa-google"></i>
          </Link>
          <Link to="/" className="me-4 text-reset">
            <i className="fab fa-instagram"></i>
          </Link>
          <Link to="/" className="me-4 text-reset">
            <i className="fab fa-linkedin"></i>
          </Link>
          <Link to="/" className="me-4 text-reset">
            <i className="fab fa-github"></i>
          </Link>
        </div>
      </section>
      {/* Section: Links */}
      <section>
        <div className="container text-center text-md-start mt-5">
          {/* Grid row */}
          <div className="row mt-3">
            {/* Grid column */}
            <div className="col-md-3 col-lg-4 col-xl-3 mx-auto mb-4">
              {/* Content */}
              <h6 className="text-uppercase fw-bold mb-4">
                <i className="fas fa-gem me-3"></i>KoiFeng Shui
              </h6>
              <p>
                Koi Feng Shui aimed at creating a harmonious environment that
                brings wealth, good luck, and positive energy to one's living or
                working space.
              </p>
            </div>
            {/* Grid column */}
            <div className="col-md-2 col-lg-2 col-xl-2 mx-auto mb-4">
              {/* Links */}
              <h6 className="text-uppercase fw-bold mb-4">Products</h6>
              <p>
                <Link to="/" className="text-reset">
                  HomePage
                </Link>
              </p>
              <p>
                <Link to="/post-list" className="text-reset">
                  View Blog
                </Link>
              </p>
              <p>
                <Link to="/auto-consultation" className="text-reset">
                  FengShui Tool
                </Link>
              </p>
              <p>
                <Link to="/consultation-request" className="text-reset">
                  Consultation
                </Link>
              </p>
            </div>
            {/* Grid column */}
            <div className="col-md-3 col-lg-2 col-xl-2 mx-auto mb-4">
              {/* Links */}
              <h6 className="text-uppercase fw-bold mb-4">Useful links</h6>
              <p>
                <Link to="/" className="text-reset">
                  Pricing
                </Link>
              </p>
              <p>
                <Link to="/view-profile" className="text-reset">
                  Settings
                </Link>
              </p>
              <p>
                <Link to="/about-us" className="text-reset">
                  About Us
                </Link>
              </p>
              <p>
                <Link to="/" className="text-reset">
                  Help
                </Link>
              </p>
            </div>
            {/* Grid column */}
            <div className="col-md-4 col-lg-3 col-xl-3 mx-auto mb-md-0 mb-4">
              {/* Links */}
              <h6 className="text-uppercase fw-bold mb-4">Contact</h6>
              <p>
                <i className="fas fa-home me-3"></i> Japan, JP 10012,Tokyo
              </p>
              <p>
                <i className="fas fa-envelope me-3"></i> FengShuiKoi@gmail.com
              </p>
              <p>
                <i className="fas fa-phone me-3"></i> +84986 888 666
              </p>
              <p>
                <i className="fas fa-print me-3"></i> +84986 888 666
              </p>
            </div>
          </div>
        </div>
      </section>
    </footer>
  );
}

export default Footer;
