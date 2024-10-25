// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getStorage } from "firebase/storage"; // Import Firebase Storage

// Your web app's Firebase configuration
const firebaseConfig = {
  apiKey: "AIzaSyDhu-qs3pc_A6DwRgViZGLnxAwGMoemcTw",
  authDomain: "fengshuikoisystem.firebaseapp.com",
  projectId: "fengshuikoisystem",
  storageBucket: "fengshuikoisystem.appspot.com",
  messagingSenderId: "1022450375371",
  appId: "1:1022450375371:web:9e9b19e2488167ed70194e"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);

// Initialize Firebase Storage
const storage = getStorage(app);

export { app, storage }; // Export both app and storage
