import { useState } from 'react'
import api from '../api'

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
      <h3>Best Value Recommendations</h3>
      <p>We compare every matching class against the market average and pick the best value.</p>

      <div className="search-row">
        <input
          placeholder="Subject (e.g. Mathematics)"
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
            {data.totalClassesFound} classes found · Average fee: Rs. {data.averageFee}
            {' '}· Range: Rs. {data.lowestFee} – Rs. {data.highestFee}
          </div>

          <div className="card-list">
            {recs.map(r => (
              <div key={r.classPost.id} className="card card-recommended">
                <div className="rank-row">
                  <span className="rank">#{r.rank}</span>
                  <span className="score">Value score: {r.valueScore}/100</span>
                </div>
                <strong>{r.classPost.subject}</strong> — {r.classPost.teacherName}<br />
                <small>
                  {r.classPost.grade} · {r.classPost.district} · {r.classPost.mode}
                  {r.classPost.place ? ` · ${r.classPost.place}` : ''} · Rs. {r.classPost.fee}
                </small>
                <p className="reason">{r.reason}</p>
              </div>
            ))}
          </div>
        </>
      )}
    </div>
  )
}