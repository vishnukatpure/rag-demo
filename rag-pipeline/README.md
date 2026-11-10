# RAG Demo – Retrieval Augmented Generation using Java & Spring Boot

## Overview

`rag-demo` is a hands-on implementation of a **Retrieval-Augmented Generation (RAG)** pipeline using Java and Spring Boot.

The objective of this project is to build an AI-powered application that can answer user questions based on custom documents and knowledge sources rather than relying entirely on the LLM's pre-trained knowledge.

The project initially integrates with an external LLM API and is designed to support local LLM integration in the future.

## What is RAG?

Retrieval-Augmented Generation (RAG) combines information retrieval with Large Language Models (LLMs).

Instead of directly sending a user's question to an LLM, RAG follows these steps:

1. Receive a question from the user.
2. Convert the question into vector embeddings.
3. Search the knowledge base for relevant information.
4. Retrieve the most relevant document chunks.
5. Pass the retrieved context and original question to the LLM.
6. Generate a context-aware response.

## Architecture

```text
                 User
                   |
                   v
             REST API
                   |
                   v
             Spring Boot
                   |
                   v
             RAG Pipeline
                   |
          +--------+--------+
          |                 |
          v                 v
     Embedding Model    Vector Database
          |                 |
          |                 v
          |           Similarity Search
          |                 |
          +--------+--------+
                   |
                   v
             Retrieved Context
                   |
                   v
               LLM API
                   |
                   v
             Generated Answer
                   |
                   v
                 User
```

## Technology Stack

| Component | Technology |
|---|---|
| Programming Language | Java |
| Backend Framework | Spring Boot |
| API | REST API |
| LLM | llama3.2:1b |
| Embedding Model | nomic-embed-text |
| Vector Database | ElasticSearch |
| Build Tool | Maven |


## Key Features

- Document ingestion and processing.
- Document text extraction and chunking.
- Text-to-vector embedding generation.
- Vector-based similarity search.
- Context-aware question answering.
- Integration with external LLM APIs.
- REST API-based interaction.
- Extensible architecture for local LLM integration.

## Project Structure

```text
rag-demo/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/rag/
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── config/
│   │   │       ├── model/
│   │   │       ├── repository/
│   │   │       └── RagDemoApplication.java
│   │   │
│   │   └── resources/
│   │       └── application.yml
│   │
│   └── test/
│
├── documents/
│
├── pom.xml
└── README.md
```

## Getting Started

### Prerequisites

- Java 17 or later
- Maven
- Vector database (Elastic search)

### Configure Application

Configure the required API credentials and application properties in `application.yml`.

Example:

```yaml
spring:
  application:
    name: rag-demo

# Configure your LLM provider,
# embedding model and vector database here.
```

Store API keys securely using environment variables. Do not commit secrets to Git.



## RAG Workflow

### 1. Document Ingestion

Upload or provide documents that will serve as the knowledge base.

### 2. Text Processing

Extract text from documents and split it into smaller chunks.

### 3. Embedding Generation

Convert document chunks into numerical vector representations using an embedding model.

### 4. Vector Storage

Store the generated embeddings along with their associated document content and metadata.

### 5. Similarity Search

When a user submits a question, retrieve the most relevant document chunks using vector similarity.

### 6. LLM Response Generation

Send the retrieved context and user question to the LLM to generate a relevant answer.

## Development Roadmap

- [ ] Set up Spring Boot application.
- [ ] Integrate an external LLM API.
- [ ] Implement document ingestion.
- [ ] Implement document chunking.
- [ ] Integrate an embedding model.
- [ ] Configure vector database.
- [ ] Implement similarity search.
- [ ] Develop RAG-based question-answering REST API.
- [ ] Add conversation history.
- [ ] Add source references to generated answers.
- [ ] Integrate a local LLM.
- [ ] Compare external and local LLM responses.

## Future Enhancements

- Support multiple document formats (PDF, DOCX, TXT).
- Implement metadata-based filtering.
- Add hybrid search (keyword + vector search).
- Implement reranking for better retrieval accuracy.
- Add conversational memory.
- Support multiple LLM providers.

## Learning Objectives

This project is intended to explore:

- RAG architecture and implementation.
- LLM integration with Java applications.
- Embedding models and vector databases.
- Semantic search.
- Prompt engineering.
- Retrieval optimization.

## License

This project is intended for learning and experimentation.
