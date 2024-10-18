package com.fengshuisystem.demo.configuration;

public class EndPoint {

        public static final String[] ADMIN_GET_ENDPOINTS = {
                "/animals", "/animals/{search}", "/shelters", "/shelters/{search}",
                "/colors", "shapes", "/animal-images", "/shelter-images", "api/destiny-years", "api/consultation_destiny"

        };
        public static final String[] ADMIN_POST_ENDPOINTS = {
                "/animals", "/shelters", "/colors", "/shapes", "/consulation-animal", "/consulation-shelter",
                "api/destiny-years", "api/consultation_destiny"
        };

        public static final String[] ADMIN_PUT_ENDPOINTS = {
                "/animals/{id}", "/shelters/{id}", "/colors/{id}",  "/shapes/{id}","api/destiny-years/{id}",
                "api/consultation_destiny/{id}"
        };

        public static final String[] ADMIN_DELETE_ENDPOINTS = {
                "/animals/{id}", "/shelters/{id}", "/colors/{id}",
                "/shapes/{id}","api/destiny-years/{id}","api/consultation_destiny/{id}"
        };

        public static final String[] PUBLIC_ENDPOINTS = {
                "/users", "/users/admin", "/auth/token", "/auth/introspect", "/auth/logout", "/auth/refresh"
                , "/auth/outbound/authentication" , "/roles", "auth/token-email"
        };
        public static final String[] PUBLIC_GET_ENDPOINTS = {
                "users/existByUserName", "users/existByEmail", "users/activate", "api/consultationDestiny/{yearOfBirth}",
                "api/compatibility/{yearOfBirth}"
        };

        public static final String[] USER_POST_ENDPOINTS = {
                "users/forgot-password",
                "users/reset-password"
        };
    }

