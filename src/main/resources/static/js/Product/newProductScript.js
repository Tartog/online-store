let currentCategoryPage = 0;
const categorySize = 10; // Количество категорий на странице

function loadCategories(page) {
    $.get(`/api/v1/store/products/${seller.getLogin()}/categories?page=${page}&size=${categorySize}`, function(data) {
        $('#category-list').empty(); // Очистить текущий список категорий
        data.content.forEach(category => {
            $('#category-list').append(`
                <div>
                    <input type="checkbox" id="category_${category.id}" value="${category.id}" name="productCategories" />
                    <label for="category_${category.id}">${category.category}</label>
                </div>
            `);
        });

        // Управление кнопками пагинации категорий
        $('#prev-category').toggle(page > 0);
        $('#next-category').toggle(page < data.totalPages - 1);
    });
}

$(document).ready(function() {
    loadCategories(currentCategoryPage);

    $('#prev-category').click(function() {
        currentCategoryPage--;
        loadCategories(currentCategoryPage);
    });

    $('#next-category').click(function() {
        currentCategoryPage++;
        loadCategories(currentCategoryPage);
    });
});
