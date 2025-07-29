// components/ProtectedRoute.js
import React from 'react';
import { Navigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';

const ProtectedRoute = ({ children, allowedRoles }) => {
    const token = localStorage.getItem('token');

    if (!token) {
        return <Navigate to="/" replace />;
    }

    try {
        const decoded = jwtDecode(token);
        const userRole = decoded.role;

        if (allowedRoles.includes(userRole)) {
            return children;
        } else {
            console.warn(`Access denied: userRole=${userRole}, allowed=${allowedRoles}`);
            return <Navigate to="/" replace />;
        }
    } catch (error) {
        console.error("Invalid token:", error);
        return <Navigate to="/" replace />;
    }
};

export default ProtectedRoute;
