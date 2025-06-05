console.log('admin-usuarios.js cargado ***********************');

function initializeAdminUsuarios() {
    console.log('Inicializando funciones de admin-usuarios.js...');
    cargarUsuarios();
    setupFormulario();
    console.log('Usuarios y formulario inicializados.');
}


function setupFormulario() {
    const form = document.getElementById('userForm');
    if (form) {
        form.addEventListener('submit', handleFormSubmit);
    }
}

function cargarUsuarios() {
    const jwt = localStorage.getItem('jwt');
    console.log(jwt);
    console.log("Estamos en carga usuarios en admin-usuarios.js");

    // ✅ Verificar que el elemento existe antes de continuar
    const tablaUsuariosBody  = document.getElementById('tablaUsuarios');
    if (!tablaUsuariosBody ) {
        console.error('❌ Elemento tablaUsuarios no encontrado, reintentando...');
        return;
    }

    fetch('/api/usuarios', {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + jwt,
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            console.log("Esa es la respuesta *******************");
            console.log(response);
            if (!response.ok) {
                throw new Error(`HTTP ${response.status}`);
            }
            return response.json();
        })
        .then(usuarios => {
            mostrarUsuarios(usuarios);
            showSuccessMessage(`${usuarios.length} usuarios cargados correctamente`);
        })
        .catch(error => {
            showErrorMessage('Error al cargar usuarios: ' + error.message);
            if (tablaUsuariosBody ) {
                tablaUsuariosBody .innerHTML = '<tr><td colspan="6" class="text-center text-danger">Error al cargar usuarios</td></tr>';
            }
        });
    console.log('Usuarios cargados correctamente o no *********************');
}

function mostrarUsuarios(usuarios) {
    const tbody = document.getElementById('tablaUsuarios');
    if (!tbody) {
        console.error('❌ Elemento tablaUsuarios no encontrado en mostrarUsuarios');
        return;
    }
    if (usuarios.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center">No hay usuarios registrados</td></tr>';
        return;
    }

    tbody.innerHTML = usuarios.map(usuario => `
        <tr>
            <td>${usuario.id}</td>
            <td>${usuario.nombre} ${usuario.apellidos || ''}</td>
            <td>${usuario.email}</td>
            <td><span class="badge bg-${getTipoBadgeColor(usuario.tipo)}">${usuario.tipo}</span></td>
            <td>${usuario.telefono || '-'}</td>
            <td>
                <button class="btn btn-sm btn-info" onclick="verUsuario(${usuario.id})">
                    <i class="fas fa-eye"></i>
                </button>
                <button class="btn btn-sm btn-warning" onclick="editarUsuario(${usuario.id})">
                    <i class="fas fa-edit"></i>
                </button>
                <button class="btn btn-sm btn-danger" onclick="eliminarUsuario(${usuario.id})">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
        </tr>
    `).join('');
}

function getTipoBadgeColor(tipo) {
    switch(tipo) {
        case 'Administrador': return 'danger';
        case 'Personal': return 'warning';
        case 'Cliente': return 'success';
        default: return 'secondary';
    }
}

