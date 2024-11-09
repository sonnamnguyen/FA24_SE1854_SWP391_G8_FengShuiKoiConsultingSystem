package com.fengshuisystem.demo.configuration;

public class EndPoint {

        public static final String[] ADMIN_GET_ENDPOINTS = {
                "/animals", "/animals/animal-search", "/shelters", "/shelters/search-name", "/shelters/{search}",
                "/colors", "shapes", "/animal-images", "/shelter-images", "/destinys", "colors/getAll-Colors",
                "shapes/getAll-Shapes", "shapes/shape-search", "/users", "/users/search-name", "/roles",
                "colors/search-colors", "/bills", "/bills/status", "api/bills", "api/bills/status"
        };

        public static final String[] ADMIN_POST_ENDPOINTS = {
                "/animals", "/shelters", "/colors", "/shapes", "/consulation-animal", "/consulation-shelter",
                "/users{userId}/set-roles", "/animal-images/{id}", "/shelter-images/{id}", "/packages",
                "/api/consultation-results/requestId/{requestId}",
                "/api/consultation-animals/resultId/{resultId}/animal-category-id/{animalCategoryId}",
                "/api/consultation-shelters/resultId/{resultId}/shelter-category-id/{shelterCategoryId}"
        };

        public static final String[] ADMIN_PUT_ENDPOINTS = {
                "/animals/{id}", "/shelters/{id}", "/colors/{id}", "/shapes/{id}", "/bills/{id}",
                "/packages/{id}", "api/bills/{id}", "/api/consultation-results/send-email/{resultId}",
                "/api/consultation-results/{id}"
        };

        public static final String[] ADMIN_DELETE_ENDPOINTS = {
                "/animals/{id}", "/shelters/{id}", "/colors/{id}", "/shapes/{id}", "/packages/{id}"
        };

        public static final String[] PUBLIC_ENDPOINTS = {
                "/users", "/users/admin", "/auth/token", "/auth/introspect", "/auth/logout", "/auth/refresh",
                "/auth/outbound/authentication", "/roles", "auth/token-email",
                "/destinys/compatibility/{yearOfBirth}"
        };

        public static final String[] PUBLIC_GET_ENDPOINTS = {
                "users/existByUserName", "users/existByEmail", "users/activate",
                "/destinys/destiny/{yearOfBirth}", "/destinys/autoConsultation/{yearOfBirth}",
                "api/vn_pay/create_vn_pay", "shapes/getAll-Shapes", "/api/number",
                "animals/animalCategory", "colors/getAll-Colors"
        };

        public static final String[] USER_GET_ENDPOINTS = {
                "/posts", "/posts/search-posts", "/post/comments", "/post/images",
                "/consultation-request-details/{id}", "/api/consultation-request-details/{requestDetailId}",
                "api/bills/{billId}", "/vn_pay/create_vn_pay","/search-posts/title","/search-posts/email","/search-posts/{id}"
        };

        public static final String[] USER_POST_ENDPOINTS = {
                "users/reset-password", "users/forgot-password", "/posts", "/post/comments", "/post/images",
                "api/bills", "/api/consultation-requests",
                "/api/consultation-request-details/request-id/{requestId}/bill-id/{billId}",
                "api/bills/request/{requestId}/payments/{paymentId}"
        };

        public static final String[] USER_PUT_ENDPOINTS = {
                "/posts/{id}", "/post/comments/{id}", "/post/images/{id}", "api/bills/{id}",
                "/api/consultation-requests/{requestId}"
        };

        public static final String[] USER_DELETE_ENDPOINTS = {
                "/posts/{id}", "/post/comments/{id}", "/post/images/{id}",
                "/api/consultation-request-details/{requestDetailId}"
        };

        public static final String[] BOTH_GET_ENDPOINTS = {
                "api/bills/{accountId}/status", "/api/consultation-request-details/{requestDetailId}",
                "api/bills/{billId}"
        };

        public static final String[] BOTH_DELETE_ENDPOINTS = {
                "/posts/{id}"
        };
}