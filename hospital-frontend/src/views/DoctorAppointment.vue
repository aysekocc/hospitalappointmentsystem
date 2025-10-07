<template>
  <div class="doctor-appointment-page p-6 max-w-5xl mx-auto relative">
    <div class="overlay"></div>
    <div class="content-container">

      <div v-if="showWelcome" class="welcome-screen flex flex-col items-center justify-center h-screen text-white text-center">
        <h1 class="text-4xl font-bold mb-6">Doktor Ekranına Hoş Geldiniz!</h1>
        <p class="mb-6">Randevularınızı görüntülemek için butona tıklayın.</p>
        <div class="flex flex-col gap-4">
          <button
            @click="showWelcome = false; fetchAppointments()"
            class="btn-appointments">
            Randevuları Görüntüle
          </button>


          <button
            @click="$router.push('/')"
            class="btn-home">
            Anasayfaya Dön
          </button>
        </div>
      </div>

      <div v-else>
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-2xl font-semibold text-white">Randevularım</h2>

          <button
            @click="goBackToWelcome"
            class="flex justify-end gap-2">
            ← Geri Dön
          </button>
        </div>

        <div v-if="loading" class="text-gray-200 text-center">Yükleniyor...</div>
        <div v-else-if="errorMessage" class="text-red-400 text-center">{{ errorMessage }}</div>

        <div v-if="showModal" class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
          <div class="bg-white p-6 rounded-lg shadow-md w-96">
            <h3 class="text-lg font-bold mb-4 text-gray-800">Reçete Yaz</h3>

            <input v-model="prescription.medicineName" placeholder="İlaç Adı" class="input-prescription mb-3"/>
            <input v-model="prescription.diagnosis" placeholder="Teşhis" class="input-prescription mb-3"/>

            <div class="flex justify-end gap-2">
              <button @click="showModal=false" class="btn-cancel">İptal</button>
              <button @click="savePrescription" class="btn-save">Kaydet</button>
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
            <th class="border p-2">Hastane Adı</th>z
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
                class="btn-prescription"
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
    goBackToWelcome() {
      this.showWelcome = true;
      this.appointments = [];
    }
  },
};
</script>

<style scoped>
.doctor-appointment-page {
  position: relative;
  min-height: 100vh;
  font-family: 'Arial', sans-serif;
  background: linear-gradient(to bottom right, #cceeff, #74b9ff);
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

table {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 8px 20px rgba(0,0,0,0.1);
}

.input-prescription {
  border: 1px solid #cbd5e1;
  padding: 10px 14px;
  width: 100%;
  border-radius: 8px;
  font-size: 1rem;
  transition: all 0.2s ease;
}
.input-prescription:focus {
  outline: none;
  border-color: #2563eb;
  box-shadow: 0 0 5px rgba(37, 99, 235, 0.3);
}

button {
  transition: all 0.3s ease;
}
button:hover {
  transform: translateY(-1px);
  box-shadow: 0 3px 8px rgba(0,0,0,0.2);
}

.back-button {
  border: none;
  transition: all 0.3s ease;
}
.back-button:hover {
  box-shadow: 0 5px 12px rgba(59, 130, 246, 0.4);
  transform: translateY(-1px) scale(1.03);
}

.btn-appointments {
  background: linear-gradient(90deg, #74b9ff, #cceeff);
  font-weight: bold;
  padding: 10px 20px;
  border-radius: 10px;
  color: white;
  font-size: 1rem;
  transition: all 0.3s ease;
}
.btn-appointments:hover {
  background: linear-gradient(90deg, #cceeff, #74b9ff);
  transform: scale(1.03);
}

.btn-home {
  background: linear-gradient(90deg, #ff7f50, #ffb347);
  font-weight: bold;
  padding: 8px 18px;
  border-radius: 10px;
  color: white;
  font-size: 0.95rem;
  transition: all 0.3s ease;
}
.btn-home:hover {
  background: linear-gradient(90deg, #ffb347, #ff7f50);
  transform: scale(1.03);
}

.btn-prescription {
  background-color: #22c55e;
  color: white;
  padding: 6px 12px;
  border-radius: 6px;
  font-size: 0.95rem;
  transition: all 0.3s ease;
}
.btn-prescription:hover {
  background-color: #16a34a;
  transform: scale(1.05);
}

.btn-cancel {
  background-color: #9ca3af;
  color: white;
  padding: 6px 12px;
  border-radius: 6px;
  font-size: 0.95rem;
  transition: all 0.3s ease;
}
.btn-cancel:hover {
  background-color: #6b7280;
  transform: scale(1.05);
}

.btn-save {
  background-color: #3b82f6;
  color: white;
  padding: 6px 12px;
  border-radius: 6px;
  font-size: 0.95rem;
  transition: all 0.3s ease;
}
.btn-save:hover {
  background-color: #2563eb;
  transform: scale(1.05);
}

@keyframes fadeIn {
  0% { opacity: 0; transform: translateY(10px);}
  100% { opacity: 1; transform: translateY(0);}
}

.welcome-screen h1 {
  text-shadow: 1px 1px 5px rgba(0,0,0,0.2);
}
.welcome-screen p {
  font-size: 1.2rem;
  margin-bottom: 2rem;
}
</style>
