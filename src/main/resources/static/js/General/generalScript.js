let currentPage = 1;
const itemsPerPage = 10;

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
});
