let currentPage = 0;

function loadAddresses(page) {
    $.ajax({
        url: '/api/v1/store/deliveryAddress?page=' + page,
        method: 'GET',
        success: function(data) {
            $('#address-list').empty(); // Очистить текущий список адресов
            data.content.forEach(function(deliveryAddress) {
                $('#address-list').append(`
                <div>
                    <span>${'г. ' + deliveryAddress.city + ', ул. ' + deliveryAddress.street + '  д. №' + deliveryAddress.houseNumber}</span>
                    <button class="delete-button" data-id="${deliveryAddress.id}">Удалить</button>
                    <hr>
                </div>
            `);
            });

            // Управление кнопками пагинации
            $('#prev-page').toggle(page > 0);
            $('#next-page').toggle(data.totalPages > page + 1);
        },
        error: function(xhr, status, error) {
            console.error("Ошибка при загрузке адресов:", error);
        }
    });
}

$(document).ready(function() {
    loadAddresses(currentPage); // Загрузить адреса при загрузке страницы

    // Обработчик события для кнопок удаления
    $(document).on('click', '.delete-button', function() {
        const addressId = $(this).data('id');
        if (confirm("Вы уверены, что хотите удалить этот адрес?")) {
            $.ajax({
                url: `/api/v1/store/deliveryAddress/deleteAddress/${addressId}`,
                method: 'DELETE',
                success: function() {
                    // После удаления, перезагрузим адреса
                    loadAddresses(currentPage);
                },
                error: function(xhr, status, error) {
                    console.error("Ошибка при удалении адреса:", error);
                }
            });
        }
    });

    $('#prev-page').click(function() {
        if (currentPage > 0) {
            currentPage--;
            loadAddresses(currentPage);
        }
    });

    $('#next-page').click(function() {
        currentPage++;
        loadAddresses(currentPage);
    });

    // Обработчик для кнопки добавления нового адреса
    $('#add-address-button').click(function() {
        $('#new-address-form').toggle(); // Показать или скрыть форму
    });

    // Обработчик отправки формы
    $('#address-form').submit(function(event) {
        event.preventDefault(); // Предотвратить стандартное поведение формы

        const newAddress = {
            city: $('#city').val(),
            street: $('#street').val(),
            houseNumber: $('#houseNumber').val()
        };

        $.ajax({
            url: '/api/v1/store/deliveryAddress/newAddress',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(newAddress),
            success: function(response) {
                $('#new-address-form').hide(); // Скрыть форму после успешного добавления
                loadAddresses(currentPage); // Обновить список адресов
                $('#address-form')[0].reset(); // Очистить форму
            },
            error: function(xhr, error) {
                console.error("Ошибка при добавлении адреса:", error);
                let errorMessage = xhr.responseText || "Произошла ошибка при добавлении адреса."; // Получаем сообщение об ошибке
                alert(errorMessage); // Выводим сообщение об ошибке
            }
        });
    });
});
