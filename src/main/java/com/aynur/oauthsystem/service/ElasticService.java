package com.aynur.oauthsystem.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import com.aynur.oauthsystem.entity.PdfDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElasticService {

    @Autowired
    private ElasticsearchClient client;

    public void index(PdfDocument doc) {
        try {
            IndexRequest<PdfDocument> request = IndexRequest.of(i -> i
                    .index("pdfs")
                    .id(doc.getId())
                    .document(doc)
            );

            client.index(request);
        } catch (Exception e) {
            throw new RuntimeException("Index error");
        }
    }
}