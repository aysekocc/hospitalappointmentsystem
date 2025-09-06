<template>
  <div class="user-card">
    <h3>{{ user.name }} {{ user.surname }}</h3>
    <p>Role: {{ user.role }}</p>
  </div>
</template>

<script>
export default { props: ['user'] }
</script>
