$(document).ready(function() {
    let currentPage = 0;
    const pageSize = 10; // Размер страницы
    const addressId = 'all'; // Здесь можно указать конкретный ID адреса, если нужно

    function loadOrders(page) {
        $.ajax({
            url: `/api/v1/store/orders?addressId=${addressId}&page=${page}&size=${pageSize}`, // Запрос к контроллеру
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

                    const row =
                    `<tr>
                        <td>${order.id}</td>
                        <td>${order.orderDate}</td>
                        <td>${order.expectedReceiveDate}</td>
                        <td>${order.user?.login || 'Неизвестно'}</td>
                        <td>${order.orderStatus?.status || 'Неизвестно'}</td>
                        <td>
                            <ul>
                                ${productsList} <!-- Вставляем список товаров -->
                            </ul>
                        </td>
                    </tr>`;
                    $('#order-table-body').append(row);
                });

                // Управление видимостью кнопок пагинации
                $('#prev').toggle(currentPage > 0);
                $('#next').toggle(currentPage < totalPages - 1);
                $('#prev').data('page', currentPage - 1);
                $('#next').data('page', currentPage + 1);
            },
            error: function(xhr, status, error) {
                console.error('Ошибка загрузки заказов:', error);
            }
        });
    }

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
});
