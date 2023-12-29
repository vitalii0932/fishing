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
            let centeredBlock = document.getElementById('centeredBlock');
            centeredBlock.style.display = 'block';

            setTimeout(function() {
                centeredBlock.style.display = 'none';
            }, 1500);
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

let swiper = new Swiper(".mySwiper", {
    spaceBetween: 30,
    navigation: {
        nextEl: ".swiper-button-next",
        prevEl: ".swiper-button-prev",
    },
    pagination: {
        el: ".swiper-pagination",
    },
    mousewheel: true,
    keyboard: true,
});
