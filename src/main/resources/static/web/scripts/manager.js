const { createApp } = Vue

createApp({
    data() {
        return {
            clients: [],
            firstName: '',
            lastName: '',
            email: '',
            jsonRest: null,
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            axios.get('/rest/clients')
                .then(response => {
                    this.clients = response.data._embedded.clients
                    this.jsonRest = JSON.stringify(response.data, null, 1);
                })
                .catch(error => console.error('Error:', error));
        },
        inputCheck() {
            if (this.firstName && this.lastName && this.email) {
                this.addClient();
            } else {
                window.alert('Please complete the form');
            }
        },
        addClient() {
            let clientNew = {
                firstName: this.firstName,
                lastName: this.lastName,
                email: this.email,
            }
            axios.post(url, clientNew)
                .then(response => {
                    this.firstName = '';
                    this.lastName = '';
                    this.email = '';
                    this.loadData();
                })
                .catch(error => console.error('Error:', error));
        },
        logout() {
            axios.post('/api/logout')
                .then(response => {
                    location.href = '/web/index.html';
                })
        }
    }
}).mount('#app')