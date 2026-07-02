import { useState, useEffect } from 'react'
import api from '../api'

export default function Classes() {
  const [subject, setSubject] = useState('')
  const [classes, setClasses] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    setLoading(true)
    api.get('/classes', { params: subject ? { subject } : {} })
      .then(res => setClasses(res.data))
      .catch(() => setClasses([]))
      .finally(() => setLoading(false))
  }, [subject])

  return (
    <div>
      <h3>Find Classes</h3>
      <input
        placeholder="Filter by subject (e.g. Maths)"
        value={subject}
        onChange={e=>setSubject(e.target.value)}
        style={{ padding:8, width:280, marginBottom:16 }}
      />

      {loading && <p>Loading...</p>}
      {!loading && classes.length === 0 && <p>No classes found.</p>}

      <div style={{ display:'grid', gap:12 }}>
        {classes.map(c => (
          <div key={c.id} style={{ border:'1px solid #ccc', borderRadius:8, padding:12 }}>
            <strong>{c.subject}</strong> — {c.teacherName}<br/>
            <small>{c.district} · {c.mode} · Rs. {c.fee}</small>
          </div>
        ))}
      </div>
    </div>
  )
}