let currentPage = 0;
const size = 10;

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
                }).join(''); // Создаем список товаров

                const row =
                    `<tr>
                        <td>${order.id}</td>
                        <td>${order.orderDate}</td>
                        <td>${order.expectedReceiveDate}</td>
                        <td>${order.user?.login || 'Неизвестно'}</td>
                        <td>${order.orderStatus.status}</td>
                        <td>${'г. ' + order.deliveryAddress.city +
                                ', ул. ' + order.deliveryAddress.street +
                                ', д. №' + order.deliveryAddress.houseNumber}
                        </td>
                        <td>
                            <ul>
                                ${productsList} <!-- Вставляем список товаров -->
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
