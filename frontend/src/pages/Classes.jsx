import { useState, useEffect } from 'react'
import api from '../api'

const GRADES = [
  'Grade 1', 'Grade 2', 'Grade 3', 'Grade 4', 'Grade 5',
  'Grade 6', 'Grade 7', 'Grade 8', 'Grade 9', 'Grade 10',
  'O/L', 'A/L',
  'Scholarship Exam', 'Other / Custom'
]

export default function Classes() {
  const [subject, setSubject] = useState('')
  const [grade, setGrade] = useState('')
  const [classes, setClasses] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    setLoading(true)
    const params = {}
    if (subject) params.subject = subject
    if (grade) params.grade = grade

    api.get('/classes', { params })
      .then(res => setClasses(res.data))
      .catch(() => setClasses([]))
      .finally(() => setLoading(false))
  }, [subject, grade])

  return (
    <div>
      <h3>Find Classes</h3>
      <p>Search for tuition classes by subject and grade.</p>

      <div className="search-row">
        <input
          placeholder="Filter by subject (e.g. Maths)"
          value={subject}
          onChange={e => setSubject(e.target.value)}
        />
        <select value={grade} onChange={e => setGrade(e.target.value)}>
          <option value="">All Grades</option>
          {GRADES.map(g => <option key={g} value={g}>{g}</option>)}
        </select>
      </div>

      {loading && <p>Loading...</p>}
      {!loading && classes.length === 0 && <p>No classes found.</p>}

      <div className="card-list">
        {classes.map(c => (
          <div key={c.id} className="card">
            <strong>{c.subject}</strong> — {c.teacherName}<br />
            <small>
              {c.grade} · {c.district} · {c.mode}
              {c.place ? ` · ${c.place}` : ''} · Rs. {c.fee}
            </small>
          </div>
        ))}
      </div>
    </div>
  )
}