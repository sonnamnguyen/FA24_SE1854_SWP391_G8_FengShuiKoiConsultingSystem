import React, { useEffect } from 'react';
import './App.css';
import AppRoutes from './routes/AppRoutes';
import { generateToken, messaging } from './firebase/firebase';
import { onMessage } from 'firebase/messaging';

function App() {
  useEffect(() => {
    // Đăng ký Service Worker
    if ('serviceWorker' in navigator) {
      navigator.serviceWorker
        .register('/firebase-messaging-sw.js')
        .then((registration) => {
          console.log('Service Worker đã được đăng ký với scope:', registration.scope);
          generateToken();
        })
        .catch((error) => {
          console.error('Đăng ký Service Worker thất bại:', error);
        });
    }

    // Lắng nghe thông báo foreground
    onMessage(messaging, (payload) => {
      console.log('Thông báo foreground nhận được:', payload);
      // Bạn có thể tùy chỉnh hiển thị thông báo trong ứng dụng tại đây
      alert(`Thông báo mới: ${payload.notification?.title || 'Thông báo'} - ${payload.notification?.body || ''}`);
    });
  }, []);

  return <AppRoutes />;
}

export default App;
