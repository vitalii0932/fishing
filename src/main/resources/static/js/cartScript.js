function buy() {

    window.location.href = '/index/buy';
}

function deleteProduct(button) {
    var productId = button.getAttribute('data-id');

    fetch('/index/shoppingCart/deleteProduct/' + productId, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log('Запрос успешно выполнен', data);
            alert(data);
        })
        .catch(error => {
            console.error('Произошла ошибка при выполнении запроса', error);
        });

    setTimeout(() => {
        window.location.href = `/index/shoppingCart`;
    }, 500);
}