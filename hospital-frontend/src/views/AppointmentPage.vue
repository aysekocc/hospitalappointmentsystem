<template>
  <div class="p-6 max-w-lg mx-auto">
    <h2 class="text-2xl mb-4 font-semibold">Randevu İşlemleri</h2>

    <!-- Randevu Formu -->
    <form @submit.prevent="createAppointment" class="space-y-3">
      <input
        type="datetime-local"
        v-model="appointment.startedDate"
        placeholder="Randevu Başlangıç Tarihi"
        required
        class="border p-2 w-full rounded" />
      <input
        type="datetime-local"
        v-model="appointment.endedDate"
        placeholder="Randevu Bitiş Tarihi"
        required
        class="border p-2 w-full rounded" />
      <input
        type="number"
        v-model="appointment.doctor"
        placeholder="Doktor ID"
        required
        class="border p-2 w-full rounded" />
      <input
        type="number"
        v-model="appointment.user"
        placeholder="Kullanıcı ID"
        required
        class="border p-2 w-full rounded" />
      <input
        type="number"
        v-model="appointment.hospitalId"
        placeholder="Hastane ID"
        required
        class="border p-2 w-full rounded" />

      <select v-model="appointment.status" class="border p-2 w-full rounded">
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
          <th class="border p-2">Durum</th>
          <th class="border p-2">Doktor</th>
          <th class="border p-2">Kullanıcı</th>
          <th class="border p-2">Hastane</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="appt in appointments" :key="appt.id">
          <td class="border p-2">{{ appt.id }}</td>
          <td class="border p-2">{{ formatDate(appt.startedDate) }}</td>
          <td class="border p-2">{{ formatDate(appt.endedDate) }}</td>
          <td class="border p-2">{{ appt.status }}</td>
          <td class="border p-2">{{ appt.doctor?.id || appt.doctor }}</td>
          <td class="border p-2">{{ appt.user?.id || appt.user }}</td>
          <td class="border p-2">{{ appt.hospitalId?.id || appt.hospitalId }}</td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import axiosIns from "../plugins/axios";

export default {
  data() {
    return {
      appointment: {
        startedDate: "",
        endedDate: "",
        status: "PENDING",
        doctor: null,
        user: null,
        hospitalId: null,
      },
      appointments: [],
      loading: false,
      successMessage: "",
      errorMessage: "",
    };
  },
  methods: {
    async createAppointment() {
      try {
        const token = localStorage.getItem("token");
        await axiosIns.post("/api/v1/appointment/create", this.appointment, {
          headers: { Authorization: `Bearer ${token}` },
        });
        this.successMessage = "Randevu başarıyla oluşturuldu.";
        this.errorMessage = "";

        // Formu temizle
        this.appointment = {
          startedDate: "",
          endedDate: "",
          status: "PENDING",
          doctor: null,
          user: null,
          hospitalId: null,
        };

        this.fetchAppointments();
      } catch (err) {
        console.error(err);
        this.errorMessage = "Randevu oluşturulamadı.";
        this.successMessage = "";
      }
    },

    async fetchAppointments() {
      this.loading = true;
      this.errorMessage = "";
      try {
        const token = localStorage.getItem("token");
        const res = await axiosIns.get("/api/v1/appointment?pageNumber=0&pageSize=10", {
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

    formatDate(date) {
      return new Date(date).toLocaleString();
    },
  },
  mounted() {
    this.fetchAppointments();
  },
};
</script>
