package com.fengshuisystem.demo.configuration;

public class EndPoint {

        public static final String[] ADMIN_GET_ENDPOINTS = {
                "/animals", "/animals/{search}"

        };
        public static final String[] ADMIN_POST_ENDPOINTS = {
                "/animals"

        };

        public static final String[] ADMIN_PUT_ENDPOINTS = {
                "/animals/{id}"
        };

        public static final String[] ADMIN_DELETE_ENDPOINTS = {
                "/animals/{id}"
        };

        public static final String[] PUBLIC_ENDPOINTS = {
                "/users", "/users/admin", "/auth/token", "/auth/introspect", "/auth/logout", "/auth/refresh"
                , "/auth/outbound/authentication" , "/roles"
        };
    }

