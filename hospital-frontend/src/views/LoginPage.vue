<script>
import axios from "axios";

export default {
  data() {
    return {
      username: "",
      password: "",
      message: ""
    };
  },
  methods: {
    async login() {
      try {
        const res = await axios.post("http://localhost:8082/api/v1/auth/login", {
          username: this.username,
          password: this.password
        });

        // backend role ve token dönmeli
        localStorage.setItem("token", res.data.token);
        localStorage.setItem("role", res.data.role);
        if (res.data.role === "ROLE_DOCTOR") {
          this.$router.push("/prescriptions");
        } else if (res.data.role === "ROLE_USER") {
          this.$router.push("/prescriptionsUser");
        }
      } catch (err) {
        console.error(err);
        this.message = "Login failed!";
      }
    }
  }
};
</script>
