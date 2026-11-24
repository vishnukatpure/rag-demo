import { useCallback, useEffect, useMemo, useRef, useState } from 'react'
import { documentService } from '../api/document.service'
import { ragService } from '../api/rag.service'
import { useAuth } from '../auth/useAuth'
import { FileIcon, NoteIcon, SendIcon, SparkIcon, TrashIcon, UploadIcon } from '../components/Icons'
import type { FormEvent } from 'react';
import { downloadFile } from '../api/server'

const SUGGESTIONS = [
  'Summarize the most important points in my knowledge base.',
  'What constraints or requirements are mentioned?',
  'List open questions I should follow up on.',
]


interface Source {
  id: string;
  fileEntity?: {
    fileId?: string;
    fileName?: string;
    fileSize?: number;
  } | null;
  textEntity?: {
    textData?: string;
  } | null;
}

interface ChatMessage {
  role: 'user' | 'assistant';
  content: string;
  error?: boolean;
}

interface ApiError {
  status?: number;
  message?: string;
  response?: {
    status?: number;
    data?: {
      message?: string;
    };
  };
}

interface SourceItem {
  id: string;
  fileEntity?: {
    fileId?: string;
    fileName?: string;
    fileSize?: number;
  } | null;

  textEntity?: {
    textData?: string;
  } | null;
}

interface SourceCardProps {
  item: SourceItem;
  onDelete: () => void;
  onDownload: () => void;
}

