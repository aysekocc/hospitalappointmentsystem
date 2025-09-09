<template>
  <div class="p-6 max-w-lg mx-auto">
    <h2 class="text-2xl mb-4 font-semibold">Randevu İşlemleri</h2>

    <!-- Randevu Formu -->
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

    <!-- Mesajlar -->
    <p class="mt-4 text-green-600" v-if="successMessage">{{ successMessage }}</p>
    <p class="mt-4 text-red-600" v-if="errorMessage">{{ errorMessage }}</p>

    <!-- Randevu Listesi -->
    <div class="mt-8">
      <h3 class="text-xl mb-2">Randevularım</h3>
      <div v-if="loading">Yükleniyor...</div>
      <div v-else-if="errorMessage && appointments.length === 0" class="text-red-600">{{ errorMessage }}</div>
      <table v-else class="w-full border mt-2">
        <thead>
        <tr class="bg-gray-100">
          <th class="border p-2">ID</th>
          <th class="border p-2">Başlangıç</th>
          <th class="border p-2">Bitiş</th>
          <th class="border p-2">Doktor</th>
          <th class="border p-2">Kullanıcı</th>
          <th class="border p-2">Hastane</th>
          <th class="border p-2">Durum</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="appt in appointments" :key="appt.id">
          <td class="border p-2">{{ appt.id }}</td>
          <td class="border p-2">{{ formatDate(appt.startedDate) }}</td>
          <td class="border p-2">{{ formatDate(appt.endedDate) }}</td>
          <td class="border p-2">{{ appt.doctor?.id || appt.doctor }}</td>
          <td class="border p-2">{{ appt.user?.id || appt.user }}</td>
          <td class="border p-2">{{ appt.hospitalId?.id || appt.hospitalId }}</td>
          <td class="border p-2">{{ appt.status }}</td>
        </tr>
        </tbody>
      </table>
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
      appointments: [],
      loading: false,
      successMessage: "",
      errorMessage: "",
    };
  },
  methods: {
    async createAppointment() {
      if (
        !this.appointment.startedDate ||
        !this.appointment.endedDate ||
        !this.appointment.doctor ||
        !this.appointment.user ||
        !this.appointment.hospitalId ||
        !this.appointment.status
      ) {
        this.errorMessage = "Lütfen tüm alanları doldurun!";
        this.successMessage = "";
        return;
      }

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
        console.log(JSON.stringify(this.appointment))
      //  await this.fetchAppointments();
      } catch (err) {
        console.error(err);
        if (err.response?.status === 403) {
          this.errorMessage = "Yetkisiz işlem! Lütfen giriş yapın.";
          console.log(JSON.stringify(this.appointment))
          this.$router.push("/login");
        } else {
          this.errorMessage = "Randevu oluşturulamadı.";
        }
        this.successMessage = "";
      }
    },
/*
    async fetchAppointments() {
      this.loading = true;
      this.errorMessage = "";
      try {
        const token = localStorage.getItem("token");
        if (!token) {
          this.$router.push("/login");
          return;
        }

        const res = await axios.get("/api/v1/appointment?pageNumber=0&pageSize=10", {
          headers: { Authorization: `Bearer ${token}` },
        });
        this.appointments = res.data.content;
      } catch (err) {
        console.error(err);
        this.errorMessage = "Randevular yüklenemedi.";
      } finally {
        this.loading = false;
      }
    },
*/
    formatDate(date) {
      return new Date(date).toLocaleString();
    },
  },
  mounted() {
    const token = localStorage.getItem("token");
    const role = localStorage.getItem("role");
    if (!token) {
      this.$router.push("/login");
      return;
    }

    if (role !== "ROLE_USER") {
      this.$router.push("/login");
    } else {
     // this.fetchAppointments();
    }
  },
};
</script>
