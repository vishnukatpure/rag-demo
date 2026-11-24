
import { useEffect, useMemo, useState } from 'react'
import {
  clearSession,
  getStoredToken,
  getStoredUser,
  persistSession
} from '../api/storage.util.ts'
import { AuthContext } from './auth-context'
import type { AuthContextType } from './auth-context'
import { LoginService } from '../api/login.service'

export function AuthProvider({
  children
}: {
  children: React.ReactNode
}) {
  const [user, setUser] = useState<any>(() => getStoredUser())
  const [token, setToken] = useState<string | null>(() => getStoredToken())
  const [booting, setBooting] = useState(Boolean(getStoredToken()))

  useEffect(() => {
    let cancelled = false

    async function restore() {
      const storedToken = getStoredToken()

      if (!storedToken) {
        setBooting(false)
        return
      }

      try {
        const profile = await LoginService.me()

        if (cancelled) return

        setUser(profile.data)
        persistSession(storedToken, profile.data)
      } catch (error) {
        if (!cancelled) {
          console.error('Session restoration failed:', error)
        }
      } finally {
        if (!cancelled) {
          setBooting(false)
        }
      }
    }

    restore()

    return () => {
      cancelled = true
    }
  }, [])

  const value = useMemo<AuthContextType>(() => {
    const applyAuth = (payload: { token: string; user: any }) => {
      persistSession(payload.token, payload.user)
      setToken(payload.token)
      setUser(payload.user)
    }

    return {
      user,
      token,
      booting,

      isAuthenticated: Boolean(token && user),

      login: async (username, password) => {
        const payload = await LoginService.login(username, password)
        applyAuth(payload.data)
      },

      register: async (form) => {
        const payload = await LoginService.register(form)

        if (payload.data?.token) {
          applyAuth(payload.data)
        }
      },

      logout: () => {
        clearSession()
        setToken(null)
        setUser(null)
      },

      expireSession: () => {
        clearSession()
        setToken(null)
        setUser(null)
      }
    }
  }, [token, user, booting])

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  )
}
