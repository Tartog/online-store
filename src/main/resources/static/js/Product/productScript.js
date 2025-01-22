let currentPage = 0;
const size = 10; // Количество товаров на странице


function loadProducts(page, seller) {
    $.get(`/api/v1/store/products?login=${seller}&page=${page}`)
        .done(function(data) {
            $('#product-list').empty(); // Очистить текущий список
            if (data.content) {
                data.content.forEach(function(product) {
                    $('#product-list').append(`
                    <div>
                        <span>${product.name}</span>
                        <button class="update-button" data-id="${product.id}">Редактировать</button>
                        <button class="delete-button" data-id="${product.id}">Удалить</button>
                        </hr>
                    </div>
                `);
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

    $(document).on('click', '.delete-button', function() {
        const productId = $(this).data('id');
        if (confirm("Вы уверены, что хотите удалить этот товар?")) {
            $.ajax({
                url: `/api/v1/store/products/deleteProduct/${productId}`,
                method: 'DELETE',
                success: function() {
                    loadProducts(currentPage, seller);
                },
                error: function(xhr, status, error) {
                    console.error("Ошибка при удалении товара:", error);
                }
            });
        }
    });

    $('#prev-page').click(function() {
        currentPage--;
        loadProducts(currentPage, seller); // Передайте seller
    });

    $('#next-page').click(function() {
        currentPage++;
        loadProducts(currentPage, seller); // Передайте seller
    });
});
