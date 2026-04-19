package com.aynur.oauthsystem.service;

import com.aynur.oauthsystem.entity.User;
import com.aynur.oauthsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class OAuth2Service {

    @Autowired
    private UserRepository userRepository;

    public User processUser(OAuth2User oAuth2User) {

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        return userRepository.findByEmail(email)
                .orElseGet(() ->
                        userRepository.save(new User(null, name, email, "GOOGLE"))
                );
    }
}