let currentPage = 0;
let login = '';

function loadProducts(page, login = '') {
    let url = `/api/v1/store/products/${login}?page=${page}`;
    this.login = login;

    $.ajax({
        url: url,
        method: 'GET',
        success: function(data) {
            $('#product-list').empty();
            data.content.forEach(function(product) {
                $('#product-list').append(`
                <div>
                    <span>${product.name + ', Цена: ' + product.price + ', Количество на складе: '
                + product.numberOfProducts}</span>
                    <hr>
                </div>
            `);
            });

            // Управление кнопками пагинации
            $('#prev-page').toggle(page > 0);
            $('#next-page').toggle(data.totalPages > page + 1);
        },
        error: function(xhr, status, error) {
            console.error("Ошибка при загрузке товаров:", error);
        }
    });
}

$(document).ready(function() {
    loadProducts(currentPage, login);

    $(document).on('click', '.add-product-button', function() {
        const addressId = $(this).data('id');
        // Получите данные адреса, используя его ID
        $.ajax({
            url: `/api/v1/store/deliveryAddress/${addressId}`, // Правильный URL для получения адреса
            method: 'GET',
            success: function(deliveryAddress) {
                // Заполните форму редактирования
                $('#update-id').val(deliveryAddress.id);
                $('#update-city').val(deliveryAddress.city);
                $('#update-street').val(deliveryAddress.street);
                $('#update-houseNumber').val(deliveryAddress.houseNumber);
                $('#update-address-form').show(); // Покажите форму редактирования
            },
            error: function(xhr, status, error) {
                console.error("Ошибка при получении адреса:", error);
                let errorMessage = xhr.responseText || "Произошла ошибка при получении адреса.";
                alert(errorMessage);
            }
        });
    });

    $('#prev-page').click(function() {
        if (currentPage > 0) {
            currentPage--;
            loadProducts(currentPage, login);
        }
    });

    $('#next-page').click(function() {
        currentPage++;
        loadProducts(currentPage, login);
    });
});
