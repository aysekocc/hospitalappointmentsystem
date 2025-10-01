<template>
  <div class="register-page">
    <div class="overlay"></div>
    <div class="register-container">
      <h2>Register</h2>
      <form @submit.prevent="register">
        <input v-model="identity" placeholder="Identity" required />
        <input v-model="name" placeholder="Name" required />
        <input v-model="surname" placeholder="Surname" required />
        <input v-model="age" type="number" placeholder="Age" required />

        <select v-model="gender" required>
          <option disabled value="">Select Gender</option>
          <option value="MALE">Male</option>
          <option value="FEMALE">Female</option>
        </select>

        <input v-model="username" placeholder="Username" required />
        <input v-model="password" type="password" placeholder="Password" required />

        <select v-model="role" required>
          <option disabled value="">Select Role</option>
          <option value="USER">User</option>
          <option value="DOCTOR">Doctor</option>
        </select>

        <button type="submit">Register</button>
      </form>

      <p>{{ message }}</p>


      <button v-if="registrationSuccess" class="login-btn" @click="goToLogin">
        Giriş Yap
      </button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      identity: '',
      name: '',
      surname: '',
      age: null,
      gender: '',
      username: '',
      password: '',
      role: '',
      message: '',
      registrationSuccess: false,
    };
  },
  methods: {
    async register() {
      try {
        await axios.post(
          'http://localhost:8082/api/v1/auth/register',
          {
            identity: this.identity,
            name: this.name,
            surname: this.surname,
            age: this.age,
            gender: this.gender,
            username: this.username,
            password: this.password,
            role: this.role
          }
        );

        this.message = 'Registration successful!';
        this.registrationSuccess = true;
      } catch (err) {
        console.error(err.response?.data);
        this.message = 'Registration failed! ' + (err.response?.data?.message || '');
        this.registrationSuccess = false;
      }
    },
    goToLogin() {
      this.$router.push('/login');
    }
  }
};
</script>

<style scoped>
.register-page {
  position: relative;
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: url('https://cdn-icons-png.flaticon.com/512/2966/2966506.png') no-repeat center center/cover;
  font-family: Arial, sans-serif;
  color: white;
}

.overlay {
  position: absolute;
  top: 0; left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0,0,0,0.5);
  z-index: 1;
}

.register-container {
  position: relative;
  z-index: 2;
  background: rgba(255, 255, 255, 0.1);
  padding: 30px 40px;
  border-radius: 12px;
  text-align: center;
  backdrop-filter: blur(5px);
  box-shadow: 0 4px 15px rgba(0,0,0,0.3);
}

.register-container h2 {
  margin-bottom: 20px;
  text-shadow: 1px 1px 5px rgba(0,0,0,0.7);
}

.register-container input,
.register-container select {
  width: 100%;
  padding: 10px;
  margin: 8px 0;
  border-radius: 6px;
  border: none;
}

.register-container button {
  width: 100%;
  padding: 12px;
  margin-top: 10px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  background: #007BFF;
  color: white;
  font-weight: bold;
  transition: all 0.3s ease;
}

.register-container button:hover {
  background: #0056b3;
  transform: scale(1.05);
}

.register-container p {
  margin-top: 10px;
  color: #ffdddd;
}

.login-btn {
  margin-top: 15px;
  background: #28a745;
}

.login-btn:hover {
  background: #1e7e34;
}
</style>
