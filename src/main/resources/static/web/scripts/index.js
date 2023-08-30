const { createApp } = Vue

createApp({
    data() {
        return {
            email: '',
            password: '',
            firstName: '',
            lastName: '',
            errorMessage: '',
        }
    },
    methods: {
        login() {
            axios.post('http://localhost:8080/api/login', `email=${this.email}&password=${this.password}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => {
                    if (this.email.includes("admin.mindhub")) {
                        location.href = '../manager.html';
                    } else {
                        location.href = './accounts.html';
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    Swal.fire({
                        icon: 'error',
                        title: 'Invalid email or password',
                        text: error.message,
                        confirmButtonText: 'OK',
                        customClass: {
                            popup: 'custom-alert',
                        }
                    });
                })
        },
        logout() {
            axios.post('http://localhost:8080/api/logout')
                .then(response => {
                    location.href = './web/index.html';
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
                    Swal.fire({
                        icon: 'error',
                        title: error.response.data,
                        text: 'Please enter another email.',
                        confirmButtonText: 'OK',
                        customClass: {
                            popup: 'custom-alert',
                        }
                    });
                })
        }
    }
}).mount('#app')
