import { server } from './server';

export const ragService = {
    ask: (question: string) =>
        server.post('/rag/ask', { question }),
}