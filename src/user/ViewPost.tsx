import React, { useEffect, useState } from "react";
import axios from "axios";
import "../css/ViewPost.css";
import Navbar from "../layouts/header-footer/Navbar";
import Footer from "../layouts/header-footer/Footer";
import { getToken } from "../service/localStorageService";
import { jwtDecode } from "jwt-decode";
import { useNavigate, useParams } from "react-router-dom";

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

const ViewPost: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [newComment, setNewComment] = useState<string>("");
  const [post, setPost] = useState<Post | null>(null); // Chỉ sử dụng một bài viết duy nhất
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [currentImageIndex, setCurrentImageIndex] = useState<number>(0);
  const [searchData, setSearchData] = useState<string>("");

  useEffect(() => {
    const fetchPost = async () => {
      setLoading(true);
      const token = getToken();
      try {
        const response = await axios.get(
          `http://localhost:9090/posts/search-posts/${id}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        if (response.data && response.data.result) {
          setPost(response.data.result); // Lưu bài viết duy nhất
        } else {
          setError("Bài viết không tồn tại");
        }
      } catch (err) {
        setError("Lỗi khi lấy dữ liệu");
      } finally {
        setLoading(false);
      }
    };

    fetchPost();
  }, [id]);

  const handleCommentSubmit = async () => {
    const token = getToken();
    if (token && post) {
      const decodedToken: any = jwtDecode(token);
      const userEmail = decodedToken.email || decodedToken.sub;
      try {
        const response = await axios.post(
          "http://localhost:9090/post/comments",
          {
            postId: post.id,
            content: newComment,
            status: "ACTIVE",
            createdBy: userEmail,
            createdDate: new Date().toISOString(),
          },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        if (response.data) {
          const newCommentData = {
            id: response.data.id,
            content: newComment,
            createdBy: userEmail,
            createdDate: new Date().toISOString(),
          };

          setPost((prevPost) => {
            if (prevPost) {
              const updatedPost = { ...prevPost };
              updatedPost.comments.push(newCommentData);
              return updatedPost;
            }
            return prevPost;
          });
          setNewComment("");
        }
      } catch (error) {
        console.error("Gửi bình luận thất bại", error);
        setError("Gửi bình luận thất bại");
      }
    }
  };

  useEffect(() => {
    const interval = setInterval(() => {
      if (post && post.images.length > 0) {
        setCurrentImageIndex((prev) => {
          if (prev === post.images.length - 1) {
            return 0;
          }
          return prev + 1;
        });
      }
    }, 3000);

    return () => clearInterval(interval);
  }, [post]);

  if (loading) return <p>Loading...</p>;
  if (error) return <p>{error}</p>;

  return (
    <div>
      <Navbar searchData={searchData} setSearchData={setSearchData} />
      <div className="view-post-content">
        {post ? (
          <div className="post">
            <h1 className="title">{post.title}</h1>

            {/* Display HTML content using dangerouslySetInnerHTML */}
            <div
              className="contentPost"
              dangerouslySetInnerHTML={{ __html: post.content }}
            />

            <div className="slide">
              <div className="images">
                <div
                  className="image-container"
                  style={{
                    transform: `translateX(-${currentImageIndex * 100}%)`,
                  }}
                >
                  {post.images.map((image) => (
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
                <span className="author-name">{post.destiny.destiny}</span>
              </p>
              <p className="post-category">
                Post Category:{" "}
                <span className="author-name">
                  {post.postCategory.postCategoryName}
                </span>
              </p>
              <p className="post-author">
                Created By:{" "}
                <span className="author-name">{post.createdBy}</span> on{" "}
                <span className="created-date">
                  {new Date(post.createdDate).toLocaleString()}
                </span>
              </p>
            </div>

            <div className="comments">
              <h3 className="comments-title">Your Comment</h3>
              {post.comments.length > 0 ? (
                post.comments.map((comment) => (
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
          </div>
        ) : (
          <p>No post available.</p>
        )}
      </div>
      <Footer />
    </div>
  );
};

export default ViewPost;
