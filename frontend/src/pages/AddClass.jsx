import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../api'

export default function AddClass() {
  const [subject, setSubject] = useState('')
  const [teacherName, setTeacherName] = useState('')
  const [district, setDistrict] = useState('')
  const [mode, setMode] = useState('Individual')
  const [fee, setFee] = useState('')

  const [errors, setErrors] = useState({})   // field errors from backend
  const [message, setMessage] = useState('')
  const [saving, setSaving] = useState(false)
  const navigate = useNavigate()

  const handleAddClass = async (e) => {
    e.preventDefault()
    setErrors({})
    setMessage('')
    setSaving(true)

    try {
      await api.post('/classes', {
        subject,
        teacherName,
        district,
        mode,
        fee: Number(fee)
      })
      setMessage('Class added successfully! Redirecting...')
      setTimeout(() => navigate('/classes'), 1500)
    } catch (err) {
      if (err.response && err.response.status === 400) {
        // Backend sent field-by-field validation errors
        setErrors(err.response.data)
        setMessage('Please fix the errors below.')
      } else {
        setMessage('Could not add class. Is the backend running?')
      }
    } finally {
      setSaving(false)
    }
  }

  const errStyle = { color: 'red', fontSize: 12, margin: 0 }

  return (
    <div>
      <h3>Add a New Class</h3>
      <p style={{ color:'#555', fontSize:14 }}>For teachers: post your tuition class here.</p>

      <form onSubmit={handleAddClass} style={{ display:'grid', gap:10, maxWidth:320 }}>

        <input placeholder="Subject (e.g. Mathematics)" value={subject}
               onChange={e=>setSubject(e.target.value)} />
        {errors.subject && <p style={errStyle}>{errors.subject}</p>}

        <input placeholder="Teacher Name" value={teacherName}
               onChange={e=>setTeacherName(e.target.value)} />
        {errors.teacherName && <p style={errStyle}>{errors.teacherName}</p>}

        <input placeholder="District (e.g. Colombo)" value={district}
               onChange={e=>setDistrict(e.target.value)} />
        {errors.district && <p style={errStyle}>{errors.district}</p>}

        <select value={mode} onChange={e=>setMode(e.target.value)}>
          <option value="Individual">Individual</option>
          <option value="Mass">Mass</option>
          <option value="Online">Online</option>
        </select>
        {errors.mode && <p style={errStyle}>{errors.mode}</p>}

        <input placeholder="Fee (Rs.)" type="number" value={fee}
               onChange={e=>setFee(e.target.value)} />
        {errors.fee && <p style={errStyle}>{errors.fee}</p>}

        <button type="submit" disabled={saving}>
          {saving ? 'Adding...' : 'Add Class'}
        </button>

        {message && (
          <p style={{ color: message.includes('success') ? 'green' : 'red' }}>{message}</p>
        )}
      </form>
    </div>
  )
}