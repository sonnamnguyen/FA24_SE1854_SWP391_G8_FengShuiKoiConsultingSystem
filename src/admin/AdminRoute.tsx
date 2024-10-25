// AdminRoute.tsx
import { jwtDecode } from "jwt-decode";
import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { getToken } from "../service/localStorageService";

interface JwtPayload {
    iss: string;
    sub: string;
    exp: number;
    iat: number;
    jti: string;
    scope: string; 
}

// Define AdminRoute as a function that returns a React component
const AdminRoute = <P extends object>(WrappedComponent: React.ComponentType<P>) => {
    // Define the component that will perform the admin check
    const WithAdminCheck: React.FC<P> = (props) => {
        const navigate = useNavigate();

        useEffect(() => {
            const token =  getToken();
            if (!token) {
                navigate("/login");
                return;
            }

            try {
                const decodedToken = jwtDecode<JwtPayload>(token);
                const isAdmin = decodedToken.scope === "ROLE_ADMIN";             

                if (!isAdmin) {
                    navigate("/403");
                    return;
                } 
            } catch (error) {
                console.error("Invalid token", error);
                navigate("/login");
            }
        }, [navigate]);

        return <WrappedComponent {...props} />;
    };

    return WithAdminCheck; // Return the component
};

export default AdminRoute;
