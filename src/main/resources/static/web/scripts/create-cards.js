const { createApp } = Vue

const url = 'http://localhost:8080/api/clients/current'

createApp({
    data() {
        return {
            cardType: "",
            cardColor: "",
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            axios.get(url)
                .then(response => {
                })
                .catch(error => console.error('Error:', error));
        },
        logout() {
            axios.post('http://localhost:8080/api/logout')
                .then(response => {
                    location.href = '/web/index.html';
                })
        },
        createCard() {
            axios.post('http://localhost:8080/api/clients/current/cards', `type=${this.cardType}&color=${this.cardColor}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => {
                    location.href = './cards.html'
                })
                .catch(error => {
                    console.error('Error:', error);
                    Swal.fire({
                        icon: 'error',
                        title: error.response.data,
                        text: `Please create a card you don't own`,
                        confirmButtonText: 'OK',
                        customClass: {
                            popup: 'custom-alert',
                        }
                    });
                })
        }
    }
}).mount('#app')