import axios from 'axios';
import { toast } from '../hooks/use-toast';
import { getStoredToken } from './storage.util';
import { BASE_URL, VITE_BACKEND_API_URL } from '../constant/constants';


// Create an axios instance
export const server = axios.create({
  baseURL: VITE_BACKEND_API_URL,
  headers: {
    'X-Requested-With': 'XMLHttpRequest',
    'accept': '*/*'
  },
  withCredentials: false, // Ensure you are sending cookies if needed
});

// Add request interceptor
server.interceptors.request.use(
  (config) => {
    // Optionally, add auth token here if needed
    const token = getStoredToken(); // Example: You can fetch token from localStorage or sessionStorage
    if (token) {
      config.headers = config.headers || {};
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Add response interceptor
server.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    // Handle global error scenarios
    if (error.response?.status === 302 || error.code === 'ERR_NETWORK') {
      // Handle unauthorized (e.g., redirect to login)
      console.log('Unauthorized access - redirecting to login');
      window.location.href = `${BASE_URL}`;
    }
    if (error.response?.status === 500) {
      // Handle server error
      toast({
        title: 'Sorry, something went wrong!',
        description: 'Please try again later.',
        variant: 'error',
      });
    }
    return Promise.reject(error); // Re-throw the error
  });



export async function downloadFile(fileId: string, fileName: string | undefined) {
  if (fileName == undefined)
    return;
  const token = getStoredToken()
  try {
    const response = await server.get(`/file/${encodeURIComponent(fileId)}`, {
      headers: token ? { Authorization: `Bearer ${token}` } : {},
    })
    const blob = new Blob([response.data], { type: response.headers['content-type'] as string });
    const link = document.createElement('a');
    link.href = window.URL.createObjectURL(blob);
    link.download = fileName;
    link.click();
    window.URL.revokeObjectURL(link.href); // This URL can be used to display/download the file
  } catch (error) {
    toast({
      title: 'Sorry, something went wrong!',
      description: 'Please try again later.',
      variant: 'error',
    });
    throw error;
  }
}
