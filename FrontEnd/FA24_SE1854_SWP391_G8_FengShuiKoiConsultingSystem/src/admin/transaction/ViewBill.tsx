import React, { useState } from "react";
import '../../css/boostrap.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import SidebarAdmin from "../layouts/slideBar";
import NavbarAdmin from "../layouts/headerAdmin";
import FooterAdmin from "../layouts/footerAdmin";
import BillCollection from "./collection/BillCollection";

const ViewBill: React.FC = () => {
  const [isNavbarVisible, setIsNavbarVisible] = useState(true); 

  return (
    <div className="container-fluid position-relative bg-white d-flex p-0">
      <SidebarAdmin />
      <div className="content">
        {isNavbarVisible &&<NavbarAdmin />}
        <div className="container-fluid pt-4 px-4">
          <BillCollection setIsNavbarVisible={setIsNavbarVisible} />
        </div>
        <FooterAdmin />
      </div>
    </div>
  );
};

export default ViewBill;