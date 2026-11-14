//package com.healthcheck;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//@Component
//public class UserAuthorizationInterceptor
//        implements HandlerInterceptor {
//
//    private final AuthorizationService authorizationService =
//            new AuthorizationService();
//
//    @Override
//    public boolean preHandle(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            Object handler) throws Exception {
//
//        // Primary method
//        String remoteUser = request.getRemoteUser();
//        
//        
//
//        // IIS Reverse Proxy Header Fallback
//        if (remoteUser == null || remoteUser.trim().isEmpty()) {
//
//            remoteUser =
//                    request.getHeader(
//                            "X-Forwarded-User");
//        }
//
//        // Debug Logging
//        System.out.println(
//                "RemoteUser      = "
//                        + request.getRemoteUser());
//
//        System.out.println(
//                "ForwardedUser   = "
//                        + request.getHeader(
//                        "X-Forwarded-User"));
//
//        System.out.println(
//                "FinalUser       = "
//                        + remoteUser);
//
//        if (remoteUser == null
//                || remoteUser.trim().isEmpty()) {
//
//            request.getSession()
//                    .setAttribute(
//                            "deniedUser",
//                            "USER_NOT_RECEIVED_FROM_IIS");
//
//            response.sendRedirect(
//                    "/access-denied");
//
//            return false;
//        }
//
//        // Full AD account
//        String displayUser =
//                remoteUser;
//
//        // Username only
//        String username =
//                remoteUser;
//
//        if (username.contains("\\")) {
//
//            username =
//                    username.substring(
//                            username.lastIndexOf("\\")
//                                    + 1);
//        }
//
//        System.out.println(
//                "Username        = "
//                        + username);
//
//        boolean authorized =
//                authorizationService
//                        .isAuthorized(
//                                username);
//
//        System.out.println(
//                "Authorized       = "
//                        + authorized);
//
//        if (!authorized) {
//
//            request.getSession()
//                    .setAttribute(
//                            "deniedUser",
//                            displayUser);
//
//            response.sendRedirect(
//                    "/access-denied");
//
//            return false;
//        }
//
//        request.getSession()
//                .setAttribute(
//                        "loggedInUser",
//                        username);
//
//        return true;
//    }
//}