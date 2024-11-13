// Give the service worker access to Firebase Messaging.
// Note that you can only use Firebase Messaging here. Other Firebase libraries
// are not available in the service worker.
// Replace 10.13.2 with latest version of the Firebase JS SDK.
importScripts('https://www.gstatic.com/firebasejs/10.13.2/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/10.13.2/firebase-messaging-compat.js');

// Initialize the Firebase app in the service worker by passing in
// your app's Firebase config object.
// https://firebase.google.com/docs/web/setup#config-object
firebase.initializeApp({
    apiKey: "AIzaSyBZYq6Rc75-LC99jOsCGOdGfsbuxp1nGpI",
    authDomain: "notification-2defc.firebaseapp.com",
    projectId: "notification-2defc",
    storageBucket: "notification-2defc.firebasestorage.app",
    messagingSenderId: "1086510466261",
    appId: "1:1086510466261:web:b2057ac51290b6a93cce5c",
    measurementId: "G-YGNFFQ9F7K"
});

// Retrieve an instance of Firebase Messaging so that it can handle background
// messages.
const messaging = firebase.messaging();

messaging.onBackgroundMessage((payload) => {
    console.log(
      '[firebase-messaging-sw.js] Received background message ',
      payload
    );
    // Customize notification here
    const notificationTitle = payload.notification?.title || "Background Message Title";
    const notificationOptions = {
        body: payload.notification?.body || "Background Message body.",
        icon: payload.notification?.icon || "./koifish.png", // Set a default icon if needed
    };

  
    self.registration.showNotification(notificationTitle, notificationOptions);
  });