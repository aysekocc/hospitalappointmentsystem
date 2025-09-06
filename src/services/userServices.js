const BASE_URL = 'http://localhost:8080/api/v1/auth'

export async function getUsers() {
    const token = localStorage.getItem('token')
    const res = await fetch(`${BASE_URL}/userAll`, {
        headers: { 'Authorization': `Bearer ${token}` }
    })
    return res.json()
}
