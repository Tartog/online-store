let currentPage = 0;
let searchID;

function loadOrders(page, searchID) {
    let url = `/api/v1/store/orders?&page=${page}`;

    if (searchID) {
        url += `&id=${searchID}`;
    }

    $.ajax({
        url: url,
        method: 'GET',
        success: function(data) {
            console.log(data);
            $('#order-table-body').empty();
            data.content.forEach(order => {
                console.log(order);
                const productsList = order.productsInOrders.map(product => {
                    console.log(product);
                    return `<li>${product.product?.name || 'Неизвестно'} x ${product.numberOfProduct} - ${product.product?.price || 0}</li>`;
                }).join('');

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
                                ${productsList}
                            </ul>
                        </td>
                    </tr>`;
                $('#order-table-body').append(row);
            });

            $('#prev').toggle(page > 0);
            $('#next').toggle(data.totalPages > page + 1);
        },
        error: function(xhr, status, error) {
            console.error('Ошибка загрузки заказов:', error);
        }
    });
}


$(document).ready(function() {
    loadOrders(currentPage);

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

        $.ajax({
            url: `/api/v1/store/orders/updateOrderStatus/${orderId}`,
            method: 'PATCH',
            contentType: 'application/json',
            data: JSON.stringify(selectedStatus),
            success: function(response) {
                console.log('Статус обновлён:', response);
                loadOrders(currentPage, searchID);
            },
            error: function(xhr, status, error) {
                console.error('Ошибка обновления статуса:', error);
            }
        });
    });

    $('#search-button').click(function() {
        searchID = parseInt($('#search-id').val().trim(), 10) || null;
        currentPage = 0;
        loadOrders(currentPage, searchID);
    });
});
