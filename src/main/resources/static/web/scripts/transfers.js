const { createApp } = Vue

createApp({
    data() {
        return {
            amount: "",
            description: "",
            accountOrigin: "",
            accountDestiny: "",
            numbers: "",
            selectedAccountType: "third",
            isVisible: false,
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            axios.get('/api/clients/current/accounts')
                .then(response => {
                    this.accounts = response.data
                    this.balance = this.accounts.map(account => account.balance)
                    this.numbers = this.accounts.map(account => account.number)
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
                            Swal.fire({
                                icon: 'succes',
                                title: "Transaction completed",
                                text: `Your transfer was sent successfully.`,
                                confirmButtonText: 'OK',
                                customClass: {
                                    popup: 'custom-alert',
                                }
                            });
                            setTimeout(() => {
                                location.href = './accounts.html';
                            }, 3000);
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
        },
        getAvailableBalance(accountNumber) {
            const account = this.accounts.find(account => account.number === accountNumber);
            if (account) {
                return account.balance;
            }
            return 0;
        },
        toggleVisibility() {
            this.isVisible = !this.isVisible;
        },
    }
}).mount('#app')

