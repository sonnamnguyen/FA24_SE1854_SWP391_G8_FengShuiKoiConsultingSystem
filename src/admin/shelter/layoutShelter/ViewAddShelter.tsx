import React from "react";

import '../../../css/boostrap.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import AddShelter from "../AddShelter";
import SidebarAdmin from "../../layouts/slideBar";
import NavbarAdmin from "../../layouts/headerAdmin";
import FooterAdmin from "../../layouts/footerAdmin";
const ViewAddShelter: React.FC = () => {
  return (
    <div className="container-fluid position-relative bg-white d-flex p-0">
      <SidebarAdmin />
      <div className="content">
        <NavbarAdmin />
        <div className="container-fluid pt-4 px-4">
                {/* <div className="row vh-100 bg-light rounded align-items-center justify-content-center mx-0">
                    <div className="col-md-6 text-center">
                        <h3>This is blank page</h3>
                    </div>
                </div> */}
                <AddShelter/>
            </div>
        <FooterAdmin />
      </div>
    </div>
  );
};

export default ViewAddShelter;
