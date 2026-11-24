import { createContext } from 'react'

export interface AuthContextType {
  user: any
  token: string | null
  booting: boolean
  isAuthenticated: boolean
  login: (username: string, password: string) => Promise<void>
  register: (form: { firstName: string, lastName: string, email: string; password: string, sex: string }) => Promise<void>
  logout: () => void
  expireSession: () => void
}

export const AuthContext = createContext<AuthContextType | null>(null)