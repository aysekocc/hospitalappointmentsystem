<!-- AppointmentPage.vue -->
<template>
  <div class="appointment-page p-6 relative max-w-lg mx-auto">
    <div class="overlay"></div>
    <div class="content-container">
      <h2 class="text-2xl mb-4 font-semibold text-white text-center">Randevu İşlemleri</h2>

      <form @submit.prevent="createAppointment" class="space-y-3">
        <input type="datetime-local" v-model="appointment.startedDate" required class="input-field" />
        <input type="datetime-local" v-model="appointment.endedDate" required class="input-field" />
        <input type="number" v-model="appointment.doctor" placeholder="Doktor ID" required class="input-field" />
        <input type="number" v-model="appointment.user" placeholder="Kullanıcı ID" required class="input-field" />
        <input type="number" v-model="appointment.hospitalId" placeholder="Hastane ID" required class="input-field" />


        <button type="submit" class="btn-submit">Randevu Oluştur</button>
      </form>

      <p class="mt-4 text-green-400 font-medium" v-if="successMessage">{{ successMessage }}</p>
      <p class="mt-4 text-red-400 font-medium" v-if="errorMessage">{{ errorMessage }}</p>

      <div class="mt-6 text-center">
        <button @click="$router.push('/appointments/my-appointments')" class="btn-secondary">
          Geçmiş Randevularımı Gör
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      appointment: {
        startedDate: "",
        endedDate: "",
        doctor: null,
        user: null,
        hospitalId: null,
        status: "",
      },
      successMessage: "",
      errorMessage: "",
    };
  },
  methods: {
    async createAppointment() {
      try {
        const token = localStorage.getItem("token");
        if (!token) {
          this.$router.push("/login");
          return;
        }

        await axios.post("/api/v1/appointment/create", this.appointment, {
          headers: { Authorization: `Bearer ${token}` },
        });

        this.successMessage = "Randevu başarıyla oluşturuldu.";
        this.errorMessage = "";
        this.appointment = {
          startedDate: "",
          endedDate: "",
          doctor: null,
          user: null,
          hospitalId: null,
          status: "",
        };
      } catch (err) {
        console.error(err);
        this.errorMessage = "Randevu oluşturulamadı.";
        this.successMessage = "";
      }
    },
  },
};
</script>

<style scoped>
.appointment-page {
  position: relative;
  min-height: 100vh;
  padding: 2rem;
  background: url('https://cdn-icons-png.flaticon.com/512/2966/2966486.png') no-repeat center center/cover;
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

.content-container {
  position: relative;
  z-index: 2;
  background: rgba(255,255,255,0.05);
  padding: 25px 30px;
  border-radius: 12px;
  backdrop-filter: blur(5px);
  box-shadow: 0 4px 20px rgba(0,0,0,0.3);
  color: white;
}

.input-field {
  width: 100%;
  padding: 10px 12px;
  border-radius: 8px;
  border: 1px solid rgba(255,255,255,0.5);
  background: rgba(255,255,255,0.1);
  color: white;
  outline: none;
  transition: 0.3s;
}

.input-field:focus {
  border-color: #00bfff;
  background: rgba(255,255,255,0.2);
}

.btn-submit {
  width: 100%;
  background: #007BFF;
  color: white;
  padding: 10px 0;
  border-radius: 8px;
  font-weight: bold;
  cursor: pointer;
  transition: 0.3s;
}

.btn-submit:hover {
  background: #0056b3;
}

.btn-secondary {
  width: 100%;
  background: #6c757d;
  color: white;
  padding: 10px 0;
  border-radius: 8px;
  font-weight: bold;
  cursor: pointer;
  transition: 0.3s;
}

.btn-secondary:hover {
  background: #5a6268;
}
</style>
