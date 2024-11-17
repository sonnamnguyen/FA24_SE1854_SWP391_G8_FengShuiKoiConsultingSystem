import React, { useState, useEffect } from "react";
import Navbar from "../../../layouts/header-footer/Navbar";
import Footer from "../../../layouts/header-footer/Footer";
import KoiPond from "./KoiPond";

const ViewKoiPond: React.FC = () => {
  const [search, setSearch] = useState<string>(""); // State cho search data

  return (
    <div>
      <Navbar searchData={search} setSearchData={setSearch} />
      <KoiPond />
      <Footer />
    </div>
  );
};

export default ViewKoiPond;
