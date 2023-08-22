const { createApp } = Vue

const url = 'http://localhost:8080/api/login'

createApp({
    data() {
        return {
            email: '',
            password: '',
            errorMessage: '',
        }
    },
    methods: {
        login() {
            axios.post(url, `email=${this.email}&password=${this.password}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => {
                    console.log(response);
                    location.href = '/web/accounts.html';
                })
                .catch(error => {
                    this.errorMessage = "Error";
                    console.error('Error:', error);
                });
        },
        logout() {
            axios.post('http://localhost:8080/api/logout')
                .then(response => {
                    location.href = '/web/index.html';
                })
        }
    }
}).mount('#app')