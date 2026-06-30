import { useState } from 'react'

// Fake data for now. In Sprint 2 this will come from the real backend.
const MOCK = [
  { id:1, subject:'Combined Maths', teacher:'Mr. Perera',     district:'Colombo', mode:'Mass',       fee:2000 },
  { id:2, subject:'Physics',        teacher:'Ms. Silva',      district:'Kandy',   mode:'Individual', fee:3500 },
  { id:3, subject:'Combined Maths', teacher:'Mr. Fernando',   district:'Galle',   mode:'Online',     fee:1500 },
  { id:4, subject:'Chemistry',      teacher:'Mr. Jayasuriya', district:'Colombo', mode:'Mass',       fee:2500 },
]

export default function Classes() {
  const [subject, setSubject] = useState('')
  const filtered = MOCK.filter(c =>
    subject === '' || c.subject.toLowerCase().includes(subject.toLowerCase())
  )

  return (
    <div>
      <h3>Find Classes</h3>
      <input
        placeholder="Filter by subject (e.g. Maths)"
        value={subject}
        onChange={e=>setSubject(e.target.value)}
        style={{ padding:8, width:280, marginBottom:16 }}
      />
      <div style={{ display:'grid', gap:12 }}>
        {filtered.map(c => (
          <div key={c.id} style={{ border:'1px solid #ccc', borderRadius:8, padding:12 }}>
            <strong>{c.subject}</strong> — {c.teacher}<br/>
            <small>{c.district} · {c.mode} · Rs. {c.fee}</small>
          </div>
        ))}
        {filtered.length === 0 && <p>No classes match your filter.</p>}
      </div>
    </div>
  )
}