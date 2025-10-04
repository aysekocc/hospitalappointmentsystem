<template>
  <div class="appointment-list-page p-6 max-w-5xl mx-auto relative">
    <div class="overlay"></div>
    <div class="content-container">

      <!-- WELCOME SCREEN -->
      <div v-if="showWelcome" class="welcome-screen flex flex-col items-center justify-center h-screen text-white text-center">
        <h1 class="text-4xl font-bold mb-6">Doktor Ekranına Hoş Geldiniz!</h1>
        <p class="mb-6">Randevularınızı görüntülemek için butona tıklayın.</p>
        <button
          @click="showWelcome = false; fetchAppointments()"
          class="px-6 py-3 rounded-lg bg-gradient-to-r from-blue-400 to-blue-200 hover:from-blue-200 hover:to-blue-400 font-bold">
          Randevuları Görüntüle
        </button>
      </div>

      <!-- APPOINTMENT LIST -->
      <div v-else>
        <h2 class="text-2xl mb-4 font-semibold text-white">Randevularım</h2>

        <div v-if="loading" class="text-gray-200 text-center">Yükleniyor...</div>
        <div v-else-if="errorMessage" class="text-red-400 text-center">{{ errorMessage }}</div>

        <div v-if="showModal" class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
          <div class="bg-white p-6 rounded-lg shadow-md w-96">
            <h3 class="text-lg font-bold mb-4 text-gray-800">Reçete Yaz</h3>

            <input v-model="prescription.medicineName" placeholder="İlaç Adı" class="border p-2 w-full mb-3"/>
            <input v-model="prescription.diagnosis" placeholder="Teşhis" class="border p-2 w-full mb-3"/>

            <div class="flex justify-end gap-2">
              <button @click="showModal=false" class="px-4 py-2 bg-gray-400 text-white rounded">İptal</button>
              <button @click="savePrescription" class="px-4 py-2 bg-blue-600 text-white rounded">Kaydet</button>
            </div>
          </div>
        </div>

        <table v-else class="w-full border-collapse border border-gray-300 mt-2 bg-white bg-opacity-20 text-white backdrop-blur-sm rounded-lg overflow-hidden">
          <thead>
          <tr class="bg-blue-600 bg-opacity-70 text-white">
            <th class="border p-2 w-8"></th>
            <th class="border p-2">Başlangıç</th>
            <th class="border p-2">Bitiş</th>
            <th class="border p-2">Hasta Adı</th>
            <th class="border p-2">Hastane Adı</th>
            <th class="border p-2">Doktor Adı</th>
          </tr>
          </thead>

          <tbody>
          <tr v-for="appt in appointments" :key="appt.id" class="hover:bg-blue-100 hover:bg-opacity-30 transition-colors">
            <th class="border p-2">İşlem</th>
            <td class="border p-2">{{ formatDate(appt.startedDate) }}</td>
            <td class="border p-2">{{ formatDate(appt.endedDate) }}</td>
            <td class="border p-2">{{ appt.username || "-" }}</td>
            <td class="border p-2">{{ appt.hospitalName || "-" }}</td>
            <td class="border p-2">{{ `${appt.doctorTitle || ""} ${appt.doctorName || ""}`.trim() || "-" }}</td>
            <td class="border p-2">
              <button
                class="bg-green-500 text-white px-3 py-1 rounded hover:bg-green-600"
                @click="openPrescriptionModal(appt)">
                Reçete Yaz
              </button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>

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
      showModal: false,
      showWelcome: true,
      selectedAppointment: null,
      prescription: {
        medicineName: "",
        diagnosis: "",
        appointmentId: null,
      },
    };
  },
  mounted() {
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
      } catch (err) {
        console.error(err);
        this.errorMessage = err.response?.status === 403
          ? "Yetkisiz erişim! Lütfen giriş yapın."
          : "Randevular yüklenemedi.";
      } finally {
        this.loading = false;
      }
    },
    openPrescriptionModal(appt) {
      this.selectedAppointment = appt;
      this.prescription = { medicineName: "", diagnosis: "", appointmentId: appt.id};
      this.showModal = true;
    },
    async savePrescription() {
      try {
        const token = localStorage.getItem("token");
        await axios.post("/api/v1/prescription/create", this.prescription, {
          headers: { Authorization: `Bearer ${token}` }
        });
        alert("Reçete başarıyla kaydedildi!");
        this.showModal = false;
      } catch (err) {
        console.error(err);
        alert("Reçete kaydedilemedi.");
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

<style scoped>
.appointment-list-page {
  position: relative;
  min-height: 100vh;
  font-family: 'Arial', sans-serif;
  background: linear-gradient(to bottom right, #cceeff, #74b9ff); /* Daha açık mavi tonları */
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
  background: rgba(255,255,255,0.15);
  z-index: 1;
}

.content-container {
  position: relative;
  z-index: 2;
  width: 100%;
  animation: fadeIn 0.8s ease;
}

table th, table td {
  text-align: center;
  transition: all 0.3s ease;
}

table th {
  font-weight: bold;
}

table tr:hover {
  cursor: pointer;
  background-color: rgba(72, 187, 120, 0.2);
  transform: scale(1.02);
}

button {
  transition: all 0.3s ease;
}

button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 10px rgba(0,0,0,0.2);
}

.modal-enter-active, .modal-leave-active {
  transition: opacity 0.3s ease;
}
.modal-enter-from, .modal-leave-to {
  opacity: 0;
}

table {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 8px 20px rgba(0,0,0,0.1);
}

input {
  border-radius: 6px;
  transition: all 0.2s ease;
}

input:focus {
  outline: none;
  border-color: #2563eb;
  box-shadow: 0 0 5px rgba(37, 99, 235, 0.3);
}

@keyframes fadeIn {
  0% { opacity: 0; transform: translateY(10px);}
  100% { opacity: 1; transform: translateY(0);}
}

/* Welcome ekranı stil eklemeleri */
.welcome-screen h1 {
  text-shadow: 1px 1px 5px rgba(0,0,0,0.2);
}

.welcome-screen p {
  font-size: 1.2rem;
  margin-bottom: 2rem;
}

.welcome-screen button {
  background: linear-gradient(90deg, #74b9ff, #cceeff);
  font-weight: bold;
}

.welcome-screen button:hover {
  background: linear-gradient(90deg, #cceeff, #74b9ff);
}
</style>
