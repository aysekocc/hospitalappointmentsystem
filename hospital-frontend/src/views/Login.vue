<template>
  <div>
    <h2>Login</h2>
    <form @submit.prevent="login">
      <input v-model="username" placeholder="Username" required />
      <input v-model="password" type="password" placeholder="Password" required />
      <button type="submit">Login</button>
    </form>
    <p>{{ message }}</p>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      username: '',
      password: '',
      message: ''
    };
  },
  methods: {
    async login() {
      try {
        const res = await axios.post('http://localhost:8082/api/v1/auth/login', {
          username: this.username,
          password: this.password
        });
        localStorage.setItem('token', res.data);
        this.message = 'Login successful!';
      } catch (err) {
        this.message = 'Login failed!';
      }
    }
  }
};
</script>
