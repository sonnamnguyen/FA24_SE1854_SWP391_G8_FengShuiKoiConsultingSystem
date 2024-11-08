import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './App.css';
import AppRoutes from './routes/AppRoutes';
import { getToken } from "./service/localStorageService";

const App = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const token = getToken();
    if (token) {
      try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(
          atob(base64)
            .split('')
            .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
            .join('')
        );
        const decodedToken = JSON.parse(jsonPayload);

        if (decodedToken && decodedToken.scope === 'ROLE_ADMIN') {
          navigate('/admin-page');
        }
      } catch (error) {
        console.error('Lỗi giải mã token:', error);
      }
    }
  }, [navigate]);

  return <AppRoutes />;
};

export default App;
