document.addEventListener('DOMContentLoaded', () => {
    const userTableBody = document.querySelector('#all-users tbody');
    const addUserForm = document.querySelector('#add-user-form');
    const rolesSelect = document.querySelector('#new_roles');
    const editUserModal = document.querySelector('#editUserModal');
    const editUserForm = document.querySelector('#edit-user-form');
    const editRolesSelect = document.querySelector('#edit_roles');
    const deleteUserModal = document.querySelector('#deleteUserModal');
    const deleteUserForm = document.querySelector('#delete-user-form');

    const fetchUsers = async () => {
        try {
            const response = await fetch('/api/admin/users');
            if (!response.ok) {
                throw new Error(`Error: ${response.status}`);
            }
            const users = await response.json();
            console.log('Loaded users:', users);

            userTableBody.innerHTML = '';

            users.forEach(user => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${user.id}</td>
                    <td>${user.name}</td>
                    <td>${user.year}</td>
                    <td>${user.gender}</td>
                    <td>${user.roles.map(role => role.name).join(', ')}</td>
                    <td>
                        <button class="btn btn-info edit-user" data-id="${user.id}" data-bs-toggle="modal" data-bs-target="#editUserModal">Edit</button>
                    </td>
                    <td>
                        <button class="btn btn-danger delete-user" data-id="${user.id}">Delete</button>
                    </td>
                `;
                userTableBody.appendChild(row);
            });
        } catch (error) {
            console.error('Error:', error);
        }
    };

    const fetchRoles = async () => {
        try {
            const response = await fetch('/api/admin/roles');
            if (!response.ok) {
                throw new Error(`Error: ${response.status}`);
            }
            const roles = await response.json();
            console.log('Roles:', roles);

            rolesSelect.innerHTML = '';
            roles.forEach(role => {
                const option = document.createElement('option');
                option.value = role.id;
                option.textContent = role.name;
                rolesSelect.appendChild(option);
            });

            editRolesSelect.innerHTML = '';
            roles.forEach(role => {
                const option = document.createElement('option');
                option.value = role.id;
                option.textContent = role.name;
                editRolesSelect.appendChild(option);
            });
        } catch (error) {
            console.error('Error:', error);
        }
    };

    addUserForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        try {
            const formData = new FormData(addUserForm);
            const user = Object.fromEntries(formData.entries());
            user.roles = Array.from(formData.getAll('roles')).map(id => ({ id: parseInt(id) }));
            user.online = formData.get('online') === 'on';

            console.log('Adding user:', user);

            const response = await fetch('/api/admin/users', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(user),
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`Error: ${errorText}`);
            }

            console.log('User has been added');
            addUserForm.reset();
            fetchUsers();

            const allUsersTab = document.querySelector('#all-users-tab');
            const allUsersTabInstance = new bootstrap.Tab(allUsersTab);
            allUsersTabInstance.show();
        } catch (error) {
            console.error('Error:', error);
        }
    });
    userTableBody.addEventListener('click', async (event) => {
        if (event.target.classList.contains('edit-user')) {
            const userId = event.target.getAttribute('data-id');

            try {
                const response = await fetch(`/api/admin/users/${userId}`);
                if (!response.ok) {
                    throw new Error(`Error: ${response.status}`);
                }
                const user = await response.json();
                console.log('Error:', user);

                editUserForm.querySelector('#edit_user_id').value = user.id;
                editUserForm.querySelector('#edit_username').value = user.username;
                editUserForm.querySelector('#edit_name').value = user.name;
                editUserForm.querySelector('#edit_year').value = user.year;
                editUserForm.querySelector('#edit_gender').value = user.gender;
                editUserForm.querySelector('#edit_online').checked = user.online;

                Array.from(editRolesSelect.options).forEach(option => {
                    option.selected = user.roles.some(role => role.id === parseInt(option.value));
                });
            } catch (error) {
                console.error('Error:', error);
            }
        }
    });

    editUserForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        try {
            const formData = new FormData(editUserForm);
            const updatedUser = Object.fromEntries(formData.entries());
            updatedUser.roles = Array.from(formData.getAll('roles')).map(id => ({ id: parseInt(id) }));
            updatedUser.online = formData.get('online') === 'on';

            console.log('User update:', updatedUser);

            const response = await fetch(`/api/admin/users/${updatedUser.id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(updatedUser),
            });

            if (!response.ok) {
                throw new Error('Error');
            }

            console.log('User updated');
            fetchUsers();
            const modalInstance = bootstrap.Modal.getInstance(editUserModal);
            modalInstance.hide();
        } catch (error) {
            console.error('Error:', error);
        }
    });

    userTableBody.addEventListener('click', async (event) => {
        if (event.target.classList.contains('delete-user')) {
            const userId = event.target.getAttribute('data-id');

            try {
                const response = await fetch(`/api/admin/users/${userId}`);
                if (!response.ok) {
                    throw new Error(`Error: ${response.status}`);
                }
                const user = await response.json();

                deleteUserForm.querySelector('#delete_user_id').value = user.id;
                deleteUserForm.querySelector('#delete_username').value = user.username;
                deleteUserForm.querySelector('#delete_name').value = user.name;
                deleteUserForm.querySelector('#delete_year').value = user.year;
                deleteUserForm.querySelector('#delete_gender').value = user.gender;
                deleteUserForm.querySelector('#delete_online').value = user.online ? 'Online' : 'Offline';
                deleteUserForm.querySelector('#delete_roles').value = user.roles.map(role => role.name).join(', ');

                const modalInstance = new bootstrap.Modal(deleteUserModal);
                modalInstance.show();
            } catch (error) {
                console.error('Error:', error);
            }
        }
    });

    deleteUserForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        const userId = deleteUserForm.querySelector('#delete_user_id').value;

        try {
            const response = await fetch(`/api/admin/users/${userId}`, {
                method: 'DELETE',
            });
            if (!response.ok) {
                throw new Error('Error');
            }

            console.log('User deleted');
            fetchUsers();
            const modalInstance = bootstrap.Modal.getInstance(deleteUserModal);
            modalInstance.hide();
        } catch (error) {
            console.error('Error:', error);
        }
    });

    fetchUsers();
    fetchRoles();
});
