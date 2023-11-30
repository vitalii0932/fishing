function buyProduct(button) {
    var productId = button.getAttribute('data-id');

    // Выполнение AJAX-запроса после нажатия кнопки
    fetch('/index/shop/buy/' + productId, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json', // Установите заголовок, если это необходимо
            // Другие заголовки могут быть добавлены здесь
        },
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json(); // Или response.text(), в зависимости от того, какой формат вы ожидаете
        })
        .then(data => {
            console.log('Запрос успешно выполнен', data);
            alert(data);
            // Дополнительные действия после успешного запроса
        })
        .catch(error => {
            console.error('Произошла ошибка при выполнении запроса', error);
        });
}