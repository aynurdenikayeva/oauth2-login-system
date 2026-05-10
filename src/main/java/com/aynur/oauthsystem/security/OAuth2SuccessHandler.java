package com.aynur.oauthsystem.security;

import com.aynur.oauthsystem.entity.User;
import com.aynur.oauthsystem.repository.UserRepository;
import com.aynur.oauthsystem.service.JwtService;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User user = (OAuth2User) authentication.getPrincipal();

        String provider = request.getRequestURI().contains("github") ? "GITHUB" : "GOOGLE";

        String email;
        String name;

        if ("GITHUB".equals(provider)) {
            String login = user.getAttribute("login");
            email = login + "@github.local";
            name = user.getAttribute("name");
            if (name == null) name = login;
        } else {
            email = user.getAttribute("email");
            name = user.getAttribute("name");
            if (name == null) name = "Google User";
        }

        final String finalEmail = email;
        final String finalName = name;
        final String finalProvider = provider;

        User dbUser = userRepository.findByEmail(finalEmail)
                .orElseGet(() -> userRepository.save(
                        new User(null, finalName, finalEmail, finalProvider)
                ));

        String token = jwtService.generateToken(dbUser.getEmail());

        response.setContentType("application/json");
        response.getWriter().write(token);
    }
}