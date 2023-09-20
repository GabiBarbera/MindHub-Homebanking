const { createApp } = Vue

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
            finalAmount: null,
        }
    },
    created() {
        this.loadData()
        this.loansData()
    },
    methods: {
        loadData() {
            axios.get('/api/clients/current/accounts')
                .then(response => {
                    this.accounts = response.data
                })
                .catch(error => console.error('Error:', error));
        },
        loansData() {
            axios.get("/api/loans")
                .then(response => {
                    this.loans = response.data
                    console.log(this.finalAmount);
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
            console.log(object);
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
    },
    computed: {
        calculateInterest() {
            if (this.paymentSelect == 3) {
                this.finalAmount = this.amount + (this.amount * 0.05)
                return this.finalAmount;
            }
            else if (this.paymentSelect == 6) {
                this.finalAmount = this.amount + (this.amount * 0.10)
                return this.finalAmount;
            }
            else if (this.paymentSelect == 12) {
                this.finalAmount = this.amount + (this.amount * 0.20)
                return this.finalAmount;
            }
            else if (this.paymentSelect == 24) {
                this.finalAmount = this.amount + (this.amount * 0.45)
                return this.finalAmount;
            }
            else if (this.paymentSelect == 36) {
                this.finalAmount = this.amount + (this.amount * 0.65)
                return this.finalAmount;
            }
            else if (this.paymentSelect == 48) {
                this.finalAmount = this.amount + (this.amount * 0.70)
                return this.finalAmount;
            }
            else if (this.paymentSelect == 60) {
                this.finalAmount = this.amount + (this.amount * 0.75)
                return this.finalAmount;
            } else { return 0 };
        }
    }
}).mount('#app')

