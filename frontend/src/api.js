import axios from 'axios'
// Every request goes through the API Gateway (port 8080).
// You will use this in Sprint 2 to call the real backend.
const api = axios.create({ baseURL: 'http://localhost:8080' })
export default api