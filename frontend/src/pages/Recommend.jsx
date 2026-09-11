import { useState } from 'react'
import { Link } from 'react-router-dom'
import api from '../api'
import Stars from '../Stars.jsx'

const GRADES = [
  'Grade 1', 'Grade 2', 'Grade 3', 'Grade 4', 'Grade 5',
  'Grade 6', 'Grade 7', 'Grade 8', 'Grade 9', 'Grade 10',
  'O/L', 'A/L',
  'Scholarship Exam', 'Other / Custom'
]

export default function Recommend() {
  const [subject, setSubject] = useState('')
  const [grade, setGrade] = useState('')
  const [data, setData] = useState(null)
  const [searched, setSearched] = useState(false)
  const [loading, setLoading] = useState(false)

  const getRecommendations = async () => {
    if (!subject) return
    setLoading(true)
    try {
      const params = { subject }
      if (grade) params.grade = grade
      const res = await api.get('/recommendations', { params })
      setData(res.data)
    } catch (err) {
      setData(null)
    } finally {
      setSearched(true)
      setLoading(false)
    }
  }

  const recs = data && data.recommendations ? data.recommendations : []

  return (
    <div>
      <h3>Best Class Recommendations</h3>
      <p>
        We score every matching class using student ratings (60%) and price value (40%),
        then pick the top 3.
      </p>

      <div className="search-row">
        <input
          placeholder="Subject (e.g. Physics)"
          value={subject}
          onChange={e => setSubject(e.target.value)}
        />
        <select value={grade} onChange={e => setGrade(e.target.value)}>
          <option value="">All Grades</option>
          {GRADES.map(g => <option key={g} value={g}>{g}</option>)}
        </select>
      </div>

      <div className="search-row">
        <button onClick={getRecommendations} disabled={loading}>
          {loading ? 'Analysing...' : 'Get Recommendations'}
        </button>
      </div>

      {searched && recs.length === 0 && <p>No classes found for that search.</p>}

      {recs.length > 0 && (
        <>
          <div className="market-box">
            <strong>Market analysis</strong><br />
            {data.totalClassesFound} {data.totalClassesFound === 1 ? 'class' : 'classes'} found &middot; {data.ratedClasses} rated by students<br />
            Average fee: Rs. {data.averageFee} &middot; Range: Rs. {data.lowestFee} &ndash; Rs. {data.highestFee}
          </div>

          <div className="card-list">
            {recs.map(r => (
              <div key={r.classPost.id} className="card card-recommended">
                <div className="rank-row">
                  <span className="rank">#{r.rank}</span>
                  <span className="score">Score: {r.valueScore}/100</span>
                </div>

                <strong>{r.classPost.subject}</strong> &mdash; {r.classPost.teacherName}<br />
                <small>
                  {r.classPost.grade} &middot; {r.classPost.district} &middot; {r.classPost.mode}
                  {r.classPost.place && <> &middot; {r.classPost.place}</>} &middot; Rs. {r.classPost.fee}
                </small>

                <div className="card-rating">
                  {r.classPost.ratingCount > 0 ? (
                    <span>
                      <Stars value={r.classPost.averageRating} size={15} />{' '}
                      {r.classPost.averageRating.toFixed(1)} ({r.classPost.ratingCount})
                    </span>
                  ) : (
                    <span>No ratings yet</span>
                  )}
                  <span>Rating {r.ratingScore} &middot; Price {r.priceScore}</span>
                </div>

                <p className="reason">{r.reason}</p>

                <Link
                  to={`/classes/${r.classPost.id}`}
                  className="view-link"
                  style={{ textDecoration: 'none', fontSize: 13 }}
                >
                  View &amp; rate &rarr;
                </Link>
              </div>
            ))}
          </div>
        </>
      )}
    </div>
  )
}