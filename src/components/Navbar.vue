<template>
  <nav>
    <button @click="logout">Logout</button>
  </nav>
</template>

<script>
import router from '../router'
export default {
  methods: {
    logout() {
      localStorage.removeItem('token')
      router.push('/')
    }
  }
}
</script>
