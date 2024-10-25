import React from "react";
import FooterAdmin from "../../layouts/footerAdmin";
import NavbarAdmin from "../../layouts/headerAdmin";
import SidebarAdmin from "../../layouts/slideBar";
import AddShape from "../AddShape";


function ViewAddShape() {
    return(
        <div className="container-fluid position-relative bg-white d-flex p-0">
        <SidebarAdmin />
        <div className="content">
          <NavbarAdmin/>
          <div className="container-fluid pt-4 px-4">
            <AddShape />
          </div>
          <FooterAdmin />
        </div>
      </div>
    );
}

export default ViewAddShape