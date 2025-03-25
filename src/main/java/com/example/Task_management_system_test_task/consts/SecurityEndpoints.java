package com.example.Task_management_system_test_task.consts;


public class SecurityEndpoints {
    public static final String[] ADMIN_ENDPOINTS_POST = new String[]{
            "/task"
    };
    public static final String[] ADMIN_ENDPOINTS_PUT = new String[]{
            "/task/admin"
    };
    public static final String[] ADMIN_ENDPOINTS_DELETE = new String[]{
            "/task/{id}", "/comment/{id}"
    };


    public static final String[] USER_ENDPOINTS_PUT = new String[]{
            "/task", "/task/implementer/{id}"
    };
    public static final String[] USER_ENDPOINTS_GET = new String[]{
            "/task/creator/{id}"
    };
    public static final String[] USER_ENDPOINTS_POST = new String[]{
            "/task/filters"
    };

    public static final String[] SWAGGER_ENDPOINTS = new String[] {
            "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"
    };
}
