import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../api'

export default function Register() {
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [role, setRole] = useState('STUDENT')
  const [message, setMessage] = useState('')
  const navigate = useNavigate()

  const handleRegister = async (e) => {
    e.preventDefault()
    setMessage('')
    try {
      await api.post('/auth/register', { name, email, password, role })
      setMessage('Registered successfully! Redirecting to login...')
      setTimeout(() => navigate('/'), 1500)
    } catch (err) {
      setMessage('Registration failed. Email may already be used.')
    }
  }

  return (
    <div>
      <h3>Create an Account</h3>
      <form onSubmit={handleRegister} style={{ display:'grid', gap:10, maxWidth:320 }}>
        <input placeholder="Full Name" value={name} onChange={e=>setName(e.target.value)} />
        <input placeholder="Email" value={email} onChange={e=>setEmail(e.target.value)} />
        <input placeholder="Password" type="password" value={password}
               onChange={e=>setPassword(e.target.value)} />
        <select value={role} onChange={e=>setRole(e.target.value)}>
          <option value="STUDENT">Student</option>
          <option value="TEACHER">Teacher</option>
        </select>
        <button type="submit">Register</button>
        {message && <p style={{ color: message.includes('success') ? 'green' : 'red' }}>{message}</p>}
      </form>
    </div>
  )
}