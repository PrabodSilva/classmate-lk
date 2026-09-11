// Shared login helpers used by several pages.

// Reads the saved login ticket (token).
// Returns { name, email, role } or null if not logged in or expired.
export function getLoggedInUser() {
  const token = localStorage.getItem('token')
  if (!token || token === 'undefined') return null

  try {
    // The middle part of the token holds the email, role and expiry time
    const middle = token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')
    const payload = JSON.parse(atob(middle))

    if (payload.exp && payload.exp * 1000 < Date.now()) {
      logout()
      return null
    }

    const savedName = localStorage.getItem('name')
    const name = savedName && savedName !== 'undefined' && savedName !== 'null'
      ? savedName
      : payload.sub

    return { name, email: payload.sub, role: (payload.role || '').toUpperCase() }
  } catch (e) {
    return null
  }
}

export function isTeacher(user) {
  return !!user && user.role === 'TEACHER'
}

export function isStudent(user) {
  return !!user && user.role === 'STUDENT'
}

// Adds the login token to a request, if we have one
export function authHeaders() {
  const token = localStorage.getItem('token')
  return token ? { Authorization: 'Bearer ' + token } : {}
}

export function logout() {
  localStorage.removeItem('token')
  localStorage.removeItem('name')
}