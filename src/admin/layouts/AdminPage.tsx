import React from "react";

import FooterAdmin from "./footerAdmin";
import SidebarAdmin from "./slideBar";
import NavbarAdmin from "./headerAdmin";
import '../../css/boostrap.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import AnimalCollection from "../AnimalCollection";
import AddKoiFish from "../animal/AddKoiFish";
import ShelterViewAdmin from "../ShelterCollection";
import ShelterCollection from "../ShelterCollection";
const AdminPage: React.FC = () => {
  return (
    <div className="container-fluid position-relative bg-white d-flex p-0">
      <SidebarAdmin />
      <div className="content">
        <NavbarAdmin />
        <div className="container-fluid pt-4 px-4">
                 <div className="row vh-100 bg-light rounded align-items-center justify-content-center mx-0">
                    <div className="col-md-6 text-center">
                        <h3>This is blank page</h3>
                    </div>
                </div> 
   
            </div>
        <FooterAdmin />
      </div>
    </div>
  );
};

export default AdminPage;
