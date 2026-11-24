import { server } from './server';

export const documentService = {
    list: (page = 0, size = 20) => server.get(`/documents/data?page=${page}&size=${size}`),
    upload: (file: any) => {
        const form = new FormData()
        form.append('uploadFile', file)
        return server.post('/documents/upload', form)
    },
    addText: (info: any) =>
        server.post('/documents/add-text', info),
    remove: (id: string) => server.delete(`/documents/data-to-delete?dataToDelete=${id}`),
}