const { createApp } = Vue

createApp({
    data() {
        return {
            clients: [],
            firstName: '',
            lastName: '',
            email: '',
            jsonRest: null,
            loanName: "",
            maxAmountLoan: null,
            paymentsLoan: [],
            interestLoan: null,
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            axios.get('/rest/clients')
                .then(response => {
                    this.clients = response.data._embedded.clients
                    this.jsonRest = JSON.stringify(response.data, null, 1);
                })
                .catch(error => console.error('Error:', error));
        },
        inputCheck() {
            if (this.firstName && this.lastName && this.email) {
                this.addClient();
            } else {
                window.alert('Please complete the form');
            }
        },
        addClient() {
            let clientNew = {
                firstName: this.firstName,
                lastName: this.lastName,
                email: this.email,
            }
            axios.post("/rest/clients", clientNew)
                .then(response => {
                    this.firstName = '';
                    this.lastName = '';
                    this.email = '';
                    this.loadData();
                })
                .catch(error => console.error('Error:', error));
        },
        logout() {
            axios.post('/api/logout')
                .then(response => {
                    location.href = '/web/index.html';
                })
                .catch(error => console.error('Error:', error));
        },
        createLoan() {
            let newLoan = {
                name: this.loanName,
                maxAmount: this.maxAmountLoan,
                payments: this.paymentsLoan,
                interest: this.interestLoan
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
                    return axios.post('/api/create/loans', newLoan)
                        .then(response => {
                            Swal.fire({
                                icon: 'succes',
                                title: "Loan created",
                                confirmButtonText: 'OK',
                                customClass: {
                                    popup: 'custom-alert',
                                }
                            });
                            setTimeout(() => {
                                location.href = './manager.html';
                            }, 4000);
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
