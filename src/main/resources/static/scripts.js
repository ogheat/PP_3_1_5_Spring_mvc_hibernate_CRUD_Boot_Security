function showAddUserModal() {
    document.getElementById('usersTable').style.display = 'none';
    document.getElementById('addUserForm').style.display = 'block';
}

function hideAddUserModal() {
    document.getElementById('addUserForm').style.display = 'none';
    document.getElementById('usersTable').style.display = 'block';
}

async function getUser(id) {
    try {
        const response = await fetch(`http://localhost:8080/api/user/${id}`);
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const user = await response.json();
        const tableBody = document.querySelector('#usersTable tbody');
        tableBody.innerHTML = '';

        const row = document.createElement('tr');
        row.innerHTML = `
                    <td>${user.id}</td>
                    <td>${user.email}</td>
                    <td>${user.firstName}</td>
                    <td>${user.lastName}</td>
                    <td>${user.age}</td>
                    <td>${user.roles.map(role => role.roleName.replace('ROLE_', '')).join(', ')}</td>
                `;
        tableBody.appendChild(row);
    } catch (error) {
        console.error('Error fetching user:', error);
    }
}

async function fetchUsers() {
    try {
        const response = await fetch('http://localhost:8080/api/all');
        const users = await response.json();
        populateTable(users);
    } catch (error) {
        console.error('Error fetching users:', error);
    }
}

async function fetchUsers() {
    try {
        const response = await fetch('http://localhost:8080/api/all');
        const users = await response.json();
        populateTable(users);
    } catch (error) {
        console.error('Error fetching users:', error);
    }
}

function populateTable(users) {
    const tableBody = document.querySelector('#usersTable tbody');
    tableBody.innerHTML = '';

    users.forEach(user => {
        const row = document.createElement('tr');

        row.innerHTML = `
                <td>${user.id}</td>
                <td>${user.email}</td>
                <td>${user.firstName}</td>
                <td>${user.lastName}</td>
                <td>${user.age}</td>
                <td>${user.roles.map(role => role.roleName.replace('ROLE_', '')).join(', ')}</td>

                <td>
                    <button type="button" class="btn btn-info" data-toggle="modal" data-target="#editUserModal" onclick="openEditModal(${user.id})">Edit</button>
                </td>
                 <td>
                 <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#deleteUserModal" onclick="openDeleteModal(${user.id})">Delete</button>
                 </td>

            `;

        tableBody.appendChild(row);
    });
}

function openDeleteModal(userId) {
    fetch(`/api/user/${userId}`)
        .then(response => response.json())
        .then(user => {
            console.log(user);
            // Populate modal fields with user data
            document.getElementById('deleteId').value = user.id;
            document.getElementById('deleteFirstName').value = user.firstName;
            document.getElementById('deleteLastName').value = user.lastName;
            document.getElementById('deleteAge').value = user.age;
            document.getElementById('deleteEmail').value = user.email;
            document.getElementById('deleteRoles').value = user.roles.map(role => role.roleName).join(', ');
            document.getElementById('deleteUserBtn').onclick = () => deleteUser(user.id);
        })
        .catch(error => console.error('Error fetching user:', error));
}

function deleteUser(userId) {
    fetch(`/api/user/delete/${userId}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (response.ok) {
                $('#deleteUserModal').modal('hide');
                fetchUsers()
            } else {
                alert('Failed to delete user');
            }
        })
        .catch(error => console.error('Error deleting user:', error));
}

function openEditModal(userId) {
    fetch(`/api/user/${userId}`)
        .then(response => response.json())
        .then(user => {
            console.log(user);
            document.getElementById('editId').value = user.id;
            document.getElementById('editFirstName').value = user.firstName;
            document.getElementById('editLastName').value = user.lastName;
            document.getElementById('editAge').value = user.age;
            document.getElementById('editEmail').value = user.email;
            document.getElementById('editPassword').value = user.password;
            document.getElementById('editUserBtn').onclick = () => editUser(user);
        })
        .catch(error => console.error('Error fetching user:', error));
}

function editUser(user) {
    const roleSelect = document.getElementById('roles');
    const selectedRoles = Array.from(roleSelect.selectedOptions).map(option => {
        return {
            id: option.value,
            roleName: `ROLE_${option.text.trim().toUpperCase()}`
        };
    });
    const isAdminSelected = selectedRoles.some(role => role.roleName === 'ROLE_ADMIN');

    if (isAdminSelected && !selectedRoles.some(role => role.roleName === 'ROLE_USER')) {
        selectedRoles.push({
            id: '1',
            roleName: 'ROLE_USER'
        });
    }
    const updatedUser = {
        id: document.getElementById('editId').value,
        firstName: document.getElementById('editFirstName').value,
        lastName: document.getElementById('editLastName').value,
        age: document.getElementById('editAge').value,
        email: document.getElementById('editEmail').value,
        password: document.getElementById('editPassword').value,
        roles: selectedRoles

    };

    console.log("JSON VIEW")
    console.log(selectedRoles)
    console.log(JSON.stringify(updatedUser))
    fetch(`/api/user/edit/`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(updatedUser)
    })
        .then(response => {
            if (response.ok) {
                $('#editUserModal').modal('hide');
                fetchUsers()
            } else {
                alert('Failed to edit user');
            }
        })
        .catch(error => console.error('Error editing user:', error));
}

function addNewUser() {
    event.preventDefault();
    const roleSelect = document.getElementById('addRoles');
    const selectedRoles = Array.from(roleSelect.selectedOptions).map(option => {
        return {
            id: option.value,
            roleName: `ROLE_${option.text.trim().toUpperCase()}`
        };
    });
    const isAdminSelected = selectedRoles.some(role => role.roleName === 'ROLE_ADMIN');

    if (isAdminSelected && !selectedRoles.some(role => role.roleName === 'ROLE_USER')) {
        selectedRoles.push({
            id: '1',
            roleName: 'ROLE_USER'
        });
    }
    const user = {
        firstName: document.getElementById('firstName').value,
        lastName: document.getElementById('lastName').value,
        age: document.getElementById('age').value,
        email: document.getElementById('email').value,
        password: document.getElementById('password').value,
        roles: selectedRoles
    };

    console.log(user);

    fetch("/api/user/add/", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)
    })
        .then(response => {
            if (response.ok) {
                hideAddUserModal()
                fetchUsers()
                clearAddUserForm()
            } else {
                console.log("Error: ", response.statusText);
            }
        })
        .catch(error => console.error('Error adding user:', error));
}

function clearAddUserForm() {
    document.getElementById('firstName').value = ''
    document.getElementById('lastName').value = ''
    document.getElementById('age').value = ''
    document.getElementById('email').value = ''
    document.getElementById('password').value = ''
    document.getElementById('addRoles').value = ''
    let element = document.getElementById('#userTableToggle')
    let active_element = document.getElementById('addUserToggle')
    element.classList.toggle('active')
    active_element.classList.remove('active')

}