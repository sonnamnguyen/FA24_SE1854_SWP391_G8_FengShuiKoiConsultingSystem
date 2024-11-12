import axios, { AxiosError, AxiosRequestConfig, AxiosResponse, AxiosHeaders } from 'axios';
import { getToken } from '../service/localStorageService';
import { logOut } from '../service/authentication';

// Định nghĩa interface cho cấu hình request tùy chỉnh
interface CustomAxiosRequestConfig extends AxiosRequestConfig {
    _retry?: boolean; // Thuộc tính tùy chọn để theo dõi số lần thử lại
    headers: AxiosHeaders; // Đảm bảo headers luôn được định nghĩa
}

// Địa chỉ API Base
const baseUrl: string = 'http://localhost:9090'; // Địa chỉ API của bạn

// Tạo một instance Axios với cấu hình mặc định
const api = axios.create({
    baseURL: baseUrl,
    headers: {
        'Content-Type': 'application/json', // Cấu hình header mặc định
    },
});


let isRefreshing = false;
let failedRequestsQueue: Array<any> = [];

// Function to refresh the access token
const refreshAccessToken = async () => {
    try {
        const refreshToken = getToken();
        const response = await api.post('/auth/refresh', {
            token: refreshToken,
        });

        const newAccessToken = response.data.accessToken;
        localStorage.setItem('token', JSON.stringify(newAccessToken));

        return newAccessToken;
    } catch (error) {
        throw error;
    }
};

// Request Interceptor: Attach access token to each request
api.interceptors.request.use(
    (config: CustomAxiosRequestConfig) => {
        const token = getToken();
        if (token) {
            // Khởi tạo headers nếu chúng không được định nghĩa
            config.headers = config.headers || new AxiosHeaders();
            config.headers.set('Authorization', `Bearer ${token}`);
        }
        return config;
    },
    (error) => Promise.reject(error)
);

// Response Interceptor: Handle 401 and refresh token
api.interceptors.response.use(
    (response: AxiosResponse) => response, // Pass through success responses
    async (error: AxiosError) => {
        const originalRequest = error.config as CustomAxiosRequestConfig; // Cast to CustomAxiosRequestConfig

        // Kiểm tra lỗi do token hết hạn (401) và ngăn chặn vòng lặp thử lại
        if (error.response?.status === 401 && !originalRequest?._retry) {
            if (!isRefreshing) {
                originalRequest._retry = true; // Đánh dấu yêu cầu đã thử lại
                isRefreshing = true;

                try {
                    const newAccessToken = await refreshAccessToken();

                    // Thử lại yêu cầu gốc với token mới
                    if (originalRequest.headers) {
                        originalRequest.headers.set('Authorization', `Bearer ${newAccessToken}`);
                    }
                    return api(originalRequest);
                } catch (err) {
                    // Nếu refresh token không thành công, điều hướng về trang đăng nhập
                    logOut(); // Gọi hàm logOut để xử lý đăng xuất
                    window.location.href = '/login'; // Điều hướng về trang đăng nhập
                    return Promise.reject(err);
                } finally {
                    isRefreshing = false;
                }
            }

            // Queue requests while token is being refreshed
            return new Promise((resolve, reject) => {
                failedRequestsQueue.push({
                    onSuccess: (token: string) => {
                        if (originalRequest.headers) {
                            originalRequest.headers.set('Authorization', `Bearer ${token}`);
                        }
                        resolve(api(originalRequest)); // Thử lại yêu cầu gốc
                    },
                    onFailure: (err: AxiosError) => reject(err),
                });
            });
        }

        // Đối với các lỗi khác, từ chối promise
        return Promise.reject(error);
    }
);
export default api