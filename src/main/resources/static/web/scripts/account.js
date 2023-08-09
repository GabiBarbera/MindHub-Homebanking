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
                    console.log(this.account);
                    this.transactions = this.account.transactionDTOSet
                    console.log(this.transactions);
                    // const account = response.data
                    // this.accountId = allAccounts.find(idAccount => idAccount.id == idParameters)
                    // this.transactions = this.accountId.transactionDTOSet.sort((a, b) => b.id - a.id)
                })
                .catch(error => console.error('Error:', error));
        }
    }
}).mount('#app')