<template>
  <div class="appointment-card">
    <p>Patient: {{ appointment.patientName }}</p>
    <p>Doctor: {{ appointment.doctorName }}</p>
    <p>Date: {{ appointment.startDate }}</p>
  </div>
</template>

<script>
export default { props: ['appointment'] }
</script>
