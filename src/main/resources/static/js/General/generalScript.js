let currentPage = 1;
const itemsPerPage = 16;

function displayItems() {
    const items = document.querySelectorAll('.product-item');
    const totalPages = Math.ceil(items.length / itemsPerPage);

    // Скрыть все товары
    items.forEach((item, index) => {
        item.style.display = 'none'; // Скрываем все товары
    });

    // Определяем индекс начала и конца для текущей страницы
    const start = (currentPage - 1) * itemsPerPage;
    const end = start + itemsPerPage;

    // Отображаем товары для текущей страницы
    for (let i = start; i < end && i < items.length; i++) {
        items[i].style.display = 'block'; // Показываем товары для текущей страницы
    }

    // Обновляем информацию о текущей странице
    document.getElementById('pageInfo').textContent = `Страница ${currentPage} из ${totalPages}`;

    // Управление кнопками "Назад" и "Вперед"
    document.getElementById('prevPage').disabled = currentPage === 1; // Отключаем кнопку "Назад", если на первой странице
    document.getElementById('nextPage').disabled = currentPage === totalPages; // Отключаем кнопку "Вперед", если на последней странице
}

function changePage(direction) {
    currentPage += direction; // Изменяем текущую страницу
    displayItems(); // Обновляем отображение товаров
}

// Инициализация отображения товаров при загрузке страницы
document.addEventListener('DOMContentLoaded', () => {
    displayItems();
    // Обработчик событий для кнопок "Добавить в корзину"
    const addToCartButtons = document.querySelectorAll('.add-to-cart');
    addToCartButtons.forEach(button => {
        button.addEventListener('click', function() {
            const login = this.getAttribute('data-login');
            const productId = this.getAttribute('data-productId');
            addToCart(login, productId);
        });
    });

    function addToCart(login, productId) {
        // Создаем AJAX-запрос
        const xhr = new XMLHttpRequest();
        xhr.open('PATCH', `/api/v1/store/addProductToCart/${login}/${productId}`, true);
        xhr.setRequestHeader('Content-Type', 'application/json');

        xhr.onload = function () {
            if (xhr.status === 200) {
                // Обработайте успешный ответ
                console.log('Товар добавлен в корзину');
            } else {
                // Обработайте ошибку
                console.error('Ошибка при добавлении товара в корзину');
                alert('Ошибка при добавлении товара в корзину');
            }
        };

        // Отправляем запрос
        xhr.send();
    }
});
