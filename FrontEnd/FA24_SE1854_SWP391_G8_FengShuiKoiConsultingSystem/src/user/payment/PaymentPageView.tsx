import React, { useState} from "react";
import PaymentPage from "./PaymentPage";
import Navbar from "../../layouts/header-footer/Navbar";
import Footer from "../../layouts/header-footer/Footer";
const ViewAdd: React.FC = () => {
  const [search, setSearchData] = useState(""); 
  return (
        <div>
          <Navbar searchData={search} setSearchData={setSearchData} />
          <PaymentPage />
          <Footer />
        </div>
  );
};

export default ViewAdd;