<template>
  <div class="prescriptions-page p-6">
    <h2 class="text-2xl mb-4 font-semibold">Benim Reçetelerim</h2>

    <div v-if="loading">Yükleniyor...</div>
    <div v-else-if="error" class="text-red-600">{{ error }}</div>
    <div v-else>
      <table class="border w-full">
        <thead>
        <tr>
          <th class="border px-2">ID</th>
          <th class="border px-2">İlaç Adı</th>
          <th class="border px-2">Teşhis</th>
          <th class="border px-2">Tarih</th>
          <th class="border px-2">Hash</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="p in prescriptions" :key="p.id">
          <td class="border px-2">{{ p.id }}</td>
          <td class="border px-2">{{ p.medicineName }}</td>
          <td class="border px-2">{{ p.diagnosis }}</td>
          <td class="border px-2">{{ formatDate(p.date) }}</td>
          <td class="border px-2">{{ p.hashPrescription }}</td>
        </tr>
        </tbody>
      </table>

      <div class="pagination mt-4 flex gap-4 items-center">
        <button :disabled="page === 0" @click="fetchPrescriptions(page - 1)">
          Önceki
        </button>
        <span>Sayfa {{ page + 1 }} / {{ totalPages }}</span>
        <button :disabled="page >= totalPages - 1" @click="fetchPrescriptions(page + 1)">
          Sonraki
        </button>
      </div>
    </div>


    <button
      class="mt-4 bg-blue-600 text-white px-4 py-2 rounded-lg"
      @click="fetchAppointment">
      Randevu Al
    </button>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "UserPage",
  data() {
    return {
      prescriptions: [],
      loading: false,
      error: "",
      page: 0,
      size: 2,
      totalPages: 0,
      userId: null,
    };
  },
  methods: {
    fetchAppointment() {
      this.$router.push("/appointments");
    },

    async fetchPrescriptions(page = 0) {
      const userId = localStorage.getItem("userId");
      if (!userId) {
        this.error = "Kullanıcı bilgisi bulunamadı!";
        return;
      }

      this.loading = true;
      this.error = "";

      try {
        const token = localStorage.getItem("token");

        const res = await axios.get("/api/v1/prescription/list/user", {
          params: { userId, page, size: this.size },
          headers: { Authorization: `Bearer ${token}` },
        });

        this.prescriptions = res.data.content;
        this.page = res.data.number;
        this.totalPages = res.data.totalPages;
      } catch (err) {
        console.error("Fetch error:", err);
        this.error = "Reçeteler alınamadı!";
      } finally {
        this.loading = false;
      }
    },

    formatDate(dateStr) {
      return new Date(dateStr).toLocaleDateString("tr-TR", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
      });

    },
  },
  mounted() {
    const storedUserId = localStorage.getItem("userId");
    if (storedUserId) {
      this.userId = Number(storedUserId);
      this.fetchPrescriptions(0);
    } else {
      this.error = "Kullanıcı bilgisi bulunamadı!";
    }
  },
};
</script>
