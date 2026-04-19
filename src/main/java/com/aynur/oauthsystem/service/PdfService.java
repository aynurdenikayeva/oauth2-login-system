package com.aynur.oauthsystem.service;

import com.aynur.oauthsystem.entity.PdfDocument;
import com.aynur.oauthsystem.util.PdfUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final PdfUtil tikaUtil;
    private final ElasticService elasticService;

    @Async
    public void processPdfAsync(MultipartFile file) {

        try {
            String text = tikaUtil.extractText(file.getInputStream());

            PdfDocument doc = new PdfDocument(
                    UUID.randomUUID().toString(),
                    file.getOriginalFilename(),
                    text
            );

            elasticService.index(doc);

        } catch (Exception e) {
            throw new RuntimeException("PDF error");
        }
    }
}