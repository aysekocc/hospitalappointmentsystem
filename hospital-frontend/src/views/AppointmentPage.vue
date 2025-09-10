<!-- AppointmentPage.vue -->
<template>
  <div class="p-6 max-w-lg mx-auto">
    <h2 class="text-2xl mb-4 font-semibold">Randevu İşlemleri</h2>

    <form @submit.prevent="createAppointment" class="space-y-3">
      <input type="datetime-local" v-model="appointment.startedDate" required class="border p-2 w-full rounded" />
      <input type="datetime-local" v-model="appointment.endedDate" required class="border p-2 w-full rounded" />
      <input type="number" v-model="appointment.doctor" placeholder="Doktor ID" required class="border p-2 w-full rounded" />
      <input type="number" v-model="appointment.user" placeholder="Kullanıcı ID" required class="border p-2 w-full rounded" />
      <input type="number" v-model="appointment.hospitalId" placeholder="Hastane ID" required class="border p-2 w-full rounded" />

      <select v-model="appointment.status" required class="border p-2 w-full rounded">
        <option value="">Durum Seçiniz</option>
        <option value="PENDING">Beklemede</option>
        <option value="CONFIRMED">Onaylandı</option>
        <option value="CANCELLED">İptal</option>
      </select>

      <button type="submit" class="bg-blue-600 text-white px-4 py-2 rounded-lg">
        Randevu Oluştur
      </button>
    </form>

    <p class="mt-4 text-green-600" v-if="successMessage">{{ successMessage }}</p>
    <p class="mt-4 text-red-600" v-if="errorMessage">{{ errorMessage }}</p>

    <div class="mt-6">
      <button @click="$router.push('/appointments/my-appointments')"
              class="bg-gray-600 text-white px-4 py-2 rounded-lg">
        Geçmiş Randevularımı Gör
      </button>
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
