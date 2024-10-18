package com.fengshuisystem.demo.configuration;

public class EndPoint {

        public static final String[] ADMIN_GET_ENDPOINTS = {
                "/animals", "/animals/{search}", "/shelters", "/shelters/{search}",
                "/colors", "shapes","/bills","/bills/status"

        };
        public static final String[] ADMIN_POST_ENDPOINTS = {
                "/animals", "/shelters", "/colors", "/shapes","/packages"

        };

        public static final String[] ADMIN_PUT_ENDPOINTS = {
                "/animals/{id}", "/shelters/{id}", "/colors/{id}",  "/shapes/{id}","/bills/{id}","/packages/{id}"
        };

        public static final String[] ADMIN_DELETE_ENDPOINTS = {
                "/animals/{id}", "/shelters/{id}", "/colors/{id}",
                "/shapes/{id}","/packages/{id}"
        };

        public static final String[] PUBLIC_ENDPOINTS = {
                "/users", "/users/admin", "/auth/token", "/auth/introspect", "/auth/logout", "/auth/refresh"
                , "/auth/outbound/authentication" , "/roles"
        };
        public static final String[] USER_GET_ENDPOINTS = {
                "/posts","/posts/search-posts","/post/comments","/post/images","/vn_pay/create_vn_pay"


        };
        public static final String[] USER_POST_ENDPOINTS = {
                "/posts","/post/comments","/post/images","/bills",



        };
        public static final String[] USER_PUT_ENDPOINTS = {
                "/posts/{id}","/post/comments/{id}","/post/images/{id}","/bills/{id}",
        };

        public static final String[] USER_DELETE_ENDPOINTS = {
                "/posts/{id}","/post/comments/{id}","/post/images/{id}",

        };
        public static final String[] BOTH_GET_ENDPOINTS = {
                "/bills/{accountId}/status"

        };
        public static final String[] BOTH_DELETE_ENDPOINTS = {
                "/posts/{id}"


        };
    }

