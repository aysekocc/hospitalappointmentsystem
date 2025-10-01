<template>
  <div class="home">
    <div class="overlay"></div>
    <h1>Hoşgeldiniz</h1>
    <h2>Hastane Randevu Sistemi</h2>

    <!-- Kayıt Butonları -->
    <button @click="$router.push('/register/user')">Hasta Kaydı</button>
    <button @click="$router.push('/register/doctor')">Doktor Kaydı</button>

    <!-- Login Butonları -->
    <button @click="$router.push('/login?role=user')">Hasta Girişi</button>
    <button @click="$router.push('/login?role=doctor')">Doktor Girişi</button>
  </div>
</template>

<script>
export default {
  name: "Home"
}
</script>

<style scoped>
.home {
  position: relative;
  text-align: center;
  margin-top: 50px;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: white;
  background: url('https://cdn-icons-png.flaticon.com/512/2966/2966506.png') no-repeat center center/cover;
  font-family: Arial, sans-serif;
}

.overlay {
  position: absolute;
  top: 0; left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0,0,0,0.5);
  z-index: 1;
}

.home h1, .home h2 {
  position: relative;
  z-index: 2;
  margin: 10px 0;
  text-shadow: 1px 1px 5px rgba(0,0,0,0.7);
}

button {
  position: relative;
  z-index: 2;
  margin: 10px;
  padding: 12px 25px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: bold;
  background: #007BFF;
  color: white;
  transition: all 0.3s ease;
}

button:hover {
  background: #0056b3;
  transform: scale(1.05);
}
</style>
