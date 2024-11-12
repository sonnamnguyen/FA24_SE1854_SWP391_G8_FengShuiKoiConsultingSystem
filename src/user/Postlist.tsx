import React, { useEffect, useState } from "react";
import axios from "axios";
import "../css/MyPostList.css";
import Navbar from "../layouts/header-footer/Navbar";
import Footer from "../layouts/header-footer/Footer";
import { getToken } from "../service/localStorageService";
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
  const [posts, setPosts] = useState<Post[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [searchData, setSearchData] = useState<string>(""); // Search data for search functionality
  const [currentPage, setCurrentPage] = useState<number>(1); // Current page
  const [totalPages, setTotalPages] = useState<number>(0); // Total number of pages
  const [pageSize, setPageSize] = useState<number>(10); // Number of posts per page
  const navigate = useNavigate(); // useNavigate to navigate to post detail

  // Fetch posts based on search and pagination
  useEffect(() => {
    const fetchPosts = async () => {
      setLoading(true);
      const token = getToken();
      try {
        // Define the API URL with pagination parameters (page and size)
        const url = searchData
          ? `http://localhost:9090/posts/search-posts/title?title=${searchData}&page=${currentPage}&size=${pageSize}`
          : `http://localhost:9090/posts?page=${currentPage}&size=${pageSize}`;

        const response = await axios.get(url, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.data && response.data.result) {
          console.log("API Response:", response.data);
          setPosts(response.data.result.data); // Set posts data
          setTotalPages(response.data.result.totalPages); // Set total pages from response
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
  }, [searchData, currentPage]); // Fetch posts whenever searchData or currentPage changes

  const getFirst100Chars = (htmlContent: string): string => {
    const parser = new DOMParser();
    const doc = parser.parseFromString(htmlContent, "text/html");
    const textContent = doc.body.textContent || "";
    return (
      textContent.substring(0, 200) + (textContent.length > 200 ? "..." : "")
    );
  };

  // Handle "See More" button click to navigate to post detail
  const handleSeeMore = (postId: number) => {
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
              <div className="row g-0">
                {/* Display first image */}
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
                    {/* Display post title */}
                    <h5 className="card-title">{post.title}</h5>

                    {/* Display first 100 characters of content */}
                    <p className="card-text">
                      {getFirst100Chars(post.content)}
                    </p>

                    {/* Display Destiny */}
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
                        onClick={() => handleSeeMore(post.id)}
                      >
                        See more
                      </button>
                    </p>

                    {/* Display post creation date */}
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
              setCurrentPage((prev) => Math.min(prev + 1, totalPages))
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