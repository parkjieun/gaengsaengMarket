import axios from "axios"

export default axios.create({
    baseURL: "http://localhost:8001",
//   baseURL: "http://i3a504.p.ssafy.io:8001",
  headers: { 
  }
})
