const { createApp } = Vue

const url = 'http://localhost:8080/api/clients/current/accounts'

createApp({
    data() {
        return {
            amount: "",
            description: "",
            accountOrigin: "",
            accountDestiny: "",
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
                    console.log(this.accounts);
                })
                .catch(error => console.error('Error:', error));
        },
        logout() {
            axios.post('http://localhost:8080/api/logout')
                .then(response => {
                    location.href = '/web/index.html';
                })
        },
        createTransfer() {
            Swal.fire({
                title: 'Do you want to create a new card?',
                inputAttributes: {
                    autocapitalize: 'off',
                },
                showCancelButton: true,
                confirmButtonText: 'Sure',
                showLoaderOnConfirm: true,
                preConfirm: (login) => {
                    return axios.post(`localhost:8080/api/transactions`, `amount=${this.amount}&description=${this.description}&accountOrigin=${this.accountOrigin}&accountDestiny=${this.accountDestiny}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                        .then(response => {
                            location.href = './cards.html'
                        })
                        .catch(error => {
                            console.error('Error:', error);
                            Swal.fire({
                                icon: 'error',
                                title: error.response.data,
                                text: `Please create a card you don't own`,
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