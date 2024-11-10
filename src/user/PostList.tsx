import React, { useEffect, useState } from "react";
import axios from "axios";
import "../css/MyPostList.css";
import Navbar from "../layouts/header-footer/Navbar";
import Footer from "../layouts/header-footer/Footer";
import { getToken } from "../service/localStorageService";
import { jwtDecode } from "jwt-decode";
import { useNavigate } from "react-router-dom"; // Import useNavigate

interface Post {
  id: number;
  postCategory: { postCategoryName: string };
  title: string;
  images: { id: number; imageUrl: string }[];
  content: string;
  destiny: { destiny: string };
  likeNumber: number;
  dislikeNumber: number;
  createdBy: string;
  createdDate: string;
  comments: {
    id: number;
    content: string;
    createdBy: string;
    createdDate: string;
  }[];
}

const MyPostList: React.FC = () => {
  const [newComment, setNewComment] = useState<string>("");
  const [posts, setPosts] = useState<Post[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [searchData, setSearchData] = useState<string>("");
  const [currentPage, setCurrentPage] = useState<number>(1);
  const [currentImageIndex, setCurrentImageIndex] = useState<number>(0);

  const navigate = useNavigate(); // Khai báo useNavigate
  useEffect(() => {
    const fetchPosts = async () => {
      setLoading(true);
      const token = getToken();
      try {
        const url = searchData
          ? `http://localhost:9090/posts/search-posts/title?title=${searchData}`
          : `http://localhost:9090/posts?page=${currentPage}`;

        const response = await axios.get(url, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (
          response.data &&
          response.data.result &&
          response.data.result.data
        ) {
          console.log("API Response:", response.data);
          setPosts(response.data.result.data);
        } else {
          setError("Invalid data structure from API");
        }
      } catch (err) {
        setError("Failed to fetch data");
      } finally {
        setLoading(false);
      }
    };

    fetchPosts();
  }, [searchData, currentPage]); // Fetch posts whenever searchData changes

  const totalPages = Math.ceil(posts.length / 10);

  const getFirst100Chars = (htmlContent: string): string => {
    const parser = new DOMParser();
    const doc = parser.parseFromString(htmlContent, "text/html");
    const textContent = doc.body.textContent || "";
    return (
      textContent.substring(0, 200) + (textContent.length > 200 ? "..." : "")
    );
  };

  // Hàm xử lý sự kiện khi nhấn nút See More
  const handleSeeMore = (postId: number) => {
    // Điều hướng tới trang ViewPost với id của bài viết
    navigate(`/posts/${postId}`);
  };

  return (
    <div>
      <Navbar searchData={searchData} setSearchData={setSearchData} />
      <div className="post-content">
        <h4 className="blogTitle"> Blog</h4>
        {posts.length > 0 ? (
          posts.map((post) => (
            <div
              className="card mb-3 cardMyPost"
              style={{ width: "95%" }}
              key={post.id}
            >
              <div className="row g-0 ">
                {/* Hiển thị ảnh đầu tiên */}
                <div className="col-md-4">
                  {post.images.length > 0 && (
                    <img
                      src={post.images[0].imageUrl}
                      className="img-fluid rounded-start"
                      alt="Post image"
                    />
                  )}
                </div>
                <div className="col-md-8">
                  <div className="card-body">
                    {/* Hiển thị tiêu đề bài viết */}
                    <h5 className="card-title">{post.title}</h5>

                    {/* Hiển thị 100 ký tự đầu của content */}
                    <p className="card-text">
                      {getFirst100Chars(post.content)}
                    </p>

                    {/* Hiển thị Destiny */}
                    <p className="card-text">
                      <div>
                        <strong>Destiny:</strong> {post.destiny.destiny}
                        <p className="card-text">
                          <small className="text-body-secondary">
                            Created on{" "}
                            {new Date(post.createdDate).toLocaleString()}
                          </small>
                        </p>
                      </div>
                      <button
                        className="btnSeeMore"
                        onClick={() => handleSeeMore(post.id)} // Sử dụng hàm điều hướng
                      >
                        See more
                      </button>
                    </p>

                    {/* Hiển thị ngày tạo bài viết */}
                  </div>
                </div>
              </div>
            </div>
          ))
        ) : (
          <p>No posts available.</p>
        )}

        {/* Pagination */}
        <div className="pagination">
          <button
            onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 1))}
            disabled={currentPage === 1}
          >
            Previous
          </button>
          <span>
            Page {currentPage} of {totalPages}
          </span>
          <button
            onClick={() =>
              setCurrentPage((prev) => Math.min(prev + 1, totalPages - 1))
            }
            disabled={currentPage === totalPages}
          >
            Next
          </button>
        </div>
      </div>

      <Footer />
    </div>
  );
};

export default MyPostList;