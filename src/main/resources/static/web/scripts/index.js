const { createApp } = Vue


createApp({
    data() {
        return {
            email: '',
            password: '',
            firstName: '',
            lastName: '',
        }
    },
    methods: {
        login() {
            axios.post('http://localhost:8080/api/login', `email=${this.email}&password=${this.password}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => {
                    if ("admin@admin".includes(this.email)) {
                        location.href = '../manager.html';
                    } else {
                        location.href = './accounts.html';
                    }
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
                .catch(error => {
                    console.error('Error:', error);
                });
        },
        signUp() {
            axios.post('http://localhost:8080/api/clients', `firstName=${this.firstName}&lastName=${this.lastName}&email=${this.email}&password=${this.password}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => {
                    this.login()
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        }
    }
}).mount('#app')
