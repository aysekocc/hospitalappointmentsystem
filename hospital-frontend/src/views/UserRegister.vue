<template>
  <div class="register-page">
    <div class="overlay"></div>

    <button class="back-btn" @click="$router.push('/')">← Geri Dön</button>

    <div class="register-container">
      <h2>Hasta Kaydı</h2>
      <form @submit.prevent="registerUser">
        <input v-model="identity" placeholder="T.C. Kimlik No" required />
        <input v-model="name" placeholder="Ad" required />
        <input v-model="surname" placeholder="Soyad" required />
        <input type="number" v-model="age" placeholder="Yaş" required />
        <select v-model="gender" required>
          <option disabled value="">Cinsiyet</option>
          <option value="MALE">Erkek</option>
          <option value="FEMALE">Kadın</option>
        </select>
        <input v-model="username" placeholder="Kullanıcı Adı" required />
        <input v-model="password" type="password" placeholder="Şifre" required />

        <button type="submit">Kaydol</button>
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
      message: '',
      registrationSuccess: false,
    };
  },
  methods: {
    async registerUser() {
      try {
        await axios.post('/api/v1/auth/register/user', {
          identity: this.identity,
          name: this.name,
          surname: this.surname,
          age: this.age,
          gender: this.gender,
          username: this.username,
          password: this.password,
          role: 'ROLE_USER'
        });
        this.message = 'Kayıt başarılı!';
        this.registrationSuccess = true;
      } catch (err) {
        console.error(err.response?.data);
        this.message = 'Kayıt başarısız! ' + (err.response?.data?.message || '');
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
  font-family: Arial, sans-serif;
  background: url('https://images.unsplash.com/photo-1588776814546-df3f4b64f9e4?auto=format&fit=crop&w=1470&q=80')
  no-repeat center center/cover;
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
}

.back-btn {
  position: absolute;
  top: 25px;
  left: 25px;
  background-color: rgba(255, 255, 255, 0.85);
  color: #333;
  border: none;
  border-radius: 8px;
  padding: 8px 14px;
  font-weight: bold;
  cursor: pointer;
  transition: 0.2s;
  z-index: 2;
}

.back-btn:hover {
  background-color: #e2e2e2;
}

.register-container {
  position: relative;
  background: white;
  padding: 40px;
  border-radius: 12px;
  width: 350px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.3);
  z-index: 1;
  text-align: center;
}

.register-container h2 {
  margin-bottom: 25px;
  color: #333;
}

.register-container form input,
.register-container form select {
  width: 100%;
  padding: 10px 15px;
  margin-bottom: 15px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.2s;
}

.register-container form input:focus,
.register-container form select:focus {
  border-color: #007bff;
  outline: none;
  box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);
}

.register-container button {
  width: 100%;
  padding: 12px;
  border: none;
  border-radius: 8px;
  background-color: #007bff;
  color: white;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.2s;
}

.register-container button:hover {
  background-color: #0056b3;
}

.login-btn {
  margin-top: 15px;
  background-color: #28a745;
}

.login-btn:hover {
  background-color: #1e7e34;
}

.register-container p {
  margin-top: 15px;
  color: red;
  font-weight: 500;
  font-size: 14px;
}
</style>
