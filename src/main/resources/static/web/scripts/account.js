const { createApp } = Vue

const url = 'http://localhost:8080/api/accounts/'

createApp({
    data() {
        return {
            account: [],
            transactions: [],
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
                })
                .catch(error => console.error('Error:', error));
        }
    }
}).mount('#app')