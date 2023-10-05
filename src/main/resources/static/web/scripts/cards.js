const { createApp } = Vue


createApp({
    data() {
        return {
            credit: [],
            debit: [],
            cards: [],
            dateDebit: [],
            fromDateDebit: [],
            dateCredit: [],
            fromDateCredit: [],
            dateNao: new Date().toISOString().slice(2, 7).replace(/-/g, '/'),
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
                    this.cards = this.clients.cards.sort((a, b) => b.id - a.id).filter(card => card.active)
                    this.debit = this.cards.filter(card => card.type == 'DEBIT')
                    this.credit = this.cards.filter(card => card.type == 'CREDIT')
                    this.dateDebit = this.debit.map(date => date.thruDate.slice(2, 7).replace(/-/g, '/'))
                    this.fromDateDebit = this.debit.map(date => date.fromDate.slice(2, 7).replace(/-/g, '/'))
                    this.dateCredit = this.credit.map(date => date.thruDate.slice(2, 7).replace(/-/g, '/'))
                    this.fromDateCredit = this.credit.map(date => date.fromDate.slice(2, 7).replace(/-/g, '/'))
                    console.log(this.dateCredit);
                    console.log(this.dateNao);
                })
                .catch(error => console.error('Error:', error));
        },
        logout() {
            axios.post('/api/logout')
                .then(response => {
                    location.href = '/web/index.html';
                })
        },
        deactiveCard(id) {
            Swal.fire({
                title: 'Do you want to eliminate a card?',
                inputAttributes: {
                    autocapitalize: 'off',
                },
                showCancelButton: true,
                confirmButtonText: 'Sure',
                showLoaderOnConfirm: true,
                preConfirm: (login) => {
                    axios.patch('/api/clients/current/cards/deactivate', `id=${id}`)
                        .then(response => {
                            Swal.fire({
                                icon: 'succes',
                                title: response.data,
                                text: `Your card was eliminated successfully.`,
                                confirmButtonText: 'OK',
                                customClass: {
                                    popup: 'custom-alert',
                                }
                            });
                            setTimeout(() => {
                                location.href = './cards.html';
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
