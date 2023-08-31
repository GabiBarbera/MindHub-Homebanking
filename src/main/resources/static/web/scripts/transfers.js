const { createApp } = Vue

const url = '/api/clients/current/accounts'

createApp({
    data() {
        return {
            amount: "",
            description: "",
            accountOrigin: "",
            accountDestiny: "",
            numbers: "",
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            axios.get(url)
                .then(response => {
                    this.accounts = response.data
                    this.numbers = this.accounts.map(account => account.number)
                    console.log(this.accounts);
                    console.log(this.numbers);
                })
                .catch(error => console.error('Error:', error));
        },
        logout() {
            axios.post('/api/logout')
                .then(response => {
                    location.href = '/web/index.html';
                })
        },
        createTransfer() {
            Swal.fire({
                title: 'Do you want to create a new transfer?',
                inputAttributes: {
                    autocapitalize: 'off',
                },
                showCancelButton: true,
                confirmButtonText: 'Sure',
                showLoaderOnConfirm: true,
                preConfirm: (login) => {
                    return axios.post('/api/transactions', `amount=${this.amount}&description=${this.description}&accountOrigin=${this.accountOrigin}&accountDestiny=${this.accountDestiny}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                        .then(response => {
                            location.href = './accounts.html'
                        })
                        .catch(error => {
                            console.error('Error:', error);
                            Swal.fire({
                                icon: 'error',
                                title: error.response.data,
                                text: `Please try again`,
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