package com.aynur.oauthsystem.repository;

import com.aynur.oauthsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PdfRepository extends JpaRepository<User, Long> {
}
