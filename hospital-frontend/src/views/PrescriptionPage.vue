<template>
  <div class="p-6 max-w-lg mx-auto">
    <h2 class="text-2xl mb-4 font-semibold">Reçete Oluştur</h2>

    <form @submit.prevent="createPrescription" class="space-y-3">
      <input
        v-model="prescription.medicineName"
        placeholder="İlaç Adı"
        required
        class="border p-2 w-full"
      />

      <input
        v-model="prescription.diagnosis"
        placeholder="Teşhis"
        required
        class="border p-2 w-full"
      />

      <input
        v-model="prescription.userId"
        type="number"
        placeholder="Hasta ID"
        required
        class="border p-2 w-full"
      />

      <button type="submit" class="bg-blue-600 text-white px-4 py-2 rounded-lg">
        Reçete Kaydet
      </button>
    </form>

    <p class="mt-4 text-green-600" v-if="successMessage">{{ successMessage }}</p>
    <p class="mt-4 text-red-600" v-if="errorMessage">{{ errorMessage }}</p>

    <div class="mt-8">
      <h3 class="text-xl mb-2">Reçete Sil</h3>
      <input
        v-model="deleteId"
        type="number"
        placeholder="Reçete ID"
        class="border p-2 w-full"
      />
      <button
        @click="deletePrescription"
        class="bg-red-600 text-white px-4 py-2 rounded-lg mt-2"
      >
        Sil
      </button>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      prescription: {
        medicineName: "",
        diagnosis: "",
        user: null,
      },
      deleteId: "",
      successMessage: "",
      errorMessage: "",
    };
  },
  methods: {
    async createPrescription() {
      try {
        const token = localStorage.getItem("token");

        const requestBody = {
          medicineName: this.prescription.medicineName,
          diagnosis: this.prescription.diagnosis,
          user: this.userId
        };

        await axios.post(
          "/api/v1/prescription/create",
          {
            medicineName: this.prescription.medicineName,
            diagnosis: this.prescription.diagnosis,
            userId: this.prescription.userId
          },
          {
            headers: { Authorization: `Bearer ${token}` } }
        );

        this.successMessage = "Reçete başarıyla oluşturuldu.";
        this.errorMessage = "";
        this.prescription = { medicineName: "", diagnosis: "" };
        this.userId = null;

      } catch (err) {
        console.error(err);
        this.errorMessage =
          err.response?.data?.message || "Reçete kaydedilemedi.";
      }
    },


    async deletePrescription() {
      try {
        const token = localStorage.getItem("token");
        console.log(this.deleteId);
        await axios.delete(
          `/api/v1/prescription/${this.deleteId}`,
          { headers: { Authorization: `Bearer ${token}` } }
        );
        this.successMessage = "Reçete başarıyla silindi.";
        this.errorMessage = "";
        this.deleteId = "";
      } catch (err) {
        console.error(err);
        this.errorMessage =
          err.response?.data?.message || "Reçete silinemedi.";
      }
    },
  },
};
</script>
