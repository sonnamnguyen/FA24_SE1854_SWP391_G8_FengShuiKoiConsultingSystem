import React, { useState, useEffect } from "react";
import Navbar from "../layouts/header-footer/Navbar";
import Footer from "../layouts/header-footer/Footer";
import { Link } from "react-router-dom";
import { message } from "antd"; // Import message từ Ant Design

function Blog() {
    const [search, setSearchData] = useState('');

    // useEffect để kiểm tra và hiển thị thông báo nếu cần
    useEffect(() => {
        const success = localStorage.getItem('consultationSuccess');
        if (success) {
            message.success('Chi tiết yêu cầu đã được lưu thành công!', 2);
            setTimeout(() => {
                message.info('Tư vấn đã được gửi đi, vui lòng kiểm tra gmail trong 24h tới.', 2);
            }, 2000); // Thời gian giữa hai thông báo
            localStorage.removeItem('consultationSuccess'); // Xóa trạng thái sau khi hiển thị thông báo
        }
    }, []);

    return (
        <div>
            <Navbar searchData={search} setSearchData={setSearchData} />
            <div className="container">
                <div className="row mt-n5">
                    <div className="col-md-6 col-lg-4 mt-5 wow fadeInUp" data-wow-delay=".2s">
                        <div className="blog-grid">
                            <div className="blog-grid-img position-relative">
                                <img alt="img" src="https://www.bootdey.com/image/480x480/00FFFF/000000" />
                            </div>
                            <div className="blog-grid-text p-4">
                                <h3 className="h5 mb-3">
                                    <Link to="#!">Business tool for your customer</Link>
                                </h3>
                                <p className="display-30">Exercitation ullamco laboris nisi ut aliquip ex ea commodo.</p>
                                <div className="meta meta-style2">
                                    <ul>
                                        <li><Link to="#!"><i className="fas fa-calendar-alt"></i> 10 Jul, {new Date().getFullYear()}</Link></li>
                                        <li><Link to="#!"><i className="fas fa-user"></i> User</Link></li>
                                        <li><Link to="#!"><i className="fas fa-comments"></i> 38</Link></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="col-md-6 col-lg-4 mt-5 wow fadeInUp" data-wow-delay=".4s">
                        <div className="blog-grid">
                            <div className="blog-grid-img position-relative">
                                <img alt="img" src="https://www.bootdey.com/image/480x480/00FFFF/000000" />
                            </div>
                            <div className="blog-grid-text p-4">
                                <h3 className="h5 mb-3">
                                    <Link to="#!">Growth your business strategy</Link>
                                </h3>
                                <p className="display-30">Exercitation ullamco laboris nisi ut aliquip ex ea commodo.</p>
                                <div className="meta meta-style2">
                                    <ul>
                                        <li><Link to="#!"><i className="fas fa-calendar-alt"></i> 25 Jun, {new Date().getFullYear()}</Link></li>
                                        <li><Link to="#!"><i className="fas fa-user"></i> User</Link></li>
                                        <li><Link to="#!"><i className="fas fa-comments"></i> 68</Link></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                    {/* Các thẻ blog khác vẫn tương tự */}
                    <div className="col-12">
                        <div className="pagination text-small text-uppercase text-extra-dark-gray">
                            <ul>
                                <li><Link to="#!"><i className="fas fa-long-arrow-alt-left me-1 d-none d-sm-inline-block"></i> Prev</Link></li>
                                <li className="active"><Link to="#!">1</Link></li>
                                <li><Link to="#!">2</Link></li>
                                <li><Link to="#!">3</Link></li>
                                <li><Link to="#!">Next <i className="fas fa-long-arrow-alt-right ms-1 d-none d-sm-inline-block"></i></Link></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            <Footer />
        </div>
    );
}

export default Blog;
