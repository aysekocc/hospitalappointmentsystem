<template>
  <div class="appointment-list-page p-6 max-w-5xl mx-auto relative">
    <div class="overlay"></div>
    <div class="content-container">
      <h2 class="text-2xl mb-4 font-semibold text-white">Geçmiş Randevularım</h2>

      <div v-if="loading" class="text-gray-200 text-center">Yükleniyor...</div>
      <div v-else-if="errorMessage" class="text-red-400 text-center">{{ errorMessage }}</div>

      <table v-else class="w-full border-collapse border border-gray-300 mt-2 bg-white bg-opacity-20 text-white backdrop-blur-sm rounded-lg overflow-hidden">
        <thead>
        <tr class="bg-blue-600 bg-opacity-70 text-white">
          <th class="border p-2">Başlangıç</th>
          <th class="border p-2">Bitiş</th>
          <th class="border p-2">Hasta Adı</th>
          <th class="border p-2">Hastane Adı</th>
          <th class="border p-2">Doktor Adı</th>

        </tr>
        </thead>
        <tbody>
        <tr v-for="appt in pastAppointments" :key="appt.id" class="hover:bg-blue-100 hover:bg-opacity-30 transition-colors">
          <td class="border p-2">{{ formatDate(appt.startedDate) }}</td>
          <td class="border p-2">{{ formatDate(appt.endedDate) }}</td>
          <td class="border p-2">{{ appt.username || "-" }}</td>
          <td class="border p-2">{{ appt.hospitalName || "-" }}</td>
          <td class="border p-2">{{ `${appt.doctorTitle || ""} ${appt.doctorName || ""}`.trim() || "-" }}</td>
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
      appointments: [],
      loading: false,
      errorMessage: "",
    };
  },
  computed: {
    pastAppointments() {
      const now = new Date();
      return this.appointments.filter(appt => {
        const start = new Date(appt.startedDate);
        return start < now;
      });
    }
  },
  methods: {
    async fetchAppointments() {
      this.loading = true;
      this.errorMessage = "";

      try {
        const token = localStorage.getItem("token");
        if (!token) {
          this.$router.push("/login");
          return;
        }

        const res = await axios.get(`/api/v1/appointment/my`, {
          headers: { Authorization: `Bearer ${token}` }
        });

        this.appointments = res.data;
        console.log("Randevular (Doctor):", this.appointments);

      } catch (err) {
        console.error(err);
        this.errorMessage = err.response?.status === 403
          ? "Yetkisiz erişim! Lütfen giriş yapın."
          : "Randevular yüklenemedi.";
      } finally {
        this.loading = false;
      }
    }
,
    formatDate(dateStr) {
      if (!dateStr) return "-";
      const date = new Date(dateStr);
      if (isNaN(date)) return "-";
      return date.toLocaleString("tr-TR", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit"
      });
    },
  },
  mounted() {
    this.fetchAppointments();
  },
};
</script>

<style scoped>
.appointment-list-page {
  position: relative;
  min-height: 100vh;
  font-family: Arial, sans-serif;
  background: url('https://cdn-icons-png.flaticon.com/512/2966/2966486.png') no-repeat center center/cover;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 50px;
}

.overlay {
  position: absolute;
  top: 0; left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0,0,0,0.6);
  z-index: 1;
}

.content-container {
  position: relative;
  z-index: 2;
  width: 100%;
}

table th, table td {
  text-align: center;
}

table th {
  font-weight: bold;
}

table tr:hover {
  cursor: pointer;
}
</style>
