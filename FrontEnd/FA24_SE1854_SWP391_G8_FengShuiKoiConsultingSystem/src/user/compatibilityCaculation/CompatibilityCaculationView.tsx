
import React, { useState} from "react";
import CompatibilityForm from "./CompatibilityCaculation";
import Navbar from "../../layouts/header-footer/Navbar";
import Footer from "../../layouts/header-footer/Footer";

const ViewAdd: React.FC = () => {
  const [search, setSearchData] = useState("");
  return (
        <div className="fengShui-bg">
          <Navbar searchData={search} setSearchData={setSearchData} />
         <CompatibilityForm/>
         <Footer />
        </div>
  );
};

export default ViewAdd;