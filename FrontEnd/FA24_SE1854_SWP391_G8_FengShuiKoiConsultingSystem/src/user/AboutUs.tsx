// src/pages/AboutUs.tsx
import React, { useState, useEffect } from "react";
import Navbar from "../layouts/header-footer/Navbar";
import Footer from "../layouts/header-footer/Footer";
import "../css/AboutUs.css"; // Nếu bạn có các file CSS riêng cho trang này

const AboutUs: React.FC = () => {
  const [search, setSearchData] = useState("");
  return (
    <div className="about-us-all">
      <Navbar searchData={search} setSearchData={setSearchData} />
      <div className="about-us-container">
        <section className="about-us-content">
          <h1 className="title"> About Us</h1>

          <h1 className="subtitle">Introduce</h1>
          <p>
            Welcome to Koi Feng Shui, your ultimate destination for blending
            ancient wisdom with modern living. Our platform is dedicated to
            helping you harness the power of Feng Shui to enhance your life,
            bringing balance, prosperity, and positive energy into your home and
            workspace. Specializing in the mystical world of Koi fish, we
            believe that these beautiful creatures are more than just symbols of
            tranquility—they are powerful agents of good fortune and harmony.
          </p>
          <img
            src="https://i.pinimg.com/1200x/fa/04/0f/fa040ffadd6c6d9de339856ed75e16f2.jpg"
            alt="Koi Fish"
            className="about-us-img"
          />

          <h1 className="subtitle">Development History</h1>
          <p>
            Koi Feng Shui was founded with a simple yet powerful vision: to
            bring the ancient wisdom of Feng Shui and the calming influence of
            Koi fish into the modern world, helping people transform their
            living and working environments for the better. What started as a
            small idea has now grown into a trusted online platform offering
            valuable resources, products, and guidance in the realm of Feng Shui
            and holistic living.
          </p>
          <p>
            The journey began several years ago, when a group of passionate Feng
            Shui practitioners and enthusiasts realized that many people were
            unaware of how deeply Feng Shui could affect their daily lives. As
            the team delved deeper into the art of Feng Shui, they also
            discovered the special significance of Koi fish in this practice.
            Koi fish are symbols of prosperity, luck, and positive energy, and
            they quickly became a focal point of our philosophy.
          </p>
          <p>
            In its early stages, Koi Feng Shui was simply a blog where our
            founders shared articles and tips on applying Feng Shui principles
            to different aspects of life. Over time, the demand for more
            in-depth content and specialized products grew, leading to the
            development of our full-featured website. The site now offers
            everything from Feng Shui guides to exclusive Koi fish-themed
            products, carefully curated to help our customers bring peace,
            harmony, and prosperity into their homes.
          </p>

          <h1 className="subtitle">Future Vision</h1>
          <p>
            At Koi Feng Shui, we are passionate about the profound impact that
            Feng Shui and Koi fish can have on one's life. As we look to the
            future, our vision is to expand our reach and deepen our influence,
            helping more people around the world harness the power of these
            ancient practices to create peaceful, balanced, and prosperous
            lives.
          </p>

          <h1 className="subtitle">Core Values</h1>
          <p>
            At Koi Feng Shui, our core values are the foundation of everything
            we do. These values guide our work, shape our interactions with our
            customers, and define the way we approach Feng Shui and Koi fish
            practices. They reflect our commitment to creating a positive impact
            on the lives of our community and the world at large. Below are the
            core values that we hold dear
          </p>
          <img
            src="https://tiki.vn/blog/wp-content/uploads/2023/12/mau-phong-thuy-thumb-1024x576.png"
            alt="Koi Fish"
            className="about-us-img"
          />
          <h1 className="subtitle">Core Values</h1>
          <p>
            At Koi Feng Shui, our core values are the foundation of everything
            we do. These values guide our work, shape our interactions with our
            customers, and define the way we approach Feng Shui and Koi fish
            practices. They reflect our commitment to creating a positive impact
            on the lives of our community and the world at large. Below are the
            core values that we hold dear
          </p>
          <h1 className="subtitle">Why Choose Us?</h1>
          <p>
            At Koi Feng Shui, our core values are the foundation of everything
            we do. These values guide our work, shape our interactions with our
            customers, and define the way we approach Feng Shui and Koi fish
            practices. They reflect our commitment to creating a positive impact
            on the lives of our community and the world at large. Below are the
            core values that we hold dear
          </p>
        </section>
      </div>
      <Footer />
    </div>
  );
};

export default AboutUs;
