<template>
  <div>
    <h1>Login</h1>
    <input v-model="username" placeholder="Username" />
    <input v-model="password" type="password" placeholder="Password" />
    <button @click="handleLogin">Login</button>
    <p v-if="error" style="color:red">{{ error }}</p>
  </div>
</template>

<script>
import { login } from '../services/authService.js'
import router from '../router'

export default {
  data() {
    return { username: '', password: '', error: '' }
  },
  methods: {
    async handleLogin() {
      try {
        const token = await login(this.username, this.password)
        localStorage.setItem('token', token)
        router.push('/dashboard')
      } catch (err) {
        this.error = 'Login failed!'
        console.error(err)
      }
    }
  }
}
</script>
