<template>
  <div>
    <Navbar />
    <h1>Dashboard</h1>

    <h2>Users</h2>
    <UserCard v-for="user in users" :key="user.id" :user="user" />

    <h2>Appointments</h2>
    <AppointmentCard v-for="app in appointments.content" :key="app.id" :appointment="app" />
  </div>
</template>

<script>
import Navbar from '../components/Navbar.vue'
import UserCard from '../components/UserCard.vue'
import AppointmentCard from '../components/AppointmentCard.vue'
import { getUsers } from '../services/userService.js'
import { getAppointments } from '../services/appointmentService.js'

export default {
  components: { Navbar, UserCard, AppointmentCard },
  data() {
    return { users: [], appointments: { content: [] } }
  },
  async created() {
    this.users = await getUsers()
    this.appointments = await getAppointments()
  }
}
</script>
