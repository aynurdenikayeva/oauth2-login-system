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

        String email = user.getAttribute("email");
        String name = user.getAttribute("name");

        String provider = request.getRequestURI().contains("github") ? "GITHUB" : "GOOGLE";

        User dbUser = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(new User(null, name, email, provider)));

        String token = jwtService.generateToken(dbUser.getEmail());

        response.getWriter().write(token);
    }
}