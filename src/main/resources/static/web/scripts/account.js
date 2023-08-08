const { createApp } = Vue

const url = 'http://localhost:8080/api/accounts'

createApp({
    data() {
        return {
            accountId: [],
            transactions: [],
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            axios.get(url)
                .then(response => {
                    const parameter = location.search
                    const parameters = new URLSearchParams(parameter)
                    const idParameters = parameters.get("id")
                    const allAccounts = response.data
                    this.accountId = allAccounts.find(idAccount => idAccount.id == idParameters)
                    this.transactions = this.accountId.transactionDTOSet.sort((a, b) => b.id - a.id)
                })
                .catch(error => console.error('Error:', error));
        }
    }
}).mount('#app')