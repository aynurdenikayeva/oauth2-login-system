package com.aynur.oauthsystem.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.aynur.oauthsystem.entity.PdfDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    @Autowired
    private ElasticsearchClient client;

    public List<PdfDocument> search(String keyword, int page, int size) {

        try {
            SearchResponse<PdfDocument> response = client.search(s -> s
                            .index("pdfs")
                            .from(page * size)
                            .size(size)
                            .query(q -> q
                                    .match(m -> m
                                            .field("content")
                                            .query(keyword)
                                    )
                            ),
                    PdfDocument.class
            );

            return response.hits().hits()
                    .stream()
                    .map(hit -> hit.source())
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException("Search error");
        }
    }
}