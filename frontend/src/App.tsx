import { AuthProvider } from './auth/AuthProvider'
import { useAuth } from './auth/useAuth'
import AuthPage from './pages/AuthPage'
import Workspace from './pages/Workspace'

function Gate() {
  const { isAuthenticated, booting } = useAuth()

  if (booting) {
    return (
      <div className="flex min-h-svh items-center justify-center text-sm text-[var(--color-muted)]">
        Opening workspace…
      </div>
    )
  }

  return isAuthenticated ? <Workspace /> : <AuthPage />
}

export default function App() {
  return (
    <AuthProvider>
      <Gate />
    </AuthProvider>
  )
}
