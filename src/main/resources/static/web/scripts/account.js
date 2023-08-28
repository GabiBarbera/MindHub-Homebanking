const { createApp } = Vue

const url = 'http://localhost:8080/api/clients/accounts/'

createApp({
    data() {
        return {
            account: [],
            transactions: [],
            date: [],
            hour: [],
            dateForm: {},
            client: [],
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
            axios.get(url + idParameters)
                .then(response => {
                    this.account = response.data
                    this.transactions = this.account.transactions.sort((a, b) => b.id - a.id)
                    this.date = this.transactions.map(tr => tr.date.slice(0, -16))
                    this.hour = this.transactions.map(tr => tr.date.slice(11, -7))
                    this.dateForm.date = this.date
                    this.dateForm.hour = this.hour
                })
                .catch(error => {
                    console.error('Error:', error);
                    Swal.fire({
                        icon: 'error',
                        title: error.response.data,
                        text: "You don't have permissions, not your account.",
                        confirmButtonText: 'OK',
                        customClass: {
                            popup: 'custom-alert',
                        }
                    });
                })
        },
        logout() {
            axios.post('http://localhost:8080/api/logout')
                .then(response => {
                    location.href = '/web/index.html';
                })
                .catch(error => {
                    console.error('Error:', error);
                })
        }
    }
}).mount('#app')