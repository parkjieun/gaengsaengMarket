import axios from "axios"

export default axios.create({
  baseURL: "http://i3a504.p.ssafy.io:8003",
  // baseURL:"http://localhost:8003",
  headers: {
    "Content-type": "application/json"
  }
})
