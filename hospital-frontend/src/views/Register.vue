<template>
  <div>
    <h2>Register</h2>
    <form @submit.prevent="register">
      <input v-model="identity" placeholder="Identity" required />
      <input v-model="name" placeholder="Name" required />
      <input v-model="surname" placeholder="Surname" required />
      <input v-model="age" type="number" placeholder="Age" required />

      <select v-model="gender" required>
        <option disabled value="">Select Gender</option>
        <option value="MALE">Male</option>
        <option value="FEMALE">Female</option>
      </select>

      <input v-model="username" placeholder="Username" required />
      <input v-model="password" type="password" placeholder="Password" required />

      <select v-model="role" required>
        <option disabled value="">Select Role</option>
        <option value="USER">User</option>
        <option value="DOCTOR">Doctor</option>
        <option value="ADMIN">Admin</option>
      </select>

      <button type="submit">Register</button>
    </form>

    <p>{{ message }}</p>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      identity: '',
      name: '',
      surname: '',
      age: null,
      gender: '',
      username: '',
      password: '',
      role: '',
      message: ''
    };
  },
  methods: {
    async register() {
      try {
        const response = await axios.post(
          'http://localhost:8082/api/v1/auth/register',
          {
            identity: this.identity,
            name: this.name,
            surname: this.surname,
            age: this.age,
            gender: this.gender,
            username: this.username,
            password: this.password,
            role: this.role
          }
        );


        this.message = 'Registration successful!';
      } catch (err) {

        console.error(err.response?.data);
        this.message = 'Registration failed! ' + (err.response?.data?.message || '');
      }
    }
  }
};
</script>
