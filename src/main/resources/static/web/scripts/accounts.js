const { createApp } = Vue

const url = 'http://localhost:8080/api/clients/1'

createApp({
    data() {
        return {
            accounts: [],
            clients: [],
            loans: [],
            loansConvert: [],
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
                    this.accounts = response.data.accounts.sort((a, b) => a.id - b.id)
                    this.loans = this.clients.clientLoans
                    this.loansConvert = this.loans.map(loan => loan.amount.toLocaleString(1))
                    localStorage.setItem('clients', JSON.stringify(this.clients));
                    console.log(this.loansConvert);
                })
                .catch(error => console.error('Error:', error));
        }
    }
}).mount('#app')