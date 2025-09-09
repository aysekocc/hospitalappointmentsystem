<template>
  <div class="p-6 max-w-3xl mx-auto">
    <h2 class="text-2xl font-semibold mb-4">Reçete Listesi</h2>

    <!-- Hash ile reçete arama -->
    <div class="mb-6 flex gap-2">
      <input
        v-model="hash"
        placeholder="Reçete Hash Kodu"
        class="border p-2 flex-1"
      />
      <button
        @click="searchByHash"
        class="bg-blue-600 text-white px-4 py-2 rounded-lg"
      >
        Ara
      </button>
    </div>

    <!-- Hash arama sonucu -->
    <div v-if="hashPrescription" class="border p-3 mb-6 bg-gray-100 rounded">
      <h3 class="font-semibold mb-2">Hash ile bulunan reçete</h3>
      <p><strong>İlaç:</strong> {{ hashPrescription.medicineName }}</p>
      <p><strong>Teşhis:</strong> {{ hashPrescription.diagnosis }}</p>
    </div>

    <!-- Kullanıcıya göre reçete listesi -->
    <div class="mb-4">
      <label class="block mb-1">Kullanıcı ID:</label>
      <input
        v-model="userId"
        type="number"
        placeholder="Kullanıcı ID giriniz"
        class="border p-2 w-full"
      />
      <button
        @click="fetchPrescriptions"
        class="mt-2 bg-green-600 text-white px-4 py-2 rounded-lg"
      >
        Listele
      </button>
    </div>

    <!-- Reçeteler tablosu -->
    <div v-if="prescriptions.length > 0">
      <table class="w-full border-collapse border">
        <thead>
        <tr class="bg-gray-200">
          <th class="border p-2">ID</th>
          <th class="border p-2">İlaç Adı</th>
          <th class="border p-2">Teşhis</th>
          <th class="border p-2">İşlem</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="p in prescriptions" :key="p.id">
          <td class="border p-2">{{ p.id }}</td>
          <td class="border p-2">{{ p.medicineName }}</td>
          <td class="border p-2">{{ p.diagnosis }}</td>
          <td class="border p-2">
            <button
              @click="deletePrescription(p.id)"
              class="bg-red-600 text-white px-3 py-1 rounded-lg"
            >
              Sil
            </button>
          </td>
        </tr>
        </tbody>
      </table>

      <!-- Sayfalama -->
      <div class="flex justify-between items-center mt-4">
        <button
          :disabled="page === 0"
          @click="page--, fetchPrescriptions()"
          class="px-4 py-2 bg-gray-300 rounded disabled:opacity-50"
        >
          Önceki
        </button>
        <span>Sayfa: {{ page + 1 }}</span>
        <button
          :disabled="!hasNextPage"
          @click="page++, fetchPrescriptions()"
          class="px-4 py-2 bg-gray-300 rounded disabled:opacity-50"
        >
          Sonraki
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "PrescriptionList",
  data() {
    return {
      prescriptions: [],
      hashPrescription: null,
      hash: "",
      userId: "",
      page: 0,
      size: 5,
      hasNextPage: false,
    };
  },
  methods: {
    async fetchPrescriptions() {
      if (!this.userId) {
        alert("Kullanıcı ID giriniz!");
        return;
      }
      try {
        const token = localStorage.getItem("token");
        const res = await axios.get(
          `/api/v1/prescription/list/user?userId=${this.userId}&page=${this.page}&size=${this.size}`,
          { headers: { Authorization: `Bearer ${token}` } }
        );
        this.prescriptions = res.data.content;
        this.hasNextPage = !res.data.last;
      } catch (err) {
        console.error(err);
        alert("Reçeteler yüklenemedi!");
      }
    },
    async searchByHash() {
      if (!this.hash) {
        alert("Hash kodu giriniz!");
        return;
      }
      try {
        const token = localStorage.getItem("token");
        const res = await axios.get(
          `/api/v1/prescription/list/hash?hash=${this.hash}`,
          { headers: { Authorization: `Bearer ${token}` } }
        );
        this.hashPrescription = res.data || null;
      } catch (err) {
        console.error(err);
        alert("Reçete bulunamadı!");
      }
    },
    async deletePrescription(id) {
      if (!confirm("Bu reçeteyi silmek istediğinize emin misiniz?")) return;
      try {
        const token = localStorage.getItem("token");
        await axios.delete(`/api/v1/prescription/${id}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        this.fetchPrescriptions();
      } catch (err) {
        console.error(err);
        alert("Silme işlemi başarısız!");
      }
    },
  },
};
</script>

<style scoped>
table {
  border: 1px solid #ccc;
}
</style>
