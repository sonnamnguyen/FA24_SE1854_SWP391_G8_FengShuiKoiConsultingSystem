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
  const [newComment, setNewComment] = useState<string>("");
  const [posts, setPosts] = useState<Post[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [searchData, setSearchData] = useState<string>("");
  const [currentPage, setCurrentPage] = useState<number>(0);
  const [currentImageIndex, setCurrentImageIndex] = useState<number>(0);

  useEffect(() => {
    const fetchPosts = async () => {
      setLoading(true);
      const token = getToken();
      try {
        const url = searchData
          ? `http://localhost:9090/posts/search-posts/title?page=1&size=10&title=${searchData}`
          : "http://localhost:9090/posts?page=1&size=10";

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
  }, [searchData]); // Fetch posts whenever searchData changes

  const handleCommentSubmit = async () => {
    const token = getToken();
    if (token) {
      const decodedToken: any = jwtDecode(token);
      const userEmail = decodedToken.email || decodedToken.sub;
      try {
        const response = await axios.post(
          "http://localhost:9090/post/comments",
          {
            postId: posts[currentPage].id,
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

          setPosts((prevPosts) => {
            const updatedPosts = [...prevPosts];
            updatedPosts[currentPage].comments.push(newCommentData);
            return updatedPosts;
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
      setCurrentImageIndex((prev) => {
        if (prev === posts[currentPage].images.length - 1) {
          return 0;
        }
        return prev + 1;
      });
    }, 3000);

    return () => clearInterval(interval);
  }, [currentPage, posts]);

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
