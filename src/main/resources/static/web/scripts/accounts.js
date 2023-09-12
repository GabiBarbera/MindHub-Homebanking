const { createApp } = Vue

const url =

    createApp({
        data() {
            return {
                accounts: [],
                clients: [],
                loans: [],
                loansConvert: [],
            }
        },
        created() {
            this.loadData()
        },
        methods: {
            loadData() {
                axios.get('/api/clients/current')
                    .then(response => {
                        this.clients = response.data
                        this.accounts = this.clients.accounts.sort((a, b) => a.id - b.id)
                        this.loans = this.clients.loans.sort((a, b) => b.id - a.id)
                        this.loansConvert = this.loans.map(loan => loan.amount.toLocaleString(1))
                        localStorage.setItem('clients', JSON.stringify(this.clients));
                    })
                    .catch(error => console.error('Error:', error));
            },
            logout() {
                axios.post('/api/logout')
                    .then(response => {
                        location.href = '/web/index.html';
                    })
            },
            createAccount() {
                Swal.fire({
                    title: 'Do you want to create a new account?',
                    inputAttributes: {
                        autocapitalize: 'off',
                    },
                    showCancelButton: true,
                    confirmButtonText: 'Sure',
                    showLoaderOnConfirm: true,
                    preConfirm: (login) => {
                        return axios.post(`/api/clients/current/accounts`)
                            .then(response => {
                                location.href = './accounts.html'
                            })
                            .catch(error => {
                                Swal.fire({
                                    icon: 'error',
                                    title: error.response.data,
                                    text: "You can only have a maximum of three accounts.",
                                    confirmButtonText: 'OK',
                                    customClass: {
                                        popup: 'custom-alert',
                                    }
                                });
                            })
                    },
                    allowOutsideClick: () => !Swal.isLoading(),
                })
            }
        }
    }).mount('#app')