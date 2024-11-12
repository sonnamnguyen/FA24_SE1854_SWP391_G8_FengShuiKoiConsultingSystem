// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getAnalytics } from "firebase/analytics";
import { getMessaging, getToken } from "firebase/messaging";
import { getStorage } from "firebase/storage"; // Import Firebase Storage

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyBZYq6Rc75-LC99jOsCGOdGfsbuxp1nGpI",
  authDomain: "notification-2defc.firebaseapp.com",
  projectId: "notification-2defc",
  storageBucket: "notification-2defc.firebasestorage.app",
  messagingSenderId: "1086510466261",
  appId: "1:1086510466261:web:b2057ac51290b6a93cce5c",
  measurementId: "G-YGNFFQ9F7K"
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