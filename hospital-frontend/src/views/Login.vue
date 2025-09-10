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
      message: '',
      userId: null
    };
  },
  methods: {
    async login() {
      try {
        const res = await axios.post('/api/v1/auth/login', {   // path’i backend’e göre güncelle
          username: this.username,
          password: this.password
        });

        localStorage.setItem("token", res.data.token);

        // Eğer backend ayrıca rol döndürüyorsa onu da sakla:
        localStorage.setItem("role", res.data.role);


        localStorage.setItem('token', res.data.token);
        localStorage.setItem("role", res.data.role);

        localStorage.setItem('userId', res.data.userId);
        this.$router.push("/appointments/my-appointments");

        this.message = 'Login successful!';
        if(res.data.role==="ROLE_DOCTOR"){
          this.$router.push('/prescriptions');
        }else if(res.data.role==="ROLE_USER") {
          console.log(res.data.token)
          console.log(res.data.role)
          this.$router.push('/prescriptions-user');
        }

      } catch (err) {
        console.error(err);
        this.message = err.response?.data?.message || 'Login failed!';
      }
    }
  }
};
</script>
