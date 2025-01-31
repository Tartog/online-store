let currentPage = 0;

function loadProducts(page, seller) {
    $.get(`/api/v1/store/products?login=${seller}&page=${page}`)
        .done(function(data) {
            $('#product-list').empty();
            if (data.content) {
                data.content.forEach(function(product) {
                    $('#product-list').append(`
                    <div class="address-item">
                        <span class="address-text">${product.name}</span>
                        <button class="delete-button" data-id="${product.id}">Удалить</button>
                    </div>
                `);
                });
            } else {
                console.error("Полученные данные не содержат 'content':", data);
            }

            $('#prev-page').toggle(page > 0);
            $('#next-page').toggle(page < data.totalPages - 1);
        })
        .fail(function(xhr) {
            console.error("Ошибка при загрузке товаров:", xhr.responseText);
        });
}


$(document).ready(function() {
    loadProducts(currentPage, seller);

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
        loadProducts(currentPage, seller);
    });

    $('#next-page').click(function() {
        currentPage++;
        loadProducts(currentPage, seller);
    });
});