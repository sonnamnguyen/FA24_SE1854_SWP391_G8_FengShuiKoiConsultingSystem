import React, { useEffect, useState } from "react";
import axios from "axios";
import "../css/MyPostList.css";
import Navbar from "../layouts/header-footer/Navbar";
import Footer from "../layouts/header-footer/Footer";
import { getToken } from "../service/localStorageService";
import { jwtDecode } from "jwt-decode";
import { useNavigate } from "react-router-dom"; // Import useNavigate
import { Height } from "@mui/icons-material";

interface Post {
  id: number;
  postCategory: { postCategoryName: string };
  title: String;
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

const ViewPost: React.FC = () => {
  const [newComment, setNewComment] = useState<string>("");
  const [posts, setPosts] = useState<Post[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [searchData, setSearchData] = useState<string>("");
  const [currentPage, setCurrentPage] = useState<number>(1);
  const [currentImageIndex, setCurrentImageIndex] = useState<number>(0); // State cho chỉ số ảnh hiện tại

  useEffect(() => {
    const fetchPosts = async () => {
      setLoading(true);
      const token = getToken();
      try {
        const response = await axios.get(
          `http://localhost:9090/posts/search-posts/email?page=${currentPage}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        if (
          response.data &&
          response.data.result &&
          response.data.result.data
        ) {
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
  }, [currentPage]);
  const navigate = useNavigate(); // Khai báo useNavigate

  // Hàm xóa bài viết
  const handleDeletePost = async (postId: number) => {
    const token = getToken();
    try {
      await axios.delete(`http://localhost:9090/posts/${postId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      // Sau khi xóa, cập nhật lại danh sách bài viết
      setPosts((prevPosts) => prevPosts.filter((post) => post.id !== postId));
    } catch (error) {
      console.error("Xóa bài viết thất bại", error);
      setError("Xóa bài viết thất bại");
    }
  };
  const handleUpdatePost = (postId: number) => {
    // Điều hướng sang trang UpdatePost và truyền postId qua URL
    navigate(`/update-post/${postId}`);
  };

  const totalPages = Math.ceil(posts.length / 10);
  const getFirst100Chars = (htmlContent: string): string => {
    const parser = new DOMParser();
    const doc = parser.parseFromString(htmlContent, "text/html");
    const textContent = doc.body.textContent || "";
    return (
      textContent.substring(0, 200) + (textContent.length > 200 ? "..." : "")
    );
  };

  return (
    <div>
      <Navbar searchData={searchData} setSearchData={setSearchData} />
      <div className="post-content">
        <h4 className="blogTitle"> Your Blog</h4>
        {posts.length > 0 ? (
          posts.map((post, index) => (
            <div
              className="card mb-3 cardMyPost"
              style={{ width: "96%" }}
              key={post.id}
            >
              <div className="row g-0">
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
                <div className="col-md-6">
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
                        {" "}
                        <strong>Destiny:</strong> {post.destiny.destiny}
                        <p className="card-text">
                          <small className="text-body-secondary">
                            Created on{" "}
                            {new Date(post.createdDate).toLocaleString()}
                          </small>
                        </p>
                      </div>
                    </p>

                    {/* Hiển thị ngày tạo bài viết */}
                  </div>
                </div>
                <div className="col-md-2 btnDiv">
                  <div className="btnMPL">
                    {" "}
                    <button
                      className="btnSeeMore"
                      onClick={() => handleDeletePost(post.id)}
                    >
                      <i className="bx bxs-trash"></i>
                    </button>
                  </div>
                  <div style={{ height: "60px" }}></div>

                  {/* Nút xóa bài viết */}
                  <div className="btnMPL">
                    <button
                      className="btnSeeMore"
                      onClick={() => handleUpdatePost(post.id)}
                    >
                      Update
                    </button>
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

export default ViewPost;
