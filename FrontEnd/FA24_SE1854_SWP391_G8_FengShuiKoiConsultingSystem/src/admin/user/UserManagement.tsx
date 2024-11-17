import React, { useState } from "react";
import '../../../src/css/boostrap.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import SidebarAdmin from "../layouts/slideBar";
import NavbarAdmin from "../layouts/headerAdmin";
import FooterAdmin from "../layouts/footerAdmin";
import UserCollection from "../UserCollection";

const ViewUserManagement: React.FC = () => {
  const [isNavbarVisible, setIsNavbarVisible] = useState(true);

  return (
    <div className="container-fluid position-relative bg-white d-flex p-0">
      <SidebarAdmin />
      <div className="content">
        {isNavbarVisible && <NavbarAdmin />}
        <div className="container-fluid pt-4 px-4">
          <UserCollection setIsNavbarVisible={setIsNavbarVisible}/>
        </div>
        <FooterAdmin />
      </div>
    </div>
  );
};

export default ViewUserManagement;
