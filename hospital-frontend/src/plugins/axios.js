import axios from "axios";

const baseURL = import.meta.env.VITE_BASE_URL_KEY || "/api";

const axiosIns = axios.create({
  baseURL,
  headers: {
    'Accept': 'application/json',
    //"Access-Control-Allow-Origin": "*"
  }
});

export default axiosIns;
