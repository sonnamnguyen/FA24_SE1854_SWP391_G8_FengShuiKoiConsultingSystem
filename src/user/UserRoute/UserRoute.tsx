import { jwtDecode } from "jwt-decode";
import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
interface JwtPayload {
    iss: string;
    sub: string;
    exp: number;
    iat: number;
    jti: string;
    scope: string; 
}

const UserRoute = <P extends object>(WrappedComponent: React.ComponentType<P>) => {
    const WithAdminCheck: React.FC<P> = (props) => {
        const navigate = useNavigate();

        useEffect(() => {
            const token = localStorage.getItem('token');
            // Trong tình huống chưa đăng nhập
            if (!token) {
                navigate("/login");
                return;
            }

            try {
                // Giải mã token
                const decodedToken = jwtDecode<JwtPayload>(token);
                // Lấy thông tin cụ thể
                const isUser = decodedToken.scope === "ROLE_USER";             

                // Kiểm tra k phải là admin
                if (!isUser) {
                    navigate("/403");
                    return;
                } 
            } catch (error) {
                console.error("Invalid token", error);
                navigate("/login"); // Nếu token không hợp lệ, điều hướng đến trang đăng nhập
            }
        }, [navigate]);

        // Trả về null nếu điều hướng xảy ra
        return <WrappedComponent {...props} />;
    }

    return WithAdminCheck;
}

export default UserRoute;
