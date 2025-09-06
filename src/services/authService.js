const BASE_URL = 'http://localhost:8080/api/v1/auth'

export async function login(username, password) {
    const res = await fetch(`${BASE_URL}/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password })
    })
    if (!res.ok) throw new Error('Login failed')
    return res.text() // token
}
