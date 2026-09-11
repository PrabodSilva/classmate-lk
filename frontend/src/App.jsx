import { Routes, Route, NavLink, useLocation, useNavigate } from 'react-router-dom'
import Login from './pages/Login.jsx'
import Register from './pages/Register.jsx'
import Classes from './pages/Classes.jsx'
import ClassDetail from './pages/ClassDetail.jsx'
import Recommend from './pages/Recommend.jsx'
import AddClass from './pages/AddClass.jsx'
import { getLoggedInUser, isTeacher, logout } from './auth'
import './header.css'

export default function App() {
  useLocation()                 // re-check the login every time the page changes
  const navigate = useNavigate()
  const user = getLoggedInUser()

  const handleLogout = () => {
    logout()
    navigate('/')
  }

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
          {isTeacher(user) && (
            <NavLink to="/add-class" className="nav-cta">Add Class</NavLink>
          )}
        </nav>

        <div className="login-status">
          {user ? (
            <>
              <span>
                &#10003; Logged in as <strong>{user.name}</strong>
                {user.role ? ` (${user.role})` : ''}
              </span>
              <button type="button" className="logout-btn" onClick={handleLogout}>Log out</button>
            </>
          ) : (
            <span>Not logged in</span>
          )}
        </div>
      </header>

      <main className="page">
        <Routes>
          <Route path="/" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/classes" element={<Classes />} />
          <Route path="/classes/:id" element={<ClassDetail />} />
          <Route path="/recommend" element={<Recommend />} />
          <Route path="/add-class" element={<AddClass />} />
        </Routes>
      </main>
    </div>
  )
}