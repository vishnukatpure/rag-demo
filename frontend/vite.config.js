import tailwindcss from '@tailwindcss/vite'
import react from '@vitejs/plugin-react'
import { defineConfig } from 'vite'

export default defineConfig({
  plugins: [react(), tailwindcss()],
  server: {
    'Service-Worker-Allowed': '/chat/'
  },
  build: {
    outDir: 'investor-dist',
    assetsDir: 'assets',
    chunkSizeWarningLimit: 5000, // Increase warning limit to 5000 kB
    rollupOptions: {
      output: {
        entryFileNames: 'assets/js/[name].[hash].js',
        chunkFileNames: 'assets/js/[name].[hash].js',
        assetFileNames: (assetInfo) => {
          if (!assetInfo.name) {
            return 'assets/[name].[hash][extname]';
          }
          const info = assetInfo.name.split('.');
          const ext = info[info.length - 1];
          if (/\.(css)$/.test(assetInfo.name)) {
            return `assets/css/[name].[hash].${ext}`;
          }
          return `assets/[name].[hash][extname]`;
        }
      }
    }
  }
})