function verUsuario(id) {
    const jwt = localStorage.getItem('jwt');

    fetch(`/api/usuarios/${id}`, {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + jwt,
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP ${response.status}`);
            }
            return response.json();
        })
        .then(usuario => {
            alert(`Usuario: ${usuario.nombre} ${usuario.apellidos}\nEmail: ${usuario.email}\nTipo: ${usuario.tipo}`);
        })
        .catch(error => {
            showErrorMessage('Error al obtener usuario: ' + error.message);
        });
}

function editarUsuario(id) {
    const jwt = localStorage.getItem('jwt');

    // Primero obtener los datos del usuario
    fetch(`/api/usuarios/${id}`, {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + jwt,
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(usuario => {
            // Rellenar el formulario
            document.getElementById('userId').value = usuario.id;
            document.getElementById('nombre').value = usuario.nombre;
            document.getElementById('apellidos').value = usuario.apellidos || '';
            document.getElementById('email').value = usuario.email;
            document.getElementById('tipo').value = usuario.tipo;
            document.getElementById('telefono').value = usuario.telefono || '';
            document.getElementById('direccion').value = usuario.direccion || '';
            document.getElementById('contrasena').value = ''; // No mostrar contraseña

            document.getElementById('tituloFormulario').textContent = 'Editar Usuario';
            document.getElementById('formularioUsuario').style.display = 'block';
        })
        .catch(error => {
            showErrorMessage('Error al cargar usuario para editar: ' + error.message);
        });
}

function eliminarUsuario(id) {
    if (!confirm('¿Estás seguro de que quieres eliminar este usuario?')) {
        return;
    }

    const jwt = localStorage.getItem('jwt');

    fetch(`/api/usuarios/${id}`, {
        method: 'DELETE',
        headers: {
            'Authorization': 'Bearer ' + jwt
        }
    })
        .then(response => {
            if (response.status === 204) {
                showSuccessMessage('Usuario eliminado correctamente');
                cargarUsuarios(); // Recargar la lista
            } else if (response.status === 404) {
                showErrorMessage('Usuario no encontrado');
            } else {
                throw new Error(`HTTP ${response.status}`);
            }
        })
        .catch(error => {
            showErrorMessage('Error al eliminar usuario: ' + error.message);
        });
}

function mostrarFormularioCrear() {
    document.getElementById('userForm').reset();
    document.getElementById('userId').value = '';
    document.getElementById('tituloFormulario').textContent = 'Crear Usuario';
    document.getElementById('formularioUsuario').style.display = 'block';
}

function ocultarFormulario() {
    document.getElementById('formularioUsuario').style.display = 'none';
}

function handleFormSubmit(e) {
    e.preventDefault();

    const userId = document.getElementById('userId').value;
    const isEditing = userId !== '';

    const userData = {
        tipo: document.getElementById('tipo').value,
        nombre: document.getElementById('nombre').value,
        apellidos: document.getElementById('apellidos').value,
        email: document.getElementById('email').value,
        telefono: document.getElementById('telefono').value || null,
        direccion: document.getElementById('direccion').value || null
    };

    const contrasena = document.getElementById('contrasena').value;
    if (!isEditing || contrasena) {
        userData.contrasena = contrasena;
    }

    const jwt = localStorage.getItem('jwt');
    const url = isEditing ? `/api/usuarios/${userId}` : '/api/usuarios';
    const method = isEditing ? 'PUT' : 'POST';

    fetch(url, {
        method: method,
        headers: {
            'Authorization': 'Bearer ' + jwt,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(userData)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP ${response.status}`);
            }
            return response.json();
        })
        .then(usuario => {
            const mensaje = isEditing ? 'Usuario actualizado correctamente' : 'Usuario creado correctamente';
            showSuccessMessage(mensaje);
            ocultarFormulario();
            cargarUsuarios(); // Recargar la lista
        })
        .catch(error => {
            showErrorMessage('Error al guardar usuario: ' + error.message);
        });
}

function showErrorMessage(message) {
    const errorDiv = document.getElementById('mensajeError');
    if (errorDiv) {
        const textSpan = errorDiv.querySelector('.mensaje-texto');
        if (textSpan) {
            textSpan.textContent = message;
        }
        errorDiv.style.display = 'block';
        setTimeout(() => {
            errorDiv.style.display = 'none';
        }, 5000);
    }
}

function showSuccessMessage(message) {
    const successDiv = document.getElementById('mensajeExito');
    if (successDiv) {
        const textSpan = successDiv.querySelector('.mensaje-texto');
        if (textSpan) {
            textSpan.textContent = message;
        }
        successDiv.style.display = 'block';
        setTimeout(() => {
            successDiv.style.display = 'none';
        }, 3000);
    }
}