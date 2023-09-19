const { createApp } = Vue

const url = createApp({
    data() {
        return {
            accounts: [],
            clients: [],
            loans: [],
            loansConvert: [],
            accountType: "",
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
                    this.accounts = this.clients.accounts.sort((a, b) => a.id - b.id).filter(account => account.active)
                    this.loans = this.clients.loans.sort((a, b) => b.id - a.id)
                    this.loansConvert = this.loans.map(loan => loan.amount.toLocaleString(1))
                    console.log(this.clients);
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
                html: `
                <div class="radio-group">
                      <p class="text-black fw-bold">Choose the type of account you want.</p>
                  <div class="d-flex justify-content-center gap-4">
                    <label class="text-black" for="current">Current:</label>
                      <input class="text-black" type="radio" name="account" id="current" value="CURRENT">
                  </div>
                  <div class="d-flex justify-content-center gap-4 mt-2">
                      <label class="text-black" for="savings">Savings:</label>
                      <input class="text-black" type="radio" name="account" id="savings" value="SAVINGS">
                  </div>
                </div>
                `,
                inputAttributes: {
                    autocapitalize: 'off',
                },
                showCancelButton: true,
                confirmButtonText: 'Sure',
                showLoaderOnConfirm: true,
                preConfirm() {
                    const selected = document.querySelector("input[name=account]:checked")
                    return axios.post(`/api/clients/current/accounts`, `type=${selected.value}`)
                        .then(response => {
                            location.href = './accounts.html'
                        })
                        .catch(error => {
                            Swal.fire({
                                icon: 'error',
                                title: error.response.data,
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
        deactiveAccount(id) {
            Swal.fire({
                title: 'Do you want to eliminate a account?',
                inputAttributes: {
                    autocapitalize: 'off',
                },
                showCancelButton: true,
                confirmButtonText: 'Sure',
                showLoaderOnConfirm: true,
                preConfirm() {
                    axios.patch('/api/clients/current/accounts/deactivate', `id=${id}`)
                        .then(response => {
                            Swal.fire({
                                icon: 'succes',
                                title: error.response.data,
                                confirmButtonText: 'OK',
                                customClass: {
                                    popup: 'custom-alert',
                                }
                            });
                            setTimeout(() => {
                                location.href = './accounts.html';
                            }, 2000);
                        })
                        .catch(error => {
                            console.error('Error:', error);
                            Swal.fire({
                                icon: 'error',
                                title: error.response.data,
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
