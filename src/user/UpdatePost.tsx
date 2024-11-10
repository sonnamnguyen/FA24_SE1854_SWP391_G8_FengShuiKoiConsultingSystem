import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";
import { getToken } from "../service/localStorageService";
import { jwtDecode } from "jwt-decode";
import "../css/UpdatePost.css";
import Navbar from "../layouts/header-footer/Navbar";
import Footer from "../layouts/header-footer/Footer";

const UpdatePost: React.FC = () => {
  const { id } = useParams<{ id: string }>(); // Lấy id từ URL
  const navigate = useNavigate(); // Dùng để điều hướng sau khi cập nhật
  const [post, setPost] = useState<any>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  // Các state để lưu trữ giá trị form
  const [username, setUsername] = useState<string>(""); // Set username state
  const [postCategory, setPostCategory] = useState<number>(1); // Chọn category
  const [packageId, setPackageId] = useState<number>(1); // Chọn field
  const [destiny, setDestiny] = useState<number>(1); // Chọn destiny
  const [content, setContent] = useState<string>("");

  const [likeNumber, setLikeNumber] = useState<number>(0); // Mặc định là 0
  const [dislikeNumber, setDislikeNumber] = useState<number>(0); // Mặc định là 0
  const [shareNumber, setShareNumber] = useState<number>(0); // Mặc định là 0
  const [status, setStatus] = useState<string>("ACTIVE");
  const [createdDate, setCreatedDate] = useState<string>("");
  const [searchData, setSearchData] = useState<string>("");

  // Hàm chuyển HTML thành văn bản thuần
  const parseHtmlToText = (htmlContent: string) => {
    const parser = new DOMParser();
    const doc = parser.parseFromString(htmlContent, "text/html");
    return doc.body.textContent || "";
  };

  useEffect(() => {
    const fetchPost = async () => {
      if (id) {
        setLoading(true);
        const token = getToken();

        // Giải mã token và lấy username từ token
        if (token) {
          const decodedToken: any = jwtDecode(token); // Giải mã token
          const userEmail = decodedToken.email || decodedToken.sub; // Truy cập trường email
          setUsername(userEmail); // Set the username directly from the decoded token
        }

        try {
          const response = await axios.get(
            `http://localhost:9090/posts/search-posts/${id}`,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
          );

          // Lấy kết quả từ response
          const data = response.data.result;

          // Gán dữ liệu vào các state
          setPost(data);
          setPostCategory(data.postCategory?.id || 1);
          setPackageId(data.packageId?.id || 1);
          setDestiny(data.destiny?.id || 1);
          setContent(data.content ? parseHtmlToText(data.content) : ""); // Chuyển đổi HTML thành văn bản thuần
          setLikeNumber(data.likeNumber || 0);
          setDislikeNumber(data.dislikeNumber || 0);
          setShareNumber(data.shareNumber || 0);
          setStatus(data.status || "ACTIVE");
          setCreatedDate(data.createdDate || "");
        } catch (err) {
          setError("Failed to fetch post for update");
        } finally {
          setLoading(false);
        }
      }
    };

    fetchPost();
  }, [id]);

  // Hàm xử lý cập nhật bài viết
  const handleUpdatePost = async (event: React.FormEvent) => {
    event.preventDefault();

    const token = getToken();

    // Tạo đối tượng bài viết với các trường cập nhật
    const updatedPost = {
      userResponse: {
        username: username,
      },
      postCategory: {
        id: postCategory,
      },
      packageId: {
        id: packageId,
      },
      destiny: {
        id: destiny, // Truyền ID mới của Destiny vào đây
      },
      content: content, // Nội dung đã được chuyển thành văn bản thuần
      likeNumber: likeNumber,
      dislikeNumber: dislikeNumber,
      shareNumber: shareNumber,
      status: status,
      createdDate: createdDate, // Đảm bảo giá trị createdDate hợp lệ
      createdBy: username,
    };

    try {
      // Gửi yêu cầu PUT để cập nhật bài viết
      await axios.put(`http://localhost:9090/posts/${id}`, updatedPost, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      alert("Post updated successfully!");
      navigate("/"); // Điều hướng về trang danh sách bài viết hoặc trang chi tiết sau khi cập nhật
    } catch (err) {
      setError("Failed to update post");
    }
  };

  // Kiểm tra xem loading hay đã có dữ liệu
  if (loading) return <p>Loading...</p>;

  // Kiểm tra xem bài viết có tồn tại không
  return (
    <div>
      <Navbar searchData={searchData} setSearchData={setSearchData} />
      <div className="post-content-update">
        <h2 className="update-post-title">Update Post</h2>
        {error && <p className="error-message">{error}</p>}
        {post ? (
          <form onSubmit={handleUpdatePost} className="update-post-form">
            <div className="form-group">
              <label className="form-label">Username:</label>
              <input
                className="form-input"
                type="text"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                readOnly
              />
            </div>
            <div className="form-group">
              <label className="form-label">Post Category:</label>
              <select
                className="form-select"
                value={postCategory}
                onChange={(e) => setPostCategory(Number(e.target.value))}
              >
                <option value={1}>Lifestyle</option>
                <option value={2}>Experience</option>
                <option value={3}>Buy and Sell</option>
              </select>
            </div>
            <div className="form-group">
              <label className="form-label">Package Field:</label>
              <select
                className="form-select"
                value={packageId}
                onChange={(e) => setPackageId(Number(e.target.value))}
              >
                <option value={1}>Basic Package</option>
              </select>
            </div>
            <div className="form-group">
              <label className="form-label">Destiny:</label>
              <select
                className="form-select"
                value={destiny}
                onChange={(e) => setDestiny(Number(e.target.value))}
              >
                <option value={1}>Fire</option>
                <option value={2}>Water</option>
                <option value={3}>Earth</option>
                <option value={4}>Wood</option>
                <option value={5}>Metal</option>
              </select>
            </div>
            <div className="form-group">
              <label className="form-label">Content:</label>
              <textarea
                className="form-textarea"
                value={content}
                onChange={(e) => setContent(e.target.value)}
              />
            </div>
            <div className="form-group">
              <label className="form-label">Created Date:</label>
              <input
                className="form-input"
                type="datetime-local"
                value={
                  createdDate
                    ? new Date(createdDate).toISOString().slice(0, 16)
                    : ""
                }
                disabled
              />
            </div>
            <button type="submit" className="btnSeeMore">
              Update Post
            </button>
          </form>
        ) : (
          <p>Post not found</p>
        )}
      </div>
      <Footer></Footer>
    </div>
  );
};

export default UpdatePost;
