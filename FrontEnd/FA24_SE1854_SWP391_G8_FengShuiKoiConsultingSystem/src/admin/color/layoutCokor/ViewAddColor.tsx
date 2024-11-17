import React from "react";
import SidebarAdmin from "../../layouts/slideBar";
import NavbarAdmin from "../../layouts/headerAdmin";
import FooterAdmin from "../../layouts/footerAdmin";
import AddColor from "../AddColor";

function ViewAddColor() {
    return(
        <div className="container-fluid position-relative bg-white d-flex p-0">
        <SidebarAdmin />
        <div className="content">
          <NavbarAdmin/>
          <div className="container-fluid pt-4 px-4">
            <AddColor />
          </div>
          <FooterAdmin />
        </div>
      </div>
    );
}

export default ViewAddColor