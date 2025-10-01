<template>
  <div class="doctor-appointments p-6 relative">
    <div class="overlay"></div>
    <div class="content-container">
      <h2 v-if="doctor" class="text-2xl mb-4 font-semibold text-white">
        Hoşgeldiniz {{ doctor.title }} {{ doctor.name }}
        <br />
        Randevularınız Aşağıdadır
      </h2>
      <div v-if="loading" class="text-white font-medium">Yükleniyor...</div>
      <div v-else-if="error" class="text-red-400 font-medium">{{ error }}</div>
      <div v-else>
        <table class="w-full text-left rounded-lg overflow-hidden backdrop-blur-sm">
          <thead>
          <tr class="bg-blue-600 bg-opacity-70 text-white">
            <th>ID</th>
            <th>Hasta Adı</th>
            <th>Başlangıç</th>
            <th>Bitiş</th>
            <th>Reçete Yaz</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="appt in appointments" :key="appt.id" class="hover:bg-blue-100 hover:bg-opacity-30 transition-colors">
            <td>{{ appt.id }}</td>
            <td>{{ appt.userUsername }}</td>
            <td>{{ formatDate(appt.startedDate) }}</td>
            <td>{{ formatDate(appt.endedDate) }}</td>
            <td>
              <button @click="selectAppointment(appt)" class="btn-submit">
                Reçete Yaz
              </button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <!-- Reçete formu -->
      <div v-if="selectedAppointment" class="mt-6">
        <h3 class="text-xl mb-2 text-white">Randevu ID {{ selectedAppointment.id }} için Reçete Oluştur</h3>
        <form @submit.prevent="createPrescription" class="space-y-3">
          <input v-model="prescription.medicineName" placeholder="İlaç Adı" required class="input-field" />
          <input v-model="prescription.diagnosis" placeholder="Teşhis" required class="input-field" />
          <button type="submit" class="btn-submit">Reçete Kaydet</button>
        </form>
      </div>

      <p class="mt-4 text-green-400 font-medium" v-if="successMessage">{{ successMessage }}</p>
      <p class="mt-4 text-red-400 font-medium" v-if="errorMessage">{{ errorMessage }}</p>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      doctor: null,
      appointments: [],
      loading: false,
      error: "",
      selectedAppointment: null,
      prescription: { medicineName: "", diagnosis: "", appointmentId: null },
      successMessage: "",
      errorMessage: "",
    };
  },
  mounted() {
    this.fetchAppointments();
  },
  methods: {
    async fetchAppointments() {
      try {
        this.loading = true;
        const token = localStorage.getItem("token");
        const res = await axios.get("/api/v1/appointment/my", {
          headers: { Authorization: `Bearer ${token}` },
        });
        this.appointments = res.data;

        if (this.appointments.length > 0) {
          this.doctor = {
            doctorName: this.appointments[0].doctorName,
            doctorId: this.appointments[0].doctorId,
          };
        }
      } catch (err) {
        console.error(err);
        this.error = "Randevular alınamadı!";
      } finally {
        this.loading = false;
      }
    },
    selectAppointment(appt) {
      this.selectedAppointment = appt;
      this.prescription.appointmentId = appt.id;
      this.prescription.medicineName = "";
      this.prescription.diagnosis = "";
    },
    async createPrescription() {
      try {
        const token = localStorage.getItem("token");

        await axios.post("/api/v1/prescription/create", this.prescription, {
          headers: { Authorization: `Bearer ${token}` },
        });

        this.successMessage = "Reçete başarıyla oluşturuldu.";
        this.errorMessage = "";
        this.selectedAppointment = null;
      } catch (err) {
        console.error(err);
        this.errorMessage = "Reçete kaydedilemedi.";
      }
    },
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
};
</script>