export default function Workspace() {
  const { user, logout, expireSession } = useAuth()
  const [sources, setSources] = useState<Source[]>([]);
  const [loadingSources, setLoadingSources] = useState(true);
  const [sourceError, setSourceError] = useState('');

  const [note, setNote] = useState('');
  const [uploading, setUploading] = useState(false);
  const [savingNote, setSavingNote] = useState(false);
  const [dragOver, setDragOver] = useState(false);

  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [question, setQuestion] = useState('');
  const [asking, setAsking] = useState(false);
  const [mobileKnowledge, setMobileKnowledge] = useState(false);

  const fileInputRef = useRef<HTMLInputElement>(null);
  const chatEndRef = useRef<HTMLDivElement>(null);

  const handleApiError = useCallback(
    (error: unknown): string => {
      const apiError = error as ApiError;

      const status = apiError.response?.status ?? apiError.status;

      if (status === 401) {
        expireSession();
      }

      return (
        apiError.response?.data?.message ||
        apiError.message ||
        'Request failed.'
      );
    },
    [expireSession],
  );

  const loadSources = useCallback(async () => {
    setSourceError('')
    try {
      const page = await documentService.list(0, 50)
      setSources(page.data?.content || [])
    } catch (error) {
      setSourceError(handleApiError(error))
    } finally {
      setLoadingSources(false)
    }
  }, [handleApiError])

  useEffect(() => {
    loadSources()
  }, [loadSources])

  useEffect(() => {
    chatEndRef.current?.scrollIntoView({ behavior: 'smooth' })
  }, [messages, asking])

  const sourceCount = sources.length
  const initials = useMemo(() => {
    const first = user?.firstName?.[0] || user?.email?.[0] || 'U'
    const last = user?.lastName?.[0] || ''
    return `${first}${last}`.toUpperCase()
  }, [user])

  async function ingestFile(file?: File) {
    if (!file) return;

    setUploading(true);
    setSourceError('');

    try {
      await documentService.upload(file);
      await loadSources();
    } catch (error: unknown) {
      setSourceError(handleApiError(error));
    } finally {
      setUploading(false);
    }
  }

  async function saveNote(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    if (!note.trim()) return;

    setSavingNote(true);
    setSourceError('');

    try {
      var data = { info: note.trim() };
      await documentService.addText(data);
      setNote('');
      await loadSources();
    } catch (error: unknown) {
      setSourceError(handleApiError(error));
    } finally {
      setSavingNote(false);
    }
  }

  async function removeSource(id: string) {
    setSourceError('');

    try {
      await documentService.remove(id);

      setSources((current) =>
        current.filter((item) => item.id !== id)
      );
    } catch (error: unknown) {
      setSourceError(handleApiError(error));
    }
  }

  async function ask(nextQuestion: string) {
    const trimmed = nextQuestion.trim();

    if (!trimmed || asking) return;

    setQuestion('');

    setMessages((current) => [
      ...current,
      { role: 'user', content: trimmed },
    ]);

    setAsking(true);

    try {
      const response = await ragService.ask(trimmed);

      setMessages((current) => [
        ...current,
        {
          role: 'assistant',
          content: response.data?.answer || 'No answer was returned.',
        },
      ]);
    } catch (error: unknown) {
      setMessages((current) => [
        ...current,
        {
          role: 'assistant',
          content: handleApiError(error),
          error: true,
        },
      ]);
    } finally {
      setAsking(false);
    }
  }

  return (
    <div className="relative min-h-svh overflow-hidden grain">
      <div className="glow-orb pointer-events-none absolute left-[-10%] top-[-20%] h-[26rem] w-[26rem]" />
      <div className="glow-orb-teal pointer-events-none absolute bottom-[-18%] right-[-8%] h-[28rem] w-[28rem]" />

      <div className="relative mx-auto flex min-h-svh max-w-[1440px] flex-col px-3 py-3 sm:px-4 lg:px-6">
        <header className="mb-3 flex items-center justify-between rounded-[24px] border border-[var(--color-line)] bg-[rgba(17,19,26,0.72)] px-4 py-3 backdrop-blur-xl">
          <div className="flex items-center gap-3">
            <div className="flex size-10 items-center justify-center rounded-2xl bg-[var(--color-accent)] text-[var(--color-ink)]">
              <SparkIcon />
            </div>
            <div>
              <div className="font-serif text-lg leading-none"></div>
              <div className="mt-1 text-xs text-[var(--color-muted)]">RAG workspace</div>
            </div>
          </div>
          <div className="flex items-center gap-3">
            <button
              type="button"
              className="rounded-full border border-[var(--color-line)] px-3 py-1.5 text-xs text-[var(--color-paper)] md:hidden"
              onClick={() => setMobileKnowledge((open) => !open)}
            >
              Knowledge
            </button>
            <div className="hidden text-right text-sm sm:block">
              <div className="text-[var(--color-paper)]">
                {user?.firstName} {user?.lastName}
              </div>
              <div className="text-xs text-[var(--color-muted)]">{user?.email}</div>
            </div>
            <div className="flex size-10 items-center justify-center rounded-full bg-white/10 text-sm">{initials}</div>
            <button
              type="button"
              onClick={logout}
              className="rounded-full border border-[var(--color-line)] px-3 py-1.5 text-xs text-[var(--color-muted)] hover:text-[var(--color-paper)]"
            >
              Sign out
            </button>
          </div>
        </header>

        <div className="grid min-h-0 flex-1 gap-3 md:grid-cols-[320px_minmax(0,1fr)]">
          <aside
            className={`${mobileKnowledge ? 'flex' : 'hidden'
              } min-h-0 flex-col rounded-[28px] border border-[var(--color-line)] bg-[rgba(17,19,26,0.82)] p-4 backdrop-blur-xl md:flex`}
          >
            <div className="mb-4 flex items-end justify-between">
              <div>
                <h2 className="font-serif text-2xl">Knowledge</h2>
                <p className="mt-1 text-xs text-[var(--color-muted)]">{sourceCount} sources in the vector index</p>
              </div>
            </div>

            <label
              onDragOver={(event) => {
                event.preventDefault()
                setDragOver(true)
              }}
              onDragLeave={() => setDragOver(false)}
              onDrop={(event) => {
                event.preventDefault()
                setDragOver(false)
                ingestFile(event.dataTransfer.files?.[0])
              }}
              className={`mb-4 flex cursor-pointer flex-col items-center rounded-3xl border border-dashed px-4 py-6 text-center transition ${dragOver
                ? 'border-[var(--color-accent)] bg-[var(--color-accent)]/10'
                : 'border-[var(--color-line)] bg-black/20'
                }`}
            >
              <UploadIcon className="mb-2 size-6 text-[var(--color-accent)]" />
              <div className="text-sm">Drop a file or browse</div>
              <div className="mt-1 text-xs text-[var(--color-muted)]">PDF, TXT, DOCX, MD · 20MB max</div>
              <input
                ref={fileInputRef}
                type="file"
                accept=".pdf,.txt,.docx,.md"
                className="hidden"
                onChange={(event) => {
                  ingestFile(event.target.files?.[0])
                  event.target.value = ''
                }}
              />
              <span className="mt-3 rounded-full bg-white/10 px-3 py-1 text-xs">
                {uploading ? 'Embedding…' : 'Choose file'}
              </span>
            </label>

            <form onSubmit={saveNote} className="mb-4">
              <div className="mb-2 flex items-center gap-2 text-xs uppercase tracking-[0.18em] text-[var(--color-muted)]">
                <NoteIcon className="size-4" />
                Paste context
              </div>
              <textarea
                value={note}
                onChange={(event) => setNote(event.target.value)}
                rows={4}
                placeholder="Policies, product notes, meeting minutes…"
                className="w-full resize-none rounded-3xl border border-[var(--color-line)] bg-black/20 px-4 py-3 text-sm outline-none focus:border-[var(--color-accent)]"
              />
              <button
                type="submit"
                disabled={savingNote || !note.trim()}
                className="mt-2 w-full rounded-full bg-[var(--color-paper)] px-4 py-2 text-sm text-[var(--color-ink)] disabled:opacity-50"
              >
                {savingNote ? 'Indexing…' : 'Add to vector store'}
              </button>
            </form>

            {sourceError && (
              <p className="mb-3 rounded-2xl border border-red-400/20 bg-red-400/10 px-3 py-2 text-xs text-red-200">
                {sourceError}
              </p>
            )}

            <div className="min-h-0 flex-1 overflow-y-auto scrollbar-thin pr-1">
              {loadingSources && <p className="text-sm text-[var(--color-muted)]">Loading sources…</p>}
              {!loadingSources && sources.length === 0 && (
                <p className="rounded-2xl bg-white/5 px-3 py-4 text-sm text-[var(--color-muted)]">
                  No context yet. Upload a document or add a note to start chatting against your own data.
                </p>
              )}
              <ul className="space-y-2">
                {sources.map((item) => (
                  <SourceCard
                    key={item.id}
                    item={item}
                    onDelete={() => removeSource(item.id)}
                    onDownload={() =>
                      item.fileEntity?.fileId && downloadFile(item.fileEntity.fileId, item.fileEntity.fileName)
                    }
                  />
                ))}
              </ul>
            </div>
          </aside>

          <section className="flex min-h-[70vh] flex-col rounded-[28px] border border-[var(--color-line)] bg-[rgba(17,19,26,0.55)] backdrop-blur-xl">
            <div className="flex-1 overflow-y-auto px-4 py-6 scrollbar-thin sm:px-8">
              {messages.length === 0 && !asking && (
                <div className="mx-auto flex h-full max-w-2xl flex-col justify-center">
                  <p className="text-xs uppercase tracking-[0.22em] text-[var(--color-accent-2)]">Ask with retrieval</p>
                  <h2 className="mt-3 font-serif text-4xl leading-tight">A quieter place to think with your files.</h2>
                  <p className="mt-4 max-w-lg text-[var(--color-muted)]">
                    Answers are generated from chunks retrieved from your Elasticsearch index. Add knowledge first for the
                    most grounded replies.
                  </p>
                  <div className="mt-8 grid gap-2">
                    {SUGGESTIONS.map((item) => (
                      <button
                        key={item}
                        type="button"
                        onClick={() => ask(item)}
                        className="rounded-2xl border border-[var(--color-line)] bg-black/20 px-4 py-3 text-left text-sm hover:border-[var(--color-accent)]"
                      >
                        {item}
                      </button>
                    ))}
                  </div>
                </div>
              )}

              <div className="mx-auto max-w-3xl space-y-5">
                {messages.map((message, index) => (
                  <article
                    key={`${message.role}-${index}`}
                    className={`max-w-[90%] rounded-[24px] px-4 py-3 text-sm leading-6 ${message.role === 'user'
                      ? 'ml-auto bg-[var(--color-paper)] text-[var(--color-ink)]'
                      : message.error
                        ? 'border border-red-400/20 bg-red-400/10 text-red-100'
                        : 'border border-[var(--color-line)] bg-black/25 text-[var(--color-paper)]'
                      }`}
                  >
                    {message.role === 'assistant' && (
                      <div className="mb-1 text-[10px] uppercase tracking-[0.2em] text-[var(--color-accent)]">
                        Retrieved answer
                      </div>
                    )}
                    <p className="whitespace-pre-wrap">{message.content}</p>
                  </article>
                ))}
                {asking && (
                  <article className="max-w-[90%] rounded-[24px] border border-[var(--color-line)] bg-black/25 px-4 py-3 text-sm text-[var(--color-muted)]">
                    Searching your index…
                  </article>
                )}
                <div ref={chatEndRef} />
              </div>
            </div>

            <form
              className="border-t border-[var(--color-line)] p-4"
              onSubmit={(event) => {
                event.preventDefault()
                ask(question)
              }}
            >
              <div className="mx-auto flex max-w-3xl items-end gap-2 rounded-[28px] border border-[var(--color-line)] bg-black/30 p-2">
                <textarea
                  value={question}
                  rows={1}
                  onChange={(event) => setQuestion(event.target.value)}
                  onKeyDown={(event) => {
                    if (event.key === 'Enter' && !event.shiftKey) {
                      event.preventDefault()
                      ask(question)
                    }
                  }}
                  placeholder="Ask anything grounded in your uploaded context…"
                  className="max-h-40 min-h-12 flex-1 resize-none bg-transparent px-3 py-3 text-sm outline-none"
                />
                <button
                  type="submit"
                  disabled={asking || !question.trim()}
                  className="mb-1 mr-1 flex size-11 items-center justify-center rounded-full bg-[var(--color-accent)] text-[var(--color-ink)] disabled:opacity-40"
                  aria-label="Send"
                >
                  <SendIcon />
                </button>
              </div>
            </form>
          </section>
        </div>
      </div>
    </div>
  )
}

function SourceCard({
  item,
  onDelete,
  onDownload,
}: SourceCardProps) {
  const file = item.fileEntity;
  const text = item.textEntity;

  const isFile = Boolean(file);

  const title = isFile
    ? file?.fileName || 'Uploaded file'
    : 'Pasted note';

  const preview = text?.textData
    ? text.textData.slice(0, 90)
    : formatBytes(file?.fileSize);

  return (
    <li className="rounded-2xl border border-[var(--color-line)] bg-black/20 p-3">
      <div className="flex items-start justify-between gap-2">
        <div className="flex min-w-0 items-start gap-2">
          <div className="mt-0.5 text-[var(--color-accent)]">
            {isFile ? <FileIcon /> : <NoteIcon className="size-4" />}
          </div>

          <div className="min-w-0">
            <div className="truncate text-sm">{title}</div>

            <div className="mt-0.5 line-clamp-2 text-xs text-[var(--color-muted)]">
              {preview}
            </div>
          </div>
        </div>

        <div className="flex shrink-0 items-center gap-1">
          {isFile && (
            <button
              type="button"
              onClick={onDownload}
              className="rounded-full px-2 py-1 text-[11px] text-[var(--color-muted)] hover:text-[var(--color-paper)]"
            >
              Download
            </button>
          )}

          <button
            type="button"
            onClick={onDelete}
            className="rounded-full p-1.5 text-[var(--color-muted)] hover:text-red-400"
            aria-label="Delete source"
          >
            <TrashIcon />
          </button>
        </div>
      </div>
    </li>
  );
}

function formatBytes(bytes: any) {
  if (!bytes) return 'File'
  if (bytes < 1024) return `${bytes} B`
  if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(1)} KB`
  return `${(bytes / (1024 * 1024)).toFixed(1)} MB`
}
