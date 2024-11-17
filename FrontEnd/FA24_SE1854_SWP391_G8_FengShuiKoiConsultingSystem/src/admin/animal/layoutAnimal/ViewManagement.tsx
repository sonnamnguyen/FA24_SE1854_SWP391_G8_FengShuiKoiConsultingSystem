import React, { useState } from "react";
import '../../../css/boostrap.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import SidebarAdmin from "../../layouts/slideBar";
import NavbarAdmin from "../../layouts/headerAdmin";
import FooterAdmin from "../../layouts/footerAdmin";
import AnimalCollection from "../../AnimalCollection";
const ViewManagement: React.FC = () => {
  const [isNavbarVisible, setIsNavbarVisible] = useState(true); // Tạo trạng thái để điều khiển navbar

  return (
    <div className="container-fluid position-relative bg-white d-flex p-0">
      <SidebarAdmin />
      <div className="content">
        {isNavbarVisible && <NavbarAdmin />} {/* Hiển thị navbar khi isNavbarVisible là true */}
        <div className="container-fluid pt-4 px-4">
        <AnimalCollection setIsNavbarVisible={setIsNavbarVisible} /> {/* Truyền hàm điều khiển cho AnimalCollection */}
        </div>
        <FooterAdmin />
      </div>
    </div>
  );
};

export default ViewManagement;
