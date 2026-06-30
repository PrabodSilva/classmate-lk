import { useState } from 'react'
import { useNavigate } from 'react-router-dom'

export default function Login() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const navigate = useNavigate()

  const handleLogin = (e) => {
    e.preventDefault()
    // TODO (Sprint 2): call the real backend here
    console.log('login attempt:', email, password)
    navigate('/classes')   // for now, just go to the classes page
  }

  return (
    <div>
      <h3>Student / Teacher Login</h3>
      <form onSubmit={handleLogin} style={{ display:'grid', gap:10, maxWidth:320 }}>
        <input placeholder="Email" value={email} onChange={e=>setEmail(e.target.value)} />
        <input placeholder="Password" type="password" value={password}
               onChange={e=>setPassword(e.target.value)} />
        <button type="submit">Login</button>
      </form>
    </div>
  )
}