import { Routes, Route, NavLink } from 'react-router-dom'
import Login from './pages/Login.jsx'
import Register from './pages/Register.jsx'
import Classes from './pages/Classes.jsx'
import Recommend from './pages/Recommend.jsx'
import AddClass from './pages/AddClass.jsx'

export default function App() {
  return (
    <div className="app">
      <header className="site-header">
        <h2 className="brand">
          ClassMate<span className="brand-dot">.lk</span>
        </h2>
        <nav>
          <NavLink to="/" end>Login</NavLink>
          <NavLink to="/register">Register</NavLink>
          <NavLink to="/classes">Find Classes</NavLink>
          <NavLink to="/recommend">Recommend</NavLink>
          <NavLink to="/add-class" className="nav-cta">Add Class</NavLink>
        </nav>
      </header>

      <main className="page">
        <Routes>
          <Route path="/" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/classes" element={<Classes />} />
          <Route path="/recommend" element={<Recommend />} />
          <Route path="/add-class" element={<AddClass />} />
        </Routes>
      </main>
    </div>
  )
}