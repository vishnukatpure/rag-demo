import { server } from './server';

export const LoginService = {
    login: (username: string, password: string) =>
        server.post('/non-secured/auth/login', {
            username, password
        }),
    register: (payload: any) =>
        server.post('/non-secured/auth/register', payload),
    me: () => server.get('/users/me'),
}