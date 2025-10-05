<template>
  <div class="appointment-page p-6 relative max-w-lg mx-auto min-h-screen bg-gradient-to-r from-blue-700 to-purple-700 flex items-center justify-center">
    <div class="overlay absolute inset-0 bg-black opacity-30 rounded-lg"></div>
    <div class="content-container relative bg-white rounded-xl shadow-lg p-6 w-full">

      <div v-if="!showForm" class="welcome-screen text-center">
        <h2 class="text-3xl font-bold mb-4 text-gray-800">Hoş Geldiniz!</h2>
        <p class="text-gray-600 mb-6">Lütfen yapmak istediğiniz işlemi seçin.</p>

        <button @click="showForm = true" class="btn-submit mb-4 w-full py-2 px-4 flex items-center justify-center gap-2">
          Randevu Oluştur
        </button>

        <button @click="$router.push('/appointments/my-appointments')" class="btn-secondary mb-4 w-full py-2 px-4 flex items-center justify-center gap-2">
          Geçmiş Randevularımı Gör
        </button>

        <button @click="$router.push('/')" class="btn-home w-full py-2 px-4 flex items-center justify-center gap-2">
          Anasayfaya Dön
        </button>
      </div>

      <form v-else @submit.prevent="createAppointment" class="space-y-4">
        <h2 class="text-2xl mb-4 font-bold text-gray-800 text-center">Randevu Oluştur</h2>

        <label class="block">
          Hastane ID:
          <input type="number" v-model.number="appointment.hospitalId" placeholder="Hastane ID" required class="input-field w-full mt-1 p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-400 focus:outline-none" />
        </label>

        <label class="block">
          Poliklinik:
          <select v-model="appointment.specialty" class="input-field w-full mt-1 p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-400 focus:outline-none" required>
            <option disabled value="">Branş Seçin</option>
            <option v-for="spec in specialties" :key="spec" :value="spec">
              {{ formatSpecialty(spec) }}
            </option>
          </select>
        </label>

        <label class="block">
          Doktor:
          <select v-model.number="appointment.doctor" class="input-field w-full mt-1 p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-400 focus:outline-none" required>
            <option disabled value="">Doktor Seçin</option>
            <option v-for="doc in filteredDoctors" :key="doc.id" :value="doc.id">
              {{ doc.title }} {{ doc.name }} {{ doc.surname }}
            </option>
          </select>
        </label>

        <label class="block">
          Başlangıç Tarihi:
          <input type="date" v-model="appointment.startedDate" required class="input-field w-full mt-1 p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-400 focus:outline-none" />
        </label>

        <label class="block">
          Bitiş Tarihi:
          <input type="date" v-model="appointment.endedDate" required class="input-field w-full mt-1 p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-400 focus:outline-none" />
        </label>

        <div class="flex flex-col gap-2">
          <button type="submit" class="btn-submit w-full py-2 px-4 flex items-center justify-center gap-2">
            Randevu Oluştur
          </button>

          <button type="button" @click="showForm = false" class="btn-secondary w-full py-2 px-4 flex items-center justify-center gap-2">
            Geri Dön
          </button>
        </div>

        <p class="mt-4 text-green-600 font-medium" v-if="successMessage">{{ successMessage }}</p>
        <p class="mt-4 text-red-600 font-medium" v-if="errorMessage">{{ errorMessage }}</p>
      </form>

    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      showForm: false,
      appointment: {
        startedDate: "",
        endedDate: "",
        doctor: null,
        hospitalId: null,
        specialty: "",
      },
      specialties: [
        "KARDIYOLOJI",
        "DAHILIYE",
        "ORTOPEDI",
        "GOZ_HASTALIKLARI",
        "KBB",
        "BEYIN_CERRAHISI",
        "DERMATOLOJI",
        "COCUK_SAGLIGI",
        "PSIKIYATRI",
        "NOROLOJI",
        "UROLOJI",
      ],
      doctors: [],
      successMessage: "",
      errorMessage: "",
    };
  },
  computed: {
    filteredDoctors() {
      if (!this.appointment.specialty) return [];
      return this.doctors.filter(doc => doc.specialty === this.appointment.specialty);
    }
  },
  watch: {
    'appointment.specialty'(newVal, oldVal) {
      this.appointment.doctor = null;
    }
  },
  mounted() {
    this.fetchDoctors();
  },
  methods: {
    formatSpecialty(spec) {
      return spec.replace(/_/g, " ");
    },
    async fetchDoctors() {
      try {
        const token = localStorage.getItem("token");
        const res = await axios.get("/api/v1/doctor/all", {
          headers: {
            Authorization: token ? `Bearer ${token}` : undefined,
          },
        });
        this.doctors = res.data;
      } catch (err) {
        console.error("Doktorlar yüklenemedi", err.response?.data || err.message);
      }
    },
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
          hospitalId: null,
          specialty: "",
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
  background: linear-gradient(135deg, #667eea, #764ba2);
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0,0,0,0.3);
  border-radius: 1rem;
  backdrop-filter: blur(2px);
}

.content-container {
  background: #ffffff;
  padding: 2rem;
  border-radius: 1rem;
  box-shadow: 0 10px 25px rgba(0,0,0,0.3);
  width: 100%;
  max-width: 500px;
  position: relative;
  z-index: 10;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.content-container:hover {
  transform: translateY(-5px);
  box-shadow: 0 20px 35px rgba(0,0,0,0.3);
}

h2 {
  font-weight: 700;
  color: #333333;
  transition: all 0.3s ease;
}

h2:hover {
  color: #667eea;
  transform: scale(1.02);
}

.input-field {
  width: 100%;
  padding: 0.75rem 1rem;
  margin-top: 0.3rem;
  border: 1px solid #ccc;
  border-radius: 0.75rem;
  outline: none;
  transition: all 0.3s ease;
  font-size: 1rem;
}

.input-field:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.25);
  transform: scale(1.02);
}

select.input-field {
  appearance: none;
  background: url('data:image/svg+xml;utf8,<svg fill="%23999" height="24" viewBox="0 0 24 24" width="24" xmlns="http://www.w3.org/2000/svg"><path d="M7 10l5 5 5-5z"/></svg>') no-repeat right 1rem center;
  background-size: 1rem;
  padding-right: 2.5rem;
}

.btn-submit, .btn-secondary, .btn-home {
  font-weight: 600;
  padding: 0.5rem 1rem;
  border-radius: 0.75rem;
  border: none;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
}

.btn-submit {
  background-color: #667eea;
  color: white;
}

.btn-submit:hover {
  background-color: #5a67d8;
  transform: translateY(-2px);
  box-shadow: 0 6px 15px rgba(0,0,0,0.25);
}

.btn-secondary {
  background-color: #e2e8f0;
  color: #1a202c;
}

.btn-secondary:hover {
  background-color: #cbd5e1;
  transform: translateY(-2px);
  box-shadow: 0 6px 15px rgba(0,0,0,0.15);
}

.btn-home {
  background-color: #48bb78;
  color: white;
}

.btn-home:hover {
  background-color: #38a169;
  transform: translateY(-2px);
  box-shadow: 0 6px 15px rgba(0,0,0,0.25);
}

.text-green-600, .text-red-600 {
  transition: all 0.3s ease;
  font-weight: 500;
}

.text-green-600 {
  color: #38a169;
}

.text-red-600 {
  color: #e53e3e;
}

.space-y-3 > * + * {
  margin-top: 0.75rem;
}
</style>
