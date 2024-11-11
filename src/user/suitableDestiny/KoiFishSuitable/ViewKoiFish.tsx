import React, { useState, useEffect } from "react";
import Navbar from "../../../layouts/header-footer/Navbar";
import KoiFish from "./KoiFish";
import Footer from "../../../layouts/header-footer/Footer";

const ViewKoiFish: React.FC = () => {
  const [search, setSearch] = useState<string>(""); // State cho search data

  return (
    <div>
      <Navbar searchData={search} setSearchData={setSearch} />
      <KoiFish />
      <Footer />
    </div>
  );
};

export default ViewKoiFish;
