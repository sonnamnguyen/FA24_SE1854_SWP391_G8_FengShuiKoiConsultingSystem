// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getAnalytics } from "firebase/analytics";
import { getMessaging, getToken } from "firebase/messaging";
import { getStorage } from "firebase/storage"; // Import Firebase Storage

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
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
export const messaging = getMessaging(app);
const analytics = getAnalytics(app);

// Initialize Firebase Storage
const storage = getStorage(app);

export const generateToken = async () => {
  const permission = await Notification.requestPermission();
  if (permission === "granted") {
    console.log(permission);
    const token = await getToken(messaging, {
      vapidKey: "BEWVqlpy-txsAzvUdwjM9CAo26aoa08pcLh7GNAxP4x2LAh9SVTJd8IP5na9Biup_b46livPPyT-U5gKZWLou-Q"
    })
    console.log(token);
  }
  
}


export { app, storage }; // Export both app and storage