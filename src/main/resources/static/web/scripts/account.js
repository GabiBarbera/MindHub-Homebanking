const { createApp } = Vue


createApp({
    data() {
        return {
            account: [],
            transactions: [],
            date: [],
            hour: [],
            dateForm: {},
            client: [],
            balance: "",
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            const storedClients = localStorage.getItem('clients');
            this.client = JSON.parse(storedClients);
            const parameter = location.search
            const parameters = new URLSearchParams(parameter)
            const idParameters = parameters.get("id")
            axios.get('/api/clients/accounts/' + idParameters) // {headers:{'accept':'application/xml'}}
                .then(response => {
                    this.account = response.data
                    this.balance = this.account.balance.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
                    this.transactions = this.account.transactions.sort((a, b) => b.id - a.id)
                    this.date = this.transactions.map(tr => tr.date.slice(0, -16))
                    this.hour = this.transactions.map(tr => tr.date.slice(11, -7))
                    this.dateForm.date = this.date
                    this.dateForm.hour = this.hour
                })
                .catch(error => {
                    console.error('Error:', error);
                    location.href = './error.html';
                })
        },
        logout() {
            axios.post('/api/logout')
                .then(response => {
                    location.href = '/web/index.html';
                })
                .catch(error => {
                    console.error('Error:', error);
                })
        }
    }
}).mount('#app')