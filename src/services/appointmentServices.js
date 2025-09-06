const BASE_URL = 'http://localhost:8080/api/v1/appointment'

export async function getAppointments(pageNumber = 0, pageSize = 10) {
    const token = localStorage.getItem('token')
    const res = await fetch(`${BASE_URL}?pageNumber=${pageNumber}&pageSize=${pageSize}`, {
        headers: { 'Authorization': `Bearer ${token}` }
    })
    return res.json()
}
