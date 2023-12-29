function updatePosition() {
    let centeredBlock = document.getElementById('centeredBlock');
    centeredBlock.style.top = '50%';
    centeredBlock.style.left = '50%';
    centeredBlock.style.transform = 'translate(-50%, -50%)';
}

window.addEventListener('load', updatePosition);
window.addEventListener('resize', updatePosition);

function buyProduct(button) {
    let productId = button.getAttribute('data-id');

    // Виконання AJAX-запиту після натискання кнопки
    fetch('/index/shop/buy/' + productId, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json', // Встановлюємо заголовок, якщо потрібно
            // Інші заголовки можуть бути додані тут
        },
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            let centeredBlock = document.getElementById('centeredBlock');
            centeredBlock.style.display = 'block';

            setTimeout(function() {
                centeredBlock.style.display = 'none';
            }, 1500);
            return response.json(); // Або response.text(), залежно від очікуваного формату
        })
        .then(data => {
            console.log('Запит успішно виконаний', data);
            alert(data);
            // Додаткові дії після успішного запиту
        })
        .catch(error => {
            console.error('Помилка при виконанні запиту', error);
        });
}