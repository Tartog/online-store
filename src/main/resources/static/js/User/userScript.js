let currentPage = 0;

function loadUsers(page) {
    let url = `/api/v1/store/users?page=${page}`;
    $.ajax({
        url: url,
        method: 'GET',
        success: function(data) {
            $('#user-list').empty();
            data.content.forEach(function(user) {
                $('#user-list').append(`
                <div class="user-item">
                    <span class="user-text">${user.role + ' ' + user.login}</span>
                    <button class="delete-button" data-id="${user.login}">Удалить</button>
                </div>
            `);
            });

            $('#prev-page').toggle(page > 0);
            $('#next-page').toggle(data.totalPages > page + 1);
        },
        error: function(xhr, status, error) {
            console.error("Ошибка при загрузке пользователей:", error);
        }
    });
}

$(document).ready(function() {
    loadUsers(currentPage);

    $(document).on('click', '.delete-button', function() {
        const login = $(this).data('id');
        if (confirm("Вы уверены, что хотите удалить этого пользователя?")) {
            $.ajax({
                url: `/api/v1/store/deleteUser/${login}`,
                method: 'DELETE',
                success: function() {
                    loadUsers(currentPage);
                },
                error: function(xhr, status, error) {
                    console.error("Ошибка при удалении пользователя:", error);
                }
            });
        }
    });

    $('#prev-page').click(function() {
        if (currentPage > 0) {
            currentPage--;
            loadUsers(currentPage);
        }
    });

    $('#next-page').click(function() {
        currentPage++;
        loadUsers(currentPage);
    });
});