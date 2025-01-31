let currentPage = 1;
const itemsPerPage = 16;

function displayItems() {
    const items = document.querySelectorAll('.product-item');
    const totalPages = Math.ceil(items.length / itemsPerPage);

    items.forEach((item, index) => {
        item.style.display = 'none';
    });

    const start = (currentPage - 1) * itemsPerPage;
    const end = start + itemsPerPage;

    for (let i = start; i < end && i < items.length; i++) {
        items[i].style.display = 'block';
    }

    document.getElementById('pageInfo').textContent = `Страница ${currentPage} из ${totalPages}`;

    document.getElementById('prevPage').disabled = currentPage === 1;
    document.getElementById('nextPage').disabled = currentPage === totalPages;
}

function changePage(direction) {
    currentPage += direction;
    displayItems();
}

document.addEventListener('DOMContentLoaded', () => {
    displayItems();
    const addToCartButtons = document.querySelectorAll('.add-to-cart');
    addToCartButtons.forEach(button => {
        button.addEventListener('click', function() {
            const login = this.getAttribute('data-login');
            const productId = this.getAttribute('data-productId');
            addToCart(login, productId);
        });
    });

    function addToCart(login, productId) {
        const xhr = new XMLHttpRequest();
        xhr.open('PATCH', `/api/v1/store/addProductToCart/${login}/${productId}`, true);
        xhr.setRequestHeader('Content-Type', 'application/json');

        xhr.onload = function () {
            if (xhr.status === 200) {
                console.log('Товар добавлен в корзину');
            } else {
                console.error('Ошибка при добавлении товара в корзину');
                alert('Ошибка при добавлении товара в корзину');
            }
        };
        xhr.send();
    }
});
