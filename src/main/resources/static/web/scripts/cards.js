const { createApp } = Vue

const url = 'http://localhost:8080/api/clients/1'

createApp({
    data() {
        return {
            accounts: [],
            credit: [],
            debit: [],
            cards: []
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
                    this.cards = this.clients.cards
                    this.debit = this.cards.filter(card => card.type == 'DEBIT')
                    this.credit = this.cards.filter(card => card.type == 'CREDIT')
                    this.cards = this.clients.cards.sort((a, b) => b.id - a.id)
                })
                .catch(error => console.error('Error:', error));
        }
    }
}).mount('#app')