//package com.healthcheck;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    private final UserAuthorizationInterceptor interceptor;
//
//    public WebConfig(
//            UserAuthorizationInterceptor interceptor) {
//
//        this.interceptor = interceptor;
//    }
//
//    @Override
//    public void addInterceptors(
//            InterceptorRegistry registry) {
//
//        registry.addInterceptor(interceptor)
//                .addPathPatterns("/**")
//                .excludePathPatterns(
//                        "/access-denied",
//                        "/css/**",
//                        "/js/**",
//                        "/images/**",
//                        "/webjars/**");
//    }
//}