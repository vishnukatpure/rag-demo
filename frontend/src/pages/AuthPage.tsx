
import { useState } from 'react';
import type { FormEvent } from 'react';
import { useAuth } from '../auth/useAuth';
import { SparkIcon } from '../components/Icons';
import { AxiosError } from 'axios';
import { toast } from '../hooks/use-toast';

type AuthMode = 'login' | 'register';

interface RegisterForm {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
}

interface LoginForm {
  username: string;
  password: string;
}

interface FieldProps {
  label: string;
  value: string;
  onChange: (value: string) => void;
  type?: string;
}

interface StatProps {
  label: string;
  value: string;
}

const emptyRegister: RegisterForm = {
  firstName: '',
  lastName: '',
  email: '',
  password: '',
};

export default function AuthPage() {
  const { login, register } = useAuth();

  const [mode, setMode] = useState<AuthMode>('login');

  const [loginForm, setLoginForm] = useState<LoginForm>({
    username: '',
    password: '',
  });

  const [registerForm, setRegisterForm] =
    useState<RegisterForm>(emptyRegister);

  const [error, setError] = useState('');
  const [busy, setBusy] = useState(false);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    setError('');
    setBusy(true);

    try {
      if (mode === 'login') {
        await login(loginForm.username, loginForm.password);
      } else {
        const data = {
          firstName: registerForm.firstName,
          lastName: registerForm.lastName,
          email: registerForm.email,
          password: registerForm.password,
          sex: '',
        };

        await register(data);
      }
    } catch (err: unknown) {
      const message =
        err instanceof AxiosError
          ? err.response?.data?.error
          : 'Invalid username or password.';

      toast({
        title: "Login failed",
        description: message,
        variant: "error",
      });
    } finally {
      setBusy(false);
    }
  }

  return (
    <div className="relative min-h-svh overflow-hidden grain">
      <div className="glow-orb pointer-events-none absolute -left-24 -top-16 h-[28rem] w-[28rem] blur-2xl" />
      <div className="glow-orb-teal pointer-events-none absolute -bottom-24 right-0 h-[32rem] w-[32rem] blur-2xl" />

      <div className="relative mx-auto grid min-h-svh max-w-6xl items-center gap-8 overflow-x-hidden px-5 py-8 xl:grid-cols-[minmax(0,1.15fr)_minmax(360px,440px)] xl:px-8">
        <section className="order-2 max-w-xl xl:order-1">
          <div className="mb-8 inline-flex items-center gap-2 rounded-full border border-[var(--color-line)] bg-white/5 px-3 py-1 text-xs uppercase tracking-[0.22em] text-[var(--color-accent)]">
            <SparkIcon className="size-3.5" />
            Private knowledge studio
          </div>

          <h1 className="font-serif text-4xl leading-[1.08] text-[var(--color-paper)] sm:text-5xl xl:text-6xl">
            Chat with your own documents, not the internet.
          </h1>

          <p className="mt-5 max-w-md text-base leading-7 text-[var(--color-muted)]">
            Upload PDFs, notes, and source text. We embed them in your vector
            index, then answer only from that context.
          </p>

          <div className="mt-10 grid grid-cols-3 gap-3 text-sm text-[var(--color-muted)]">
            <Stat label="Ingest" value="PDF · DOCX · MD" />
            <Stat label="Retrieve" value="Elasticsearch" />
            <Stat label="Generate" value="Ollama RAG" />
          </div>
        </section>

        <section className="order-1 w-full min-w-0 rounded-[28px] border border-[var(--color-line)] bg-[rgba(17,19,26,0.82)] p-6 shadow-[0_30px_80px_rgba(0,0,0,0.35)] backdrop-blur-xl sm:p-8 xl:order-2">
          <div className="mb-6 grid grid-cols-2 rounded-full bg-white/5 p-1">
            {(['login', 'register'] as const).map((item) => (
              <button
                key={item}
                type="button"
                onClick={() => {
                  setMode(item);
                  setError('');
                }}
                className={`rounded-full px-4 py-2 text-sm capitalize transition ${mode === item
                  ? 'bg-[var(--color-paper)] text-[var(--color-ink)]'
                  : 'text-[var(--color-muted)] hover:text-[var(--color-paper)]'
                  }`}
              >
                {item === 'login' ? 'Sign in' : 'Create account'}
              </button>
            ))}
          </div>

          <form className="space-y-4" onSubmit={handleSubmit}>
            {mode === 'register' && (
              <div className="grid gap-3 sm:grid-cols-2">
                <Field
                  label="First name"
                  value={registerForm.firstName}
                  onChange={(value) =>
                    setRegisterForm((current) => ({
                      ...current,
                      firstName: value,
                    }))
                  }
                />

                <Field
                  label="Last name"
                  value={registerForm.lastName}
                  onChange={(value) =>
                    setRegisterForm((current) => ({
                      ...current,
                      lastName: value,
                    }))
                  }
                />
              </div>
            )}

            <Field
              label="Email"
              type="email"
              value={
                mode === 'login'
                  ? loginForm.username
                  : registerForm.email
              }
              onChange={(value) =>
                mode === 'login'
                  ? setLoginForm((current) => ({
                    ...current,
                    username: value,
                  }))
                  : setRegisterForm((current) => ({
                    ...current,
                    email: value,
                  }))
              }
            />

            <Field
              label="Password"
              type="password"
              value={
                mode === 'login'
                  ? loginForm.password
                  : registerForm.password
              }
              onChange={(value) =>
                mode === 'login'
                  ? setLoginForm((current) => ({
                    ...current,
                    password: value,
                  }))
                  : setRegisterForm((current) => ({
                    ...current,
                    password: value,
                  }))
              }
            />

            {error && (
              <p className="rounded-2xl border border-red-400/20 bg-red-400/10 px-3 py-2 text-sm text-red-200">
                {error}
              </p>
            )}

            <button
              type="submit"
              disabled={busy}
              className="mt-2 w-full rounded-full bg-[var(--color-accent)] px-4 py-3 text-sm font-medium text-[var(--color-ink)] transition hover:brightness-110 disabled:opacity-60"
            >
              {busy
                ? 'Working…'
                : mode === 'login'
                  ? 'Enter workspace'
                  : 'Create workspace'}
            </button>
          </form>
        </section>
      </div>
    </div>
  );
}

function Field({
  label,
  value,
  onChange,
  type = 'text',
}: FieldProps) {
  return (
    <label className="block">
      <span className="mb-1.5 block text-xs uppercase tracking-[0.18em] text-[var(--color-muted)]">
        {label}
      </span>

      <input
        type={type}
        value={value}
        required
        onChange={(event) => onChange(event.target.value)}
        className="w-full rounded-2xl border border-[var(--color-line)] bg-black/20 px-4 py-3 text-[var(--color-paper)] outline-none transition placeholder:text-white/25 focus:border-[var(--color-accent)]"
      />
    </label>
  );
}

function Stat({ label, value }: StatProps) {
  return (
    <div className="rounded-2xl border border-[var(--color-line)] bg-white/5 px-3 py-3">
      <div className="text-[10px] uppercase tracking-[0.2em] text-[var(--color-accent)]">
        {label}
      </div>
      <div className="mt-1 text-sm text-[var(--color-paper)]">
        {value}
      </div>
    </div>
  );
}
