let currentPage = 0;

function loadOrders(page, addressId = 'all') {
    $.ajax({
        url: `/api/v1/store/orders?addressId=${addressId}&page=${page}`, // Запрос к контроллеру
        method: 'GET',
        success: function(data) {
            console.log(data); // Для отладки
            //const orders = data.content; // Получаем список заказов
            //const totalPages = data.totalPages; // Общее количество страниц
            //data.
            $('#order-table-body').empty(); // Очищаем таблицу
            data.content.forEach(order => {
                console.log(order); // Проверка структуры объекта order
                const productsList = order.productsInOrders.map(product => {
                    console.log(product); // Выводим объект товара в консоль для отладки
                    return `<li>${product.product?.name || 'Неизвестно'} x ${product.numberOfProduct} - ${product.product?.price || 0}</li>`;
                }).join(''); // Создаем список товаров

                const statusOptions = ['Ожидает', 'Доставляется', 'Возвращен', 'Получен'];
                const statusDropdown =
                    `<select id="status-${order.id}">
                    ${statusOptions.map(status => `<option value="${status}" 
                        ${status === order.orderStatus?.status ? 'selected' : ''}>${status}</option>`).join('')}
                    </select>
                    <button class="update-status" data-order-id="${order.id}">Обновить статус</button>`;

                const row =
                    `<tr>
                        <td>${order.id}</td>
                        <td>${order.orderDate}</td>
                        <td>${order.expectedReceiveDate}</td>
                        <td>${order.user?.login || 'Неизвестно'}</td>
                        <td>
                            ${order.orderStatus?.status || 'Неизвестно'}
                            ${statusDropdown}
                        </td>
                        <td>
                            <ul>
                                ${productsList} <!-- Вставляем список товаров -->
                            </ul>
                        </td>
                    </tr>`;
                $('#order-table-body').append(row);
            });

            // Управление видимостью кнопок пагинации
            $('#prev').toggle(page > 0);
            $('#next').toggle(data.totalPages > page + 1);
            // $('#next').toggle(currentPage < totalPages - 1 && totalPages > 1);
            // $('#prev').data('page', currentPage - 1);
            // $('#next').data('page', currentPage + 1);
        },
        error: function(xhr, status, error) {
            console.error('Ошибка загрузки заказов:', error);
        }
    });
}


$(document).ready(function() {
    // let currentPage = 0;
    // const pageSize = 10; // Размер страницы
    // const addressId = 'all'; // Здесь можно указать конкретный ID адреса, если нужно


    // Загрузка первых заказов при загрузке страницы
    loadOrders(currentPage);

    // Обработчики событий для кнопок пагинации
    $('#prev').click(function() {
        if (currentPage > 0) {
            currentPage--;
            loadOrders(currentPage);
        }
    });

    $('#next').click(function() {
        currentPage++;
        loadOrders(currentPage);
    });

    $(document).on('click', '.update-status', function() {
        const orderId = $(this).data('order-id');
        const selectedStatus = $(`#status-${orderId}`).val();

        // Отправка запроса на обновление статуса
        $.ajax({
            url: `/api/v1/store/orders/updateOrderStatus/${orderId}`, // URL для обновления статуса
            method: 'PATCH',
            contentType: 'application/json',
            data: JSON.stringify(selectedStatus), // Передаем новый статус
            success: function(response) {
                console.log('Статус обновлён:', response);
                loadOrders(currentPage); // Перезагружаем заказы, чтобы обновить статус в таблице
            },
            error: function(xhr, status, error) {
                console.error('Ошибка обновления статуса:', error);
            }
        });
    });
});
