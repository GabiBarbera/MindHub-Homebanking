const { createApp } = Vue

const url = 'http://localhost:8080/api/accounts/'

createApp({
    data() {
        return {
            account: [],
            transactions: [],
            date: [],
            hour: [],
            dateForm: {},
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            const parameter = location.search
            const parameters = new URLSearchParams(parameter)
            const idParameters = parameters.get("id")
            axios.get(url + idParameters)
                .then(response => {
                    this.account = response.data
                    this.transactions = this.account.transactionDTOSet.sort((a, b) => b.id - a.id)
                    this.date = this.transactions.map(tr => tr.date.slice(0, -16))
                    this.hour = this.transactions.map(tr => tr.date.slice(11, -7))
                    this.dateForm.date = this.date
                    this.dateForm.hour = this.hour
                })
                .catch(error => console.error('Error:', error));
        }
    }
}).mount('#app')