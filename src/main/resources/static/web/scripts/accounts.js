const { createApp } = Vue

const url = 'http://localhost:8080/api/clients/1'

createApp({
    data() {
        return {
            accounts: [],
            clients: [],
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
                })
                .catch(error => console.error('Error:', error));
        }
    }
}).mount('#app')