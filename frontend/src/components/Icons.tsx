export function SparkIcon({ className = 'size-5' }) {
  return (
    <svg className={className} viewBox="0 0 24 24" fill="none" aria-hidden="true">
      <path
        d="M12 3.2 13.4 8.6 18.8 10 13.4 11.4 12 16.8 10.6 11.4 5.2 10 10.6 8.6 12 3.2Z"
        stroke="currentColor"
        strokeWidth="1.4"
        strokeLinejoin="round"
      />
      <path d="M18 15.5 18.7 17.6 20.8 18.3 18.7 19 18 21.1 17.3 19 15.2 18.3 17.3 17.6 18 15.5Z" fill="currentColor" />
    </svg>
  )
}

export function UploadIcon({ className = 'size-5' }) {
  return (
    <svg className={className} viewBox="0 0 24 24" fill="none" aria-hidden="true">
      <path d="M12 16V7" stroke="currentColor" strokeWidth="1.6" strokeLinecap="round" />
      <path d="M8.5 10.5 12 7l3.5 3.5" stroke="currentColor" strokeWidth="1.6" strokeLinecap="round" strokeLinejoin="round" />
      <path d="M6 17.5v1.2A1.3 1.3 0 0 0 7.3 20h9.4a1.3 1.3 0 0 0 1.3-1.3v-1.2" stroke="currentColor" strokeWidth="1.6" strokeLinecap="round" />
    </svg>
  )
}

export function NoteIcon({ className = 'size-5' }) {
  return (
    <svg className={className} viewBox="0 0 24 24" fill="none" aria-hidden="true">
      <path d="M7 4.8h7.2L18 8.6V19.2a1.2 1.2 0 0 1-1.2 1.2H7.2A1.2 1.2 0 0 1 6 19.2V6A1.2 1.2 0 0 1 7.2 4.8Z" stroke="currentColor" strokeWidth="1.5" />
      <path d="M14 4.8V8.4h3.8" stroke="currentColor" strokeWidth="1.5" />
      <path d="M8.5 12.2h7M8.5 15.4h5" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" />
    </svg>
  )
}

export function SendIcon({ className = 'size-4' }) {
  return (
    <svg className={className} viewBox="0 0 24 24" fill="none" aria-hidden="true">
      <path d="M4.4 12 19 5.2 14.2 19l-2.7-5.2L4.4 12Z" stroke="currentColor" strokeWidth="1.6" strokeLinejoin="round" />
    </svg>
  )
}

export function TrashIcon({ className = 'size-4' }) {
  return (
    <svg className={className} viewBox="0 0 24 24" fill="none" aria-hidden="true">
      <path d="M5 7.5h14" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" />
      <path d="M9.2 7.4V6.2A1.2 1.2 0 0 1 10.4 5h3.2a1.2 1.2 0 0 1 1.2 1.2v1.2" stroke="currentColor" strokeWidth="1.5" />
      <path d="M8 7.5h8l-.7 11.1a1.2 1.2 0 0 1-1.2 1.1H9.9a1.2 1.2 0 0 1-1.2-1.1L8 7.5Z" stroke="currentColor" strokeWidth="1.5" />
    </svg>
  )
}

export function FileIcon({ className = 'size-4' }) {
  return (
    <svg className={className} viewBox="0 0 24 24" fill="none" aria-hidden="true">
      <path d="M7.2 4.5h6.3L17.8 8.8V19a1.2 1.2 0 0 1-1.2 1.2H7.2A1.2 1.2 0 0 1 6 19V5.7A1.2 1.2 0 0 1 7.2 4.5Z" stroke="currentColor" strokeWidth="1.5" />
    </svg>
  )
}
