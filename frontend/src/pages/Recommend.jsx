import { useState } from 'react'
import api from '../api'

export default function Recommend() {
  const [subject, setSubject] = useState('')
  const [results, setResults] = useState([])
  const [searched, setSearched] = useState(false)

  const getRecommendations = async () => {
    try {
      const res = await api.get('/recommendations', { params: { subject } })
      setResults(res.data)
      setSearched(true)
    } catch (err) {
      setResults([])
      setSearched(true)
    }
  }

  return (
    <div>
      <h3>Suggest a Good Class</h3>
      <p>Type a subject and we'll recommend the best classes for you.</p>

      <div className="search-row">
        <input
          placeholder="e.g. Maths"
          value={subject}
          onChange={e => setSubject(e.target.value)}
        />
        <button onClick={getRecommendations}>Get Suggestions</button>
      </div>

      {searched && results.length === 0 && <p>No suggestions found for that subject.</p>}

      <div className="card-list">
        {results.map(c => (
          <div key={c.id} className="card card-recommended">
            <strong>{c.subject}</strong> — {c.teacherName}<br />
            <small>{c.district} · {c.mode} · Rs. {c.fee}</small>
            <span className="badge">★ Recommended</span>
          </div>
        ))}
      </div>
    </div>
  )
}