import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import api from '../api'
import Stars from '../Stars.jsx'

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
      <p>Search for tuition classes by subject and grade. Click a class to see details and rate it.</p>

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
          <Link key={c.id} to={`/classes/${c.id}`} className="card card-link">
            <strong>{c.subject}</strong> &mdash; {c.teacherName}<br />
            <small>
              {c.grade} &middot; {c.district} &middot; {c.mode}
              {c.place && <> &middot; {c.place}</>} &middot; Rs. {c.fee}
            </small>

            <div className="card-rating">
              {c.ratingCount > 0 ? (
                <span>
                  <Stars value={c.averageRating} size={15} /> {c.averageRating.toFixed(1)} ({c.ratingCount})
                </span>
              ) : (
                <span>No ratings yet</span>
              )}
              <span className="view-link">View &amp; rate &rarr;</span>
            </div>
          </Link>
        ))}
      </div>
    </div>
  )
}