import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../api'

export default function Login() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const navigate = useNavigate()

  const handleLogin = async (e) => {
    e.preventDefault()
    setError('')
    try {
      const res = await api.post('/auth/login', { email, password })
      localStorage.setItem('token', res.data.token)
      localStorage.setItem('name', res.data.name)
      navigate('/classes')
    } catch (err) {
      setError('Invalid email or password')
    }
  }

  return (
    <div>
      <h3>Student / Teacher Login</h3>
      <form onSubmit={handleLogin} style={{ display:'grid', gap:10, maxWidth:320 }}>
        <input placeholder="Email" value={email} onChange={e=>setEmail(e.target.value)} />
        <input placeholder="Password" type="password" value={password}
               onChange={e=>setPassword(e.target.value)} />
        <button type="submit">Login</button>
        {error && <p style={{ color:'red' }}>{error}</p>}
      </form>
    </div>
  )
}