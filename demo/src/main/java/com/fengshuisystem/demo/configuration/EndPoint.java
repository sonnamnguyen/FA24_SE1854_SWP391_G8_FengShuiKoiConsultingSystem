
package com.fengshuisystem.demo.configuration;

public class EndPoint {

        public static final String[] ADMIN_GET_ENDPOINTS = {
                "/animals", "/animals/animal-search", "/shelters", "/shelters/{search}",
                "/colors", "shapes", "/animal-images", "/shelter-images", "/destinys", "colors/getAll-Colors",
                "shapes/getAll-Shapes","/bills","/bills/status"

        };
        public static final String[] ADMIN_POST_ENDPOINTS = {
                "/animals", "/shelters", "/colors", "/shapes", "/consulation-animal", "/consulation-shelter","/packages"

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
                , "/auth/outbound/authentication" , "/roles", "auth/token-email"
        };
        public static final String[] PUBLIC_GET_ENDPOINTS = {
                "users/existByUserName", "users/existByEmail", "users/activate"
        };
        public static final String[] USER_GET_ENDPOINTS = {
                "/posts","/posts/search-posts","/post/comments","/post/images","/vn_pay/create_vn_pay","/search-posts/email","/search-posts/title"


        };
        public static final String[] USER_POST_ENDPOINTS = {
                "users/reset-password","users/forgot-password","/posts","/post/comments","/post/images","/bills",
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
