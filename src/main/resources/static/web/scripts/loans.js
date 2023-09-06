const { createApp } = Vue

const url = '/api/clients/current/accounts'

createApp({
    data() {
        return {
            amount: null,
            payments: null,
            loans: [],
            loanSelect: "",
            accountSelect: "",
            accounts: null,
            paymentSelect: "",
        }
    },
    created() {
        this.loadData()
        this.loansData()
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
        loansData() {
            axios.get("/api/loans")
                .then(response => {
                    this.loans = response.data
                    console.log(this.loans);
                })
                .catch(error => console.error('Error:', error));
        },
        logout() {
            axios.post('/api/logout')
                .then(response => {
                    location.href = '/web/index.html';
                })
        },
        createLoans() {
            let object = {
                "id": this.loanSelect.id,
                "amount": this.amount,
                "payments": this.paymentSelect,
                "numberAccountDestination": this.accountSelect
            }
            Swal.fire({
                title: 'Do you want to create a new loan?',
                inputAttributes: {
                    autocapitalize: 'off',
                },
                showCancelButton: true,
                confirmButtonText: 'Sure',
                showLoaderOnConfirm: true,
                preConfirm: (login) => {
                    return axios.post('/api/loans', object)
                        .then(response => {
                            Swal.fire({
                                icon: 'succes',
                                title: "loan approved.",
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

