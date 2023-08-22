const { createApp } = Vue

const url = 'http://localhost:8080/api/clients/1'

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
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            axios.get(url)
                .then(response => {
                    this.clients = response.data
                    this.cards = this.clients.cards.sort((a, b) => b.id - a.id)
                    this.debit = this.cards.filter(card => card.type == 'DEBIT')
                    this.credit = this.cards.filter(card => card.type == 'CREDIT')
                    this.dateDebit = this.debit.map(date => date.thruDate.slice(2, 7).replace(/-/g, '/'))
                    this.fromDateDebit = this.debit.map(date => date.fromDate.slice(2, 7).replace(/-/g, '/'))
                    this.dateCredit = this.credit.map(date => date.thruDate.slice(2, 7).replace(/-/g, '/'))
                    this.fromDateCredit = this.credit.map(date => date.fromDate.slice(2, 7).replace(/-/g, '/'))
                })
                .catch(error => console.error('Error:', error));
        },
        logout() {
            axios.post('http://localhost:8080/api/logout')
                .then(response => {
                    location.href = '/web/index.html';
                })
        }
    }
}).mount('#app')