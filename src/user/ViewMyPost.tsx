import React, { useEffect, useState } from "react";
import axios from "axios";
import "../css/ViewPost.css";
import Navbar from "../layouts/header-footer/Navbar";
import Footer from "../layouts/header-footer/Footer";
import { getToken } from "../service/localStorageService";
import { jwtDecode } from "jwt-decode";

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
  const [currentPage, setCurrentPage] = useState<number>(0);
  const [currentImageIndex, setCurrentImageIndex] = useState<number>(0); // State cho chỉ số ảnh hiện tại

  useEffect(() => {
    const fetchPosts = async () => {
      setLoading(true);
      const token = getToken();
      try {
        const response = await axios.get(
          "http://localhost:9090/posts/search-posts/email?page=1&size=10",
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
  }, []);
  const handleCommentSubmit = async () => {
    const token = getToken();
    if (token) {
      const decodedToken: any = jwtDecode(token); // Giải mã token
      const userEmail = decodedToken.email || decodedToken.sub; // Truy cập trường email
      try {
        const response = await axios.post(
          "http://localhost:9090/posts/search-posts/email?page=1&size=10",
          {
            postId: posts[currentPage].id,
            content: newComment,
            status: "ACTIVE",
            createdBy: userEmail, // Nên lấy thông tin từ người dùng đã đăng nhập
            createdDate: new Date().toISOString(),
          },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        // Giả định rằng response.data sẽ chứa bình luận vừa gửi
        if (response.data) {
          const newCommentData = {
            id: response.data.id, // Giả định API trả về id của bình luận mới
            content: newComment,
            createdBy: userEmail, // Nên lấy thông tin từ người dùng đã đăng nhập
            createdDate: new Date().toISOString(), // Thời gian tạo bình luận
          };

          // Cập nhật danh sách bình luận
          setPosts((prevPosts) => {
            const updatedPosts = [...prevPosts];
            updatedPosts[currentPage].comments.push(newCommentData); // Thêm bình luận mới vào mảng comments
            return updatedPosts;
          });
          setNewComment(""); // Xóa nội dung ô nhập
        }
      } catch (error) {
        console.error("Gửi bình luận thất bại", error);
        setError("Gửi bình luận thất bại");
      }
    }
  };

  // Tạo hiệu ứng tự động trượt ảnh
  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentImageIndex((prev) => {
        if (prev === posts[currentPage].images.length - 1) {
          return 0; // Quay lại ảnh đầu tiên nếu đã đến ảnh cuối
        }
        return prev + 1; // Chuyển sang ảnh tiếp theo
      });
    }, 3000); // 3000 ms = 3 giây

    return () => clearInterval(interval); // Hủy interval khi component bị hủy
  }, [currentPage, posts]); // Chạy lại interval khi currentPage hoặc posts thay đổi

  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error: {error}</p>;

  const totalPages = posts.length;

  return (
    <div>
      <Navbar searchData={searchData} setSearchData={setSearchData} />
      <div className="view-post-content">
        {totalPages > 0 ? (
          <div className="post">
            <h1 className="title">{posts[currentPage].title}</h1>

            {/* Display HTML content using dangerouslySetInnerHTML */}
            <div
              className="contentPost"
              dangerouslySetInnerHTML={{ __html: posts[currentPage].content }}
            />

            <div className="slide">
              <div className="images">
                <div
                  className="image-container"
                  style={{
                    transform: `translateX(-${currentImageIndex * 100}%)`,
                  }}
                >
                  {posts[currentPage].images.map((image) => (
                    <img
                      key={image.id}
                      src={image.imageUrl}
                      alt={`Post image ${image.id}`}
                      className="post-image"
                    />
                  ))}
                </div>
              </div>
            </div>
            <div className="post-container">
              <p className="post-destiny">
                Post Destiny:{" "}
                <span className="author-name">
                  {posts[currentPage].destiny.destiny}
                </span>{" "}
              </p>
              <p className="post-category">
                Post Category:{" "}
                <span className="author-name">
                  {posts[currentPage].postCategory.postCategoryName}
                </span>
              </p>
              <p className="post-author">
                Created By:{" "}
                <span className="author-name">
                  {posts[currentPage].createdBy}
                </span>{" "}
                on{" "}
                <span className="created-date">
                  {new Date(posts[currentPage].createdDate).toLocaleString()}
                </span>
              </p>
            </div>
            <div className="comments">
              <h3 className="comments-title">Your Comment</h3>
              {posts[currentPage].comments.length > 0 ? (
                posts[currentPage].comments.map((comment) => (
                  <div key={comment.id} className="comment-item">
                    <p className="comment-content">{comment.content}</p>
                    <small className="comment-meta">
                      By <strong>{comment.createdBy}</strong> At{" "}
                      {new Date(comment.createdDate).toLocaleString()}
                    </small>
                  </div>
                ))
              ) : (
                <p className="no-comments">No Comment Yet.</p>
              )}

              <div className="comment-form">
                <textarea
                  value={newComment}
                  onChange={(e) => setNewComment(e.target.value)}
                  placeholder="Your comment here..."
                  className="comment-input"
                />
                <button onClick={handleCommentSubmit} className="submit-button">
                  Send Your Comment
                </button>
              </div>
            </div>

            <div className="pagination">
              <button
                onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 0))}
                disabled={currentPage === 0}
              >
                Previous
              </button>

              <span>
                Page {currentPage + 1} of {totalPages}
              </span>

              <button
                onClick={() =>
                  setCurrentPage((prev) => Math.min(prev + 1, totalPages - 1))
                }
                disabled={currentPage === totalPages - 1}
              >
                Next
              </button>
            </div>
          </div>
        ) : (
          <p>No posts available.</p>
        )}
      </div>
      <Footer />
    </div>
  );
};

export default ViewPost;
