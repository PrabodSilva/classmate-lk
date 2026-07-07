import { Routes, Route, Link } from 'react-router-dom'
import Login from './pages/Login.jsx'
import Register from './pages/Register.jsx'
import Classes from './pages/Classes.jsx'
import Recommend from './pages/Recommend.jsx'

export default function App() {
  return (
    <div style={{ fontFamily: 'Arial, sans-serif', maxWidth: 800, margin: '0 auto', padding: 20 }}>
      <header style={{ display:'flex', gap:16, alignItems:'center',
                       borderBottom:'2px solid #2e75b6', paddingBottom:10, marginBottom:20 }}>
        <h2 style={{ color:'#1f3a5f', margin:0 }}>ClassMate.lk</h2>
        <nav style={{ display:'flex', gap:12 }}>
          <Link to="/">Login</Link>
          <Link to="/register">Register</Link>
          <Link to="/classes">Find Classes</Link>
          <Link to="/recommend">Recommend</Link>
        </nav>
      </header>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/classes" element={<Classes />} />
        <Route path="/recommend" element={<Recommend />} />
      </Routes>
    </div>
  )
}