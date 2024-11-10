import React, { useEffect, useState } from "react";
import axios from "axios";
import { useForm } from "react-hook-form";
import "../css/CreatePost.css";
import { getToken } from "../service/localStorageService";
import { jwtDecode } from "jwt-decode";
import Navbar from "../layouts/header-footer/Navbar";
import Footer from "../layouts/header-footer/Footer";
import { ref, uploadString, getDownloadURL } from "firebase/storage";
import { storage } from "../firebase/firebase";
import { CKEditor } from "@ckeditor/ckeditor5-react"; // Import CKEditor
import ClassicEditor from "@ckeditor/ckeditor5-build-classic"; // CKEditor build

interface PostData {
  email: string;
  postCategoryId: number;
  packageIdId: number;
  title: string;
  content: string;
  status: string;
  createdBy: string;
  images: { imageUrl: string }[];
  destinyId: number | null;
}

const CreatePost: React.FC = () => {
  const { register, handleSubmit, setValue } = useForm<PostData>();
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [searchData, setSearchData] = useState<string>("");
  const [imageFiles, setImageFiles] = useState<File[]>([]);
  const [previewImages, setPreviewImages] = useState<string[]>([]);
  const [selectedDestiny, setSelectedDestiny] = useState<number | null>(null);
  const [selectedPostCategory, setSelectedPostCategory] = useState<
    number | null
  >(null);
  const [selectedpackageId, setSelectedpackageId] = useState<number | null>(
    null
  );
  const [content, setContent] = useState<string>(""); // State for CKEditor content

  useEffect(() => {
    const token = getToken();
    if (token) {
      const decodedToken: any = jwtDecode(token);
      const userEmail = decodedToken.email || decodedToken.sub;
      setValue("email", userEmail);
    }
  }, [setValue]);

  const onSubmit = async (data: PostData) => {
    const token = getToken();

    const images = await Promise.all(
      imageFiles.map(async (file) => {
        const url = URL.createObjectURL(file);
        return { imageUrl: url };
      })
    );

    const uploadImagesToFirebase = async (files: File[]): Promise<string[]> => {
      const uploadPromises = files.map(async (file) => {
        const storageRef = ref(storage, `post_images/${file.name}`);
        const base64Image = await getBase64(file);
        await uploadString(storageRef, base64Image, "data_url");
        const downloadURL = await getDownloadURL(storageRef);
        return downloadURL;
      });

      return Promise.all(uploadPromises);
    };

    const getBase64 = (file: File): Promise<string> => {
      return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result as string);
        reader.onerror = (error) => reject(error);
      });
    };

    const base64Avatars =
      imageFiles.length > 0 ? await uploadImagesToFirebase(imageFiles) : [];

    try {
      const response = await axios.post(
        "http://localhost:9090/posts",
        {
          userResponse: { email: data.email },
          postCategory: { id: selectedPostCategory },
          packageId: { id: selectedpackageId },
          destiny: { id: selectedDestiny },
          content: content, // Use content from CKEditor
          status: data.status,
          title: data.title,
          createdDate: new Date().toISOString(),
          createdBy: data.createdBy,
          images: base64Avatars.map((url) => ({ imageUrl: url })),
          comments: [],
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      setSuccess("Post created successfully!");
      console.log(response.data);
    } catch (err) {
      setError("Error creating post: " + (err as Error).message);
    }
  };

  const handleImageChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.files) {
      const filesArray = Array.from(event.target.files);
      setImageFiles((prevFiles) => [...prevFiles, ...filesArray]);
      const imageUrls = filesArray.map((file) => URL.createObjectURL(file));
      setPreviewImages((prevImages) => [...prevImages, ...imageUrls]);
    }
  };

  return (
    <div className="create-post-wrapper">
      <Navbar searchData={searchData} setSearchData={setSearchData} />
      <div className="create-post-container">
        <h1 className="create-post-title">Creating Your Own Post</h1>

        <form onSubmit={handleSubmit(onSubmit)} className="create-post-form">
          <input
            {...register("title")}
            placeholder="Title"
            required
            className="input-field title-input"
          />
          <input
            {...register("email")}
            placeholder="Email"
            required
            className="input-field email-input"
          />

          <select
            className="input-field category-select"
            value={selectedPostCategory || ""}
            onChange={(e) => setSelectedPostCategory(Number(e.target.value))}
            required
          >
            <option value="" disabled>
              Select Post Category
            </option>
            <option value={1}>Lifestyle</option>
            <option value={2}>Experience</option>
            <option value={3}>Buy and Sell</option>
          </select>

          <select
            className="input-field package-select"
            value={selectedpackageId || ""}
            onChange={(e) => setSelectedpackageId(Number(e.target.value))}
            required
          >
            <option value="" disabled>
              Select Package
            </option>
            <option value={1}>Basic Package</option>
          </select>

          <select
            className="input-field destiny-select"
            value={selectedDestiny || ""}
            onChange={(e) => setSelectedDestiny(Number(e.target.value))}
            required
          >
            <option value="" disabled>
              Select Destiny
            </option>
            <option value={1}>Fire</option>
            <option value={2}>Water</option>
            <option value={3}>Earth</option>
            <option value={4}>Wood</option>
            <option value={5}>Metal</option>
          </select>

          {/* Replace textarea with CKEditor */}
          <div className="content-editor-container">
            <CKEditor
              editor={ClassicEditor}
              data={content} // The initial content for the editor
              onChange={(event, editor) => {
                const data = editor.getData();
                setContent(data); // Update content state with the editor data
              }}
              config={{
                toolbar: [
                  "bold",
                  "italic",
                  "link",
                  "undo",
                  "redo",
                  "bulletedList",
                  "numberedList",
                ],
              }}
            />
          </div>

          <input
            type="file"
            accept="image/*"
            multiple
            onChange={handleImageChange}
            required
            className="input-field file-input"
          />

          <div className="image-preview-container">
            {previewImages.map((imageUrl, index) => (
              <img
                key={index}
                src={imageUrl}
                alt={`Preview ${index}`}
                className="image-preview"
              />
            ))}
          </div>

          <button type="submit" className="btnSeeMore">
            Create
          </button>
        </form>

        {error && <p className="error-message">{error}</p>}
        {success && <p className="success-message">{success}</p>}
      </div>
      <Footer />
    </div>
  );
};

export default CreatePost;
