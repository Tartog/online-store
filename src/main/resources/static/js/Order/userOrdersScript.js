let currentPage = 0;

function loadOrders(page, user) {
    let url = `/api/v1/store/userOrders?login=${user}&page=${page}`;

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

                const row =
                    `<tr class="address-item">
                        <td class="address-text">${order.id}</td>
                        <td class="address-text">${order.orderDate}</td>
                        <td class="address-text">${order.expectedReceiveDate}</td>
                        <td class="address-text">${order.orderStatus.status}</td>
                        <td class="address-text">${'г. ' + order.deliveryAddress.city +
                                ', ул. ' + order.deliveryAddress.street +
                                ', д. №' + order.deliveryAddress.houseNumber}
                        </td>
                        <td class="address-text">
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
    loadOrders(currentPage, user);

    $('#prev').click(function() {
        if (currentPage > 0) {
            currentPage--;
            loadOrders(currentPage, user);
        }
    });

    $('#next').click(function() {
        currentPage++;
        loadOrders(currentPage, user);
    });
});