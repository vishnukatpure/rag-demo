package com.example.rag_pipeline.config;

import java.net.URISyntaxException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest5_client.Rest5ClientTransport;
import co.elastic.clients.transport.rest5_client.low_level.Rest5Client;

@Configuration
public class ElasticsearchConfig {

	@Bean
	ElasticsearchClient elasticsearchClient(Rest5Client restClient) throws URISyntaxException {

		ElasticsearchTransport transport = new Rest5ClientTransport(restClient, new JacksonJsonpMapper());

		return new ElasticsearchClient(transport);
	}
}
