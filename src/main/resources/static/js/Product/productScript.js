let currentPage = 0;
const size = 10; // Количество товаров на странице


function loadProducts(page, seller) {
    $.get(`/api/v1/store/products?login=${seller}&page=${page}`)
        .done(function(data) {
            $('#product-list').empty(); // Очистить текущий список
            if (data.content) {
                data.content.forEach(product => {
                    $('#product-list').append(`<div>${product.name} - ${product.price}</div>`);
                });
            } else {
                console.error("Полученные данные не содержат 'content':", data);
            }

            // Управление кнопками пагинации
            $('#prev-page').toggle(page > 0);
            $('#next-page').toggle(page < data.totalPages - 1);
        })
        .fail(function(xhr) {
            console.error("Ошибка при загрузке товаров:", xhr.responseText);
        });
}


$(document).ready(function() {
    loadProducts(currentPage, seller); // Используйте seller здесь

    $('#prev-page').click(function() {
        currentPage--;
        loadProducts(currentPage, seller); // Передайте seller
    });

    $('#next-page').click(function() {
        currentPage++;
        loadProducts(currentPage, seller); // Передайте seller
    });
});
