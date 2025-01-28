let currentPage = 0;
let searchQuery = '';

function loadCategories(page, query = '') {
    $.ajax({
        url: `/api/v1/store/productCategory?page=${page}&search=${query}`,
        method: 'GET',
        success: function(data) {
            $('#category-list').empty();
            data.content.forEach(function(productCategory) {
                $('#category-list').append(`
                <div class="address-item">
                    <span class="address-text">${productCategory.category}</span>
                    <button class="update-button" data-id="${productCategory.id}">Редактировать</button>
                    <button class="delete-button" data-id="${productCategory.id}">Удалить</button>
                </div>
            `);
            });

            // Управление кнопками пагинации
            $('#prev-page').toggle(page > 0);
            $('#next-page').toggle(data.totalPages > page + 1);
        },
        error: function(xhr, status, error) {
            console.error("Ошибка при загрузке категорий:", error);
        }
    });
}



$(document).ready(function() {
    loadCategories(currentPage);

    $(document).on('click', '.delete-button', function() {
        const categoryId = $(this).data('id');
        if (confirm("Вы уверены, что хотите удалить эту категорию?")) {
            $.ajax({
                url: `/api/v1/store/productCategory/deleteCategory/${categoryId}`,
                method: 'DELETE',
                success: function() {
                    // После удаления, перезагрузим адреса
                    loadCategories(currentPage);
                },
                error: function(xhr, status, error) {
                    console.error("Ошибка при удалении категории:", error);
                }
            });
        }
    });

    $(document).on('click', '.update-button', function() {
        const productCategoryId = $(this).data('id');
        // Получите данные адреса, используя его ID
        $.ajax({
            url: `/api/v1/store/productCategory/${productCategoryId}`,
            method: 'GET',
            success: function(productCategory) {
                // Заполните форму редактирования
                $('#update-id').val(productCategory.id);
                $('#update-category').val(productCategory.category);
                $('#update-category-div').show();
            },
            error: function(xhr, status, error) {
                console.error("Ошибка при получении категории:", error);
                let errorMessage = xhr.responseText || "Произошла ошибка при получении категории.";
                alert(errorMessage);
            }
        });
    });

    $('#search-button').click(function() {
        searchQuery = $('#search-category').val().trim();
        loadCategories(0, searchQuery); // Сбросить страницу и выполнить поиск
    });

    $('#prev-page').click(function() {
        if (currentPage > 0) {
            currentPage--;
            loadCategories(currentPage);
        }
    });

    $('#next-page').click(function() {
        currentPage++;
        loadCategories(currentPage);
    });

    $('#add-productCategory-button').click(function() {
        $('#new-category-div').toggle(); // Показать или скрыть форму
    });

    $('#update-category-form').submit(function(event) {
        event.preventDefault();

        const updatedProductCategory = {
            id: $('#update-id').val(),
            category: $('#update-category').val(),
        };

        $.ajax({
            url: `/api/v1/store/productCategory/updateProductCategory`,
            method: 'PATCH',
            contentType: 'application/json',
            data: JSON.stringify(updatedProductCategory),
            success: function(response) {
                $('#update-category-div').hide();
                loadCategories(currentPage);
            },
            error: function(xhr, error) {
                console.error("Ошибка при обновлении категории:", error);
                let errorMessage = xhr.responseText || "Произошла ошибка при обновлении категории.";
                alert(errorMessage);
            }
        });
    });

    $('#new-category-form').submit(function(event) {
        event.preventDefault();

        const newProductCategory = {
            category: $('#new-category').val(),
        };

        $.ajax({
            url: '/api/v1/store/productCategory/newCategory',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(newProductCategory),
            success: function(response) {
                $('#new-category-div').hide();
                loadCategories(currentPage);
                $('#new-category-form')[0].reset();
            },
            error: function(xhr, error) {
                console.error("Ошибка при добавлении категории:", error);
                let errorMessage = xhr.responseText || "Произошла ошибка при добавлении категории.";
                alert(errorMessage);
            }
        });
    });
});