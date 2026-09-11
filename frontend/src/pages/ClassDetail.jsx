import { useState, useEffect } from 'react'
import { useParams, Link } from 'react-router-dom'
import api from '../api'
import Stars from '../Stars.jsx'

const STAR = '\u2605'
const LABELS = ['', 'Poor', 'Fair', 'Good', 'Very good', 'Excellent']

// Adds the login token (saved by the Login page) to a request, if we have one
function authHeaders() {
  const token = localStorage.getItem('token')
  return token ? { Authorization: 'Bearer ' + token } : {}
}

export default function ClassDetail() {
  const { id } = useParams()

  const [classPost, setClassPost] = useState(null)
  const [loading, setLoading] = useState(true)
  const [loadError, setLoadError] = useState('')

  const [loggedIn, setLoggedIn] = useState(false)
  const [myStars, setMyStars] = useState(0)

  const [selected, setSelected] = useState(0)
  const [hovered, setHovered] = useState(0)
  const [saving, setSaving] = useState(false)
  const [message, setMessage] = useState('')
  const [isError, setIsError] = useState(false)

  // When the page opens: load the class AND "what did I rate?"
  useEffect(() => {
    setLoading(true)
    setLoadError('')
    setMessage('')
    setSelected(0)

    Promise.all([
      api.get(`/classes/${id}`),
      api.get(`/classes/${id}/my-rating`, { headers: authHeaders() })
        .catch(() => ({ data: { loggedIn: false, stars: 0 } }))
    ])
      .then(([classRes, myRes]) => {
        setClassPost(classRes.data)
        setLoggedIn(myRes.data.loggedIn)
        setMyStars(myRes.data.stars)
      })
      .catch(err => {
        if (err.response && err.response.status === 404) {
          setLoadError('Sorry, this class was not found.')
        } else {
          setLoadError('Could not load this class. Is the backend running?')
        }
      })
      .finally(() => setLoading(false))
  }, [id])

  const submitRating = async () => {
    if (selected === 0) return
    setSaving(true)
    setMessage('')

    try {
      const res = await api.post(
        `/classes/${id}/ratings`,
        { stars: selected },
        { headers: authHeaders() }
      )
      setClassPost(res.data)          // backend sends back the NEW average
      setMyStars(selected)
      setIsError(false)
      setMessage('Thank you! Your rating was saved.')
    } catch (err) {
      setIsError(true)
      const status = err.response ? err.response.status : 0

      if (status === 409) {
        setMessage('You have already rated this class.')
        api.get(`/classes/${id}/my-rating`, { headers: authHeaders() })
          .then(r => setMyStars(r.data.stars))
          .catch(() => {})
      } else if (status === 401) {
        setLoggedIn(false)
        setMessage('Your login has expired. Please log in again.')
      } else if (status === 400) {
        setMessage(err.response.data.stars || 'Please choose between 1 and 5 stars.')
      } else {
        setMessage('Could not save your rating. Is the backend running?')
      }
    } finally {
      setSaving(false)
    }
  }

  if (loading) return <p>Loading...</p>

  if (loadError) {
    return (
      <div className="detail">
        <Link to="/classes" className="back-link">&larr; Back to Find Classes</Link>
        <p>{loadError}</p>
      </div>
    )
  }

  const shown = hovered || selected

  return (
    <div className="detail">
      <Link to="/classes" className="back-link">&larr; Back to Find Classes</Link>

      <h3>{classPost.subject}</h3>
      <p>by {classPost.teacherName}</p>

      <div className="card detail-card">
        <div className="detail-row"><span>Grade</span><strong>{classPost.grade}</strong></div>
        <div className="detail-row"><span>District</span><strong>{classPost.district}</strong></div>
        <div className="detail-row"><span>Mode</span><strong>{classPost.mode}</strong></div>
        {classPost.place && (
          <div className="detail-row"><span>Place</span><strong>{classPost.place}</strong></div>
        )}
        <div className="detail-row"><span>Fee</span><strong>Rs. {classPost.fee}</strong></div>
      </div>

      <div className="rating-summary">
        {classPost.ratingCount > 0 ? (
          <>
            <span className="big-number">{classPost.averageRating.toFixed(1)}</span>
            <Stars value={classPost.averageRating} size={26} />
            <small>
              {classPost.ratingCount} {classPost.ratingCount === 1 ? 'rating' : 'ratings'}
            </small>
          </>
        ) : (
          <small>No ratings yet. Be the first to rate this class!</small>
        )}
      </div>

      <div className="rate-box">
        {!loggedIn && (
          <p>Please <Link to="/">log in</Link> to rate this class.</p>
        )}

        {loggedIn && myStars > 0 && (
          <>
            <p>You rated this class</p>
            <Stars value={myStars} size={30} />
          </>
        )}

        {loggedIn && myStars === 0 && (
          <>
            <p>How would you rate this class?</p>

            <div className="stars-picker" onMouseLeave={() => setHovered(0)}>
              {[1, 2, 3, 4, 5].map(n => (
                <button
                  key={n}
                  type="button"
                  className={n <= shown ? 'star-on' : 'star-off'}
                  onMouseEnter={() => setHovered(n)}
                  onClick={() => setSelected(n)}
                  aria-label={`${n} star${n > 1 ? 's' : ''}`}
                >
                  {STAR}
                </button>
              ))}
            </div>

            <p className="star-label">{shown ? LABELS[shown] : 'Tap a star'}</p>

            <button type="button" onClick={submitRating} disabled={selected === 0 || saving}>
              {saving ? 'Saving...' : 'Submit Rating'}
            </button>
          </>
        )}

        {message && <p style={{ color: isError ? 'red' : 'green' }}>{message}</p>}
      </div>
    </div>
  )
}