import React, { useState, useEffect } from "react";
import Navbar from "../layouts/header-footer/Navbar";
import Footer from "../layouts/header-footer/Footer";
import "../css/HomePage.css";
import "boxicons/css/boxicons.min.css";
import { Margin } from "@mui/icons-material";
import { getToken } from "../service/localStorageService";
import { useNavigate } from "react-router-dom";
import { message } from "antd";

function Blog() {
  const [search, setSearchData] = useState("");
  const navigate = useNavigate(); // Khởi tạo navigate

  // useEffect để kiểm tra và hiển thị thông báo nếu cần
  useEffect(() => {
    const success = localStorage.getItem("consultationSuccess");
    if (success) {
      message.success("Request details have been saved successfully!", 2);
      setTimeout(() => {
        message.info(
          "Consultation has been sent, please check your email within the next 24 hours.",
          4
        );
      }, 2000); // Thời gian giữa hai thông báo
      localStorage.removeItem("consultationSuccess"); // Xóa trạng thái sau khi hiển thị thông báo
    }
  }, []);

  const handleSeeMore = () => {
    const token = getToken(); // Lấy token từ LocalStorage hoặc Cookie
    if (token) {
      // Điều hướng tới trang ViewPost nếu token hợp lệ
      navigate("/post-list");
    } else {
      // Nếu không có token, có thể redirect về trang login hoặc hiển thị thông báo
      navigate("/login");
    }
  };

  useEffect(() => {
    // Tạo observer chỉ một lần khi component mount
    const observer = new IntersectionObserver(
      (entries, observer) => {
        entries.forEach((entry) => {
          // Kiểm tra nếu phần tử vào viewport (isIntersecting = true)
          if (entry.isIntersecting) {
            entry.target.classList.add("visible"); // Thêm class 'visible' để kích hoạt hiệu ứng CSS
          } else {
            entry.target.classList.remove("visible"); // Xóa class 'visible' khi phần tử không còn trong viewport
          }
        });
      },
      { threshold: 0.3 }
    ); // Kích hoạt khi phần tử ít nhất 50% xuất hiện trong viewport

    // Lấy tất cả phần tử có class 'section-fade' và áp dụng observer cho chúng
    const sections = document.querySelectorAll(".section-fade");
    sections.forEach((section) => {
      observer.observe(section); // Bắt đầu theo dõi mỗi phần tử
    });

    // Cleanup observer khi component unmount
    return () => {
      sections.forEach((section) => {
        observer.unobserve(section); // Dừng theo dõi khi component unmount
      });
    };
  }, []); // Chỉ chạy một lần khi component được mount, giống như componentDidMount

  return (
    <div>
      <Navbar searchData={search} setSearchData={setSearchData} />
      <main className="main-content">
        <div className="StartContain">
          <section className="hero">
            <div className="blStart"></div>
            <div className="Start">
              <h4>Welcome to FengShui Koi System</h4>
              <p>
                Discover the beauty and serenity of Feng Shui with our Koi
                System.
              </p>
              <button onClick={handleSeeMore} className="btnSeeMore">
                Get Started
              </button>
            </div>
          </section>
        </div>
      </main>

      {/* Các phần tử có hiệu ứng cuộn */}
      <section className="BodyContain - container section-fade">
        <div className="row">
          <h1 className="Bodytitle">FengShuiKoi System</h1>
          <div className="col - contentBody">
            <h4>
              Scoring <i className="bx bxs-badge-check"></i>
            </h4>
            <p>
              fengshui koi will help you score whether your koi fish is suitable
              for your destiny or not, giving you the most suitable score to
              help you have a more accurate view.
            </p>
          </div>
          <div className="col - contentBody">
            <h4>
              Advise <i className="bx bxs-conversation"></i>
            </h4>
            <p>
              FengShui Koi will help you give advice on fish breeds and types of
              aquariums that best suit your destiny through the information you
              provide.
            </p>
          </div>
          <div className="col - contentBody">
            <h4>
              Blog <i className="bx bxl-blogger"></i>
            </h4>
            <p>
              Through Blog you can share your knowledge, experience and
              experience, in addition you can post sales news and many other
              functions.
            </p>
          </div>
        </div>
      </section>

      <section className="EndContain - container section-fade">
        <h4 className="Endtitle">
          FENGSHUI KOI WILL HELP YOU FIND THE SUITABLE KOI FISH MODEL
        </h4>
        <div className="EndBody">
          <img
            src="https://koiservice.vn/wp-content/uploads/2023/06/352830169_726370176165213_2004750268232881781_n.jpg"
            alt="Koi Image"
            className="col-md-4"
          />
          <p className="col-md-8">
            Fengshui Koi is dedicated to helping you find the perfect koi fish
            model that enhances the beauty and harmony of your outdoor space.
            Our knowledgeable team provides expert guidance on selecting from a
            diverse array of high-quality koi varieties, each carefully sourced
            to ensure vibrancy and health. We understand the significance of koi
            in feng shui, symbolizing prosperity and good fortune, and we’ll
            assist you in choosing fish that align with your aesthetic
            preferences and energy goals. With personalized support and valuable
            resources, we’re here to ensure your koi thrive in a stunning pond
            that reflects your unique style and brings joy to your home.
            Discover the enchanting world of koi fish with Fengshui Koi today!
          </p>
        </div>
      </section>

      <section className="BodyContain - container section-fade">
        <h4 className="Endtitle">DIVERSE KOI fISH POND</h4>
        <div className="card-group">
          <div className="card">
            <img
              src="https://chohanghoa.com.vn/wp-content/uploads/2022/05/ho-ca-koi-dep-2.jpg"
              className="card-img-top"
              alt="Koi pond"
            />
            <div className="card-body">
              <h5 className="card-title">Koi fish pond landscape garden</h5>
              <p className="card-text">
                A koi fish pond landscape garden can be a stunning centerpiece
                in your yard, providing tranquility and beauty. By thoughtfully
                designing each aspect, you can create a harmonious space that
                showcases the elegance of koi while offering a peaceful retreat
                for you and your guests.
              </p>
            </div>
          </div>
          <div className="card">
            <img
              src="https://chohanghoa.com.vn/wp-content/uploads/2022/05/ho-ca-koi-dep-3.jpg"
              className="card-img-top"
              alt="Villa koi pond"
            />
            <div className="card-body">
              <h5 className="card-title">
                Beautiful koi fish pond model for a villa
              </h5>
              <p className="card-text">
                A well-designed koi fish pond can enhance the elegance and charm
                of a villa, creating a peaceful oasis that complements the
                overall landscape. By thoughtfully incorporating these elements,
                you can create a stunning koi pond that will be a source of joy
                and relaxation for years to come.
              </p>
            </div>
          </div>
          <div className="card">
            <img
              src="https://chohanghoa.com.vn/wp-content/uploads/2022/05/ho-ca-koi-dep-1.jpg"
              className="card-img-top"
              alt="Landscape koi pond"
            />
            <div className="card-body">
              <h5 className="card-title">Beautiful landscape koi pond model</h5>
              <p className="card-text">
                A well-designed landscape koi pond model can be a stunning
                addition to any outdoor space, offering beauty, tranquility, and
                a harmonious connection with nature. By thoughtfully
                incorporating these elements, you can create an enchanting koi
                pond that will be a source of joy for years to come.
              </p>
            </div>
          </div>
        </div>
      </section>
      <section className="BodyContain - container section-fade">
        <div className="row">
          <h4 className="Endtitle">CREATE YOUR PERSONAL BLOG</h4>
          <div
            className="card mb-3 blog"
            style={{ maxWidth: "90%", marginLeft: "5%" }}
          >
            <div className="row g-0">
              <div className="col-md-4">
                <img
                  src="https://i.pinimg.com/1200x/a3/77/b9/a377b93705b963055384cdb6c3f36354.jpg"
                  className="img-fluid rounded-start"
                  alt="Card image"
                />
              </div>
              <div className="col-md-8">
                <div className="card-body">
                  <h5 className="card-title">Experience in raising koi fish</h5>
                  <p className="card-text">
                    Raising koi fish is a rewarding experience that lets you
                    enjoy their beauty and serenity. It requires commitment to
                    maintaining water quality, proper feeding, and monitoring
                    their health.
                  </p>
                  <div className="blspan">
                    <p className="card-text">
                      <small className="text-muted">
                        More than 10 relative blogs
                      </small>
                    </p>
                    <button onClick={handleSeeMore}>See more</button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div
          className="card mb-3 blog"
          style={{ maxWidth: "90%", marginLeft: "5%" }}
        >
          <div className="row g-0">
            <div className="col-md-4">
              <img
                src="https://i.pinimg.com/1200x/75/4f/18/754f188005ac24204fc8b2c4efead441.jpg"
                className="img-fluid rounded-start"
                alt="Card image"
              />
            </div>
            <div className="col-md-8">
              <div className="card-body">
                <h5 className="card-title">Buy and sell Koi fish</h5>
                <p className="card-text">
                  Buying and selling koi fish is a popular aspect of the koi
                  hobby. When purchasing koi, it's important to choose healthy
                  fish with vibrant colors and strong genetics. Sellers
                  typically offer a range of koi varieties, from young fish to
                  mature ones, each with unique patterns.
                </p>
                <div className="blspan">
                  <p className="card-text">
                    <small className="text-muted">
                      More than 10 relative blogs
                    </small>
                  </p>
                  <button onClick={handleSeeMore}>See more</button>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div
          className="card mb-3 blog"
          style={{ maxWidth: "90%", marginLeft: "5%" }}
        >
          <div className="row g-0">
            <div className="col-md-4">
              <img
                src="https://i.pinimg.com/1200x/18/b3/41/18b3417234e6a2be944a2dbdbb69c60b.jpg"
                className="img-fluid rounded-start"
                alt="Card image"
              />
            </div>
            <div className="col-md-8">
              <div className="card-body">
                <h5 className="card-title">Koi Fish Art</h5>
                <p className="card-text">
                  Koi fish art is a beautiful and symbolic form of expression
                  that captures the elegance and grace of these beloved
                  creatures. Often seen in traditional Japanese art, koi fish
                  represent various positive traits, including perseverance,
                  strength, and good fortune.
                </p>
                <div className="blspan">
                  <p className="card-text">
                    <small className="text-muted">
                      More than 10 relative blogs
                    </small>
                  </p>
                  <button onClick={handleSeeMore}>See more</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <Footer />
    </div>
  );
}

export default Blog;
