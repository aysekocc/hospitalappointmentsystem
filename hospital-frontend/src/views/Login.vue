<template>
  <div class="login-page">
    <div class="overlay"></div>
    <div class="login-container">
      <h2>{{ loginTitle }}</h2>
      <form @submit.prevent="login">
        <input v-model="username" placeholder="Username" required />
        <input v-model="password" type="password" placeholder="Password" required />
        <button type="submit">Login</button>
      </form>
      <p>{{ message }}</p>
    </div>
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
      roleParam: this.$route.query.role || null
    };
  },
  computed: {
    loginTitle() {
      if (this.roleParam === "doctor") return "Doktor Girişi";
      if (this.roleParam === "user") return "Hasta Girişi";
      return "Login";
    }
  },
  methods: {
    async login() {
      try {
        const res = await axios.post('/api/v1/auth/login', {
          username: this.username,
          password: this.password
        });

        const role = res.data.role;
        const token = res.data.token;
        const userId = res.data.userId;

        console.log("Token:", token, "Role:", role, "UserId:", userId);

        localStorage.setItem("token", token);
        localStorage.setItem("role", role);
        localStorage.setItem("userId", userId);

        if (this.roleParam && ((this.roleParam === "doctor" && role !== "ROLE_DOCTOR") || (this.roleParam === "user" && role !== "ROLE_USER"))) {
          this.message = "Giriş yetkiniz yok!";
          return;
        }

        if (role === "ROLE_DOCTOR") {
          this.$router.push('/doctor-appointments');
        } else if (role === "ROLE_USER") {
          this.$router.push('/appointments');
        }

      } catch (err) {
        console.error(err);
        this.message = err.response?.data?.message || 'Login failed!';
      }
    }


  }
};
</script>

<style scoped>
/* Sayfa arka planı */
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #6bc1ff, #3a8ee6);
  font-family: Arial, sans-serif;
  position: relative;
  overflow: hidden;
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0,0,0,0.3);
  z-index: 1;
}

.login-container {
  position: relative;
  z-index: 2;
  background: #fff;
  padding: 40px;
  border-radius: 15px;
  box-shadow: 0 10px 25px rgba(0,0,0,0.3);
  width: 320px;
  text-align: center;
}

.login-container h2 {
  margin-bottom: 20px;
  color: #333;
  font-size: 24px;
}

.login-container input {
  width: 100%;
  padding: 12px 15px;
  margin-bottom: 15px;
  border: 1px solid #ccc;
  border-radius: 8px;
  outline: none;
  transition: 0.3s;
}

.login-container input:focus {
  border-color: #3a8ee6;
  box-shadow: 0 0 5px rgba(58, 142, 230, 0.5);
}

.login-container button {
  width: 100%;
  padding: 12px;
  border: none;
  border-radius: 8px;
  background: #3a8ee6;
  color: #fff;
  font-size: 16px;
  cursor: pointer;
  transition: 0.3s;
}

.login-container button:hover {
  background: #6bc1ff;
}

.login-container p {
  margin-top: 15px;
  color: red;
  font-weight: bold;
}
</style>
