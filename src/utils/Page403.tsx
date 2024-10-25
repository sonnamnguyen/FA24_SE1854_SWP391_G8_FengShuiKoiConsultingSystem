import React from "react";
import { Navbar } from "react-bootstrap";
import { Link } from "react-router-dom";

function Page403() {
    return (
        <div className="container-fluid pt-4 px-4">
            <div className="row vh-100 bg-light rounded align-items-center justify-content-center mx-0">
                <div className="col-md-6 text-center p-4">
                    <i className="bi bi-shield-lock display-1 text-warning"></i>
                    <h1 className="display-1 fw-bold">403</h1>
                    <h1 className="mb-4">Forbidden</h1>
                    <p className="mb-4">Sorry, you do not have permission to access this page. Please check your credentials or contact the administrator.</p>
                    <Link className="btn btn-warning rounded-pill py-3 px-5" to="/">Go Back To Home</Link>
                </div>
            </div>
        </div>

    );
}
export default Page403;