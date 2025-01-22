let currentCategoryPage = 0;
let selectedCategories = []; // Массив для хранения выбранных категорий

function loadCategories(page) {
    $.get(`/api/v1/store/productCategory?page=${page}`, function(data) {
        $('#category-list').empty(); // Очистить текущий список категорий
        data.content.forEach(category => {
            const isChecked = selectedCategories.includes(category.id); // Проверяем, выбран ли чекбокс
            $('#category-list').append(`
                <div>
                    <input type="checkbox" id="category_${category.id}" value="${category.id}" name="productCategories" ${isChecked ? 'checked' : ''} />
                    <label for="category_${category.id}">${category.category}</label>
                </div>
            `);
        });

        // Управление кнопками пагинации категорий
        $('#prev-category').toggle(page > 0);
        $('#next-category').toggle(page < data.totalPages - 1);
    });
}

function updateSelectedCategories() {
    // Обновляем массив выбранных категорий
    $('input[name="productCategories"]').each(function() {
        const categoryId = parseInt($(this).val());
        if ($(this).is(':checked')) {
            if (!selectedCategories.includes(categoryId)) {
                selectedCategories.push(categoryId); // Добавляем новую выбранную категорию
            }
        } else {
            selectedCategories = selectedCategories.filter(id => id !== categoryId); // Удаляем невыбранные категории
        }
    });

    // Обновляем скрытое поле
    $('#selectedCategories').val(selectedCategories.join(',')); // Преобразуем массив в строку
}

$(document).ready(function() {
    loadCategories(currentCategoryPage);

    // Обработчик изменения состояния чекбоксов
    $(document).on('change', 'input[name="productCategories"]', function() {
        updateSelectedCategories(); // Обновляем массив при изменении
    });

    $('#prev-category').click(function() {
        if (currentCategoryPage > 0) {
            currentCategoryPage--;
            loadCategories(currentCategoryPage);
        }
    });

    $('#next-category').click(function() {
        currentCategoryPage++;
        loadCategories(currentCategoryPage);
    });

    // Обработчик отправки формы
    $('form').submit(function(event) {
        updateSelectedCategories(); // Обновляем массив перед отправкой формы
        if (selectedCategories.length === 0) {
            event.preventDefault(); // Отменяем отправку формы, если нет выбранных категорий
            alert("Товар должен иметь хотя бы 1 категорию!"); // Предупреждение пользователю
        }
    });
});
