let currentCategoryPage = 0;
let selectedCategories = [];

function loadCategories(page) {
    $.get(`/api/v1/store/productCategory?page=${page}`, function(data) {
        $('#category-list').empty();
        data.content.forEach(category => {
            const isChecked = selectedCategories.includes(category.id);
            $('#category-list').append(`
                <div>
                    <input type="checkbox" id="category_${category.id}" value="${category.id}" name="productCategories" ${isChecked ? 'checked' : ''} />
                    <label for="category_${category.id}">${category.category}</label>
                </div>
            `);
        });

        $('#prev-category').toggle(page > 0);
        $('#next-category').toggle(page < data.totalPages - 1);
    });
}

function updateSelectedCategories() {
    $('input[name="productCategories"]').each(function() {
        const categoryId = parseInt($(this).val());
        if ($(this).is(':checked')) {
            if (!selectedCategories.includes(categoryId)) {
                selectedCategories.push(categoryId);
            }
        } else {
            selectedCategories = selectedCategories.filter(id => id !== categoryId);
        }
    });

    // Обновляем скрытое поле
    $('#selectedCategories').val(selectedCategories.join(','));
}

$(document).ready(function() {
    loadCategories(currentCategoryPage);

    $(document).on('change', 'input[name="productCategories"]', function() {
        updateSelectedCategories();
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

    $('form').submit(function(event) {
        updateSelectedCategories();
        if (selectedCategories.length === 0) {
            event.preventDefault();
            alert("Товар должен иметь хотя бы 1 категорию!");
        }
    });
});
