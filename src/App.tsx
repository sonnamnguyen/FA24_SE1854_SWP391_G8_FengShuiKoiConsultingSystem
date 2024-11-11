import React, { useEffect } from 'react';
import './App.css';
import AppRoutes from './routes/AppRoutes';
import { generateToken, messaging } from './firebase/firebase'
import { onMessage } from 'firebase/messaging';

function App() {

  useEffect(() => {
    generateToken();
    onMessage(messaging, (payload) => {
      console.log(payload);
    })
  }, []);

  return (
    <AppRoutes />
  );
}

export default App;