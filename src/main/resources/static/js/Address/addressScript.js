let currentPage = 0;
let searchParams = {
    city: '',
    street: '',
    houseNumber: ''
};

function loadAddresses(page, params = {}) {
    let url = `/api/v1/store/deliveryAddress?page=${page}`;
    if (params.city) {
        url += `&city=${encodeURIComponent(params.city)}`;
    }
    if (params.street) {
        url += `&street=${encodeURIComponent(params.street)}`;
    }
    if (params.houseNumber) {
        url += `&houseNumber=${params.houseNumber}`;
    }

    $.ajax({
        url: url,
        method: 'GET',
        success: function(data) {
            $('#address-list').empty(); // Очистить текущий список адресов
            data.content.forEach(function(deliveryAddress) {
                $('#address-list').append(`
                <div class="address-item">
                    <span class="address-text">${'г. ' + deliveryAddress.city + ', ул. ' + deliveryAddress.street + '  д. №' + deliveryAddress.houseNumber}</span>
                    <button class="update-button" data-id="${deliveryAddress.id}">Редактировать</button>
                    <button class="delete-button" data-id="${deliveryAddress.id}">Удалить</button>
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

    $(document).on('click', '.update-button', function() {
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

    $('#update-form').submit(function(event) {
        event.preventDefault(); // Предотвратить стандартное поведение формы

        const updatedAddress = {
            id: $('#update-id').val(),
            city: $('#update-city').val(),
            street: $('#update-street').val(),
            houseNumber: $('#update-houseNumber').val()
        };

        $.ajax({
            url: `/api/v1/store/deliveryAddress/updateAddress`, // Убедитесь, что этот URL правильный
            method: 'PATCH', // Используйте PUT для обновления
            contentType: 'application/json',
            data: JSON.stringify(updatedAddress),
            success: function(response) {
                $('#update-address-form').hide(); // Скрыть форму после успешного обновления
                loadAddresses(currentPage); // Обновить список адресов
            },
            error: function(xhr, error) {
                console.error("Ошибка при обновлении адреса:", error);
                let errorMessage = xhr.responseText || "Произошла ошибка при обновлении адреса.";
                alert(errorMessage);
            }
        });
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
                let errorMessage = xhr.responseText || "Произошла ошибка при добавлении адреса.";
                alert(errorMessage); // Выводим сообщение об ошибке
            }
        });
    });

    $('#search-button').click(function() {
        searchParams.city = $('#search-city').val().trim();
        searchParams.street = $('#search-street').val().trim();
        searchParams.houseNumber = parseInt($('#search-houseNumber').val().trim(), 10) || null;
        currentPage = 0;
        loadAddresses(currentPage, searchParams);
    });

});
