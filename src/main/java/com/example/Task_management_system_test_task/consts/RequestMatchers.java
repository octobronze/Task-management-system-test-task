package com.example.Task_management_system_test_task.consts;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class RequestMatchers {
    public static final RequestMatcher[] ADMIN_ENDPOINTS = new RequestMatcher[]{
            new RegexRequestMatcher("/task", HttpMethod.POST.name()),
            new RegexRequestMatcher("/task/admin", HttpMethod.PUT.name()),
            new RegexRequestMatcher("/task/{id}", HttpMethod.DELETE.name()),
            new RegexRequestMatcher("/comment/{id}", HttpMethod.DELETE.name())
    };

    public static final RequestMatcher[] USER_ENDPOINTS = new RequestMatcher[] {
            new RegexRequestMatcher("/task", HttpMethod.PUT.name()),
            new RegexRequestMatcher("/task/implementer/{id}", HttpMethod.PUT.name()),
            new RegexRequestMatcher("/task/creator/{id}", HttpMethod.GET.name())
    };
}
