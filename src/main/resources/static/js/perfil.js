// perfil.js

document.addEventListener('DOMContentLoaded', function() {
    initializePerfilForm();
    loadUserProfile();
});

function initializePerfilForm() {
    const perfilForm = document.getElementById('perfilForm');
    const cancelarBtn = document.getElementById('cancelarBtn');

    // Configurar evento de envío del formulario
    perfilForm.addEventListener('submit', handleFormSubmit);

    // Configurar botón cancelar
    cancelarBtn.addEventListener('click', handleCancel);

    // Configurar validación en tiempo real
    setupFieldValidation();
}

function loadUserProfile() {
    const jwt = localStorage.getItem('jwt');
    const userName = localStorage.getItem('userName');

    if (!jwt) {
        showErrorMessage('No estás autenticado. Redirigiendo al login...');
        setTimeout(() => {
            window.location.href = '/auth/login';
        }, 2000);
        return;
    }

    // Mostrar información del usuario actual
    updateCurrentUserInfo(userName);

    // Cargar datos del perfil desde el servidor
    fetchUserProfile(jwt);
}

function updateCurrentUserInfo(userName) {
    const userInfoSpan = document.getElementById('current-user-info');
    if (userInfoSpan) {
        userInfoSpan.textContent = userName || 'Usuario';
    }
}

function fetchUserProfile(jwt) {
    fetch('/api/usuarios/perfil', {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + jwt,
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al cargar el perfil');
            }
            return response.json();
        })
        .then(data => {
            populateForm(data);
        })
        .catch(error => {
            showErrorMessage('Error al cargar los datos del perfil: ' + error.message);
        });
}

function populateForm(userData) {
    // Rellenar campos del formulario con los datos del usuario
    document.getElementById('nombre').value = userData.nombre || '';
    document.getElementById('apellidos').value = userData.apellidos || '';
    document.getElementById('email').value = userData.email || '';
    document.getElementById('direccion').value = userData.direccion || '';
    document.getElementById('poblacion').value = userData.poblacion || '';
    document.getElementById('provincia').value = userData.provincia || '';
    document.getElementById('codigoPostal').value = userData.codigoPostal || '';
    document.getElementById('telefono').value = userData.telefono || '';
    document.getElementById('numeroTarjetaCredito').value = userData.numeroTarjetaCredito || '';
}

function handleFormSubmit(e) {
    e.preventDefault();

    // Recoger datos del formulario
    const formData = collectFormData();

    // Validar datos
    if (!validateFormData(formData)) {
        return;
    }

    // Limpiar mensajes anteriores
    clearMessages();

    // Mostrar loading en el botón
    showLoadingState(true);

    // Enviar petición de actualización
    updateUserProfile(formData);
}

function collectFormData() {
    return {
        nombre: document.getElementById('nombre').value.trim(),
        apellidos: document.getElementById('apellidos').value.trim(),
        direccion: document.getElementById('direccion').value.trim() || null,
        poblacion: document.getElementById('poblacion').value.trim() || null,
        provincia: document.getElementById('provincia').value.trim() || null,
        codigoPostal: document.getElementById('codigoPostal').value.trim() || null,
        telefono: document.getElementById('telefono').value.trim() || null,
        numeroTarjetaCredito: document.getElementById('numeroTarjetaCredito').value.trim() || null,
        contrasenaActual: document.getElementById('contrasenaActual').value,
        contrasenaNueva: document.getElementById('contrasenaNueva').value
    };
}

function validateFormData(formData) {
    // Validar campos requeridos
    if (!formData.nombre || !formData.apellidos) {
        showErrorMessage('Por favor, completa el nombre y apellidos.');
        return false;
    }

    // Validar cambio de contraseña
    if (formData.contrasenaNueva && !formData.contrasenaActual) {
        showErrorMessage('Para cambiar la contraseña, debes introducir la contraseña actual.');
        return false;
    }

    if (formData.contrasenaNueva && formData.contrasenaNueva.length < 6) {
        showErrorMessage('La nueva contraseña debe tener al menos 6 caracteres.');
        return false;
    }

    return true;
}

function updateUserProfile(formData) {
    const jwt = localStorage.getItem('jwt');

    fetch('/api/usuarios/perfil', {
        method: 'PUT',
        headers: {
            'Authorization': 'Bearer ' + jwt,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => Promise.reject(err));
            }
            return response.json();
        })
        .then(data => {
            handleUpdateSuccess(data);
        })
        .catch(error => {
            handleUpdateError(error);
        })
        .finally(() => {
            showLoadingState(false);
        });
}

function handleUpdateSuccess(data) {
    showSuccessMessage('Perfil actualizado correctamente.');

    // Actualizar nombre en localStorage si cambió
    if (data.nombre) {
        localStorage.setItem('userName', data.nombre);
        updateCurrentUserInfo(data.nombre);
    }

    // Limpiar campos de contraseña
    document.getElementById('contrasenaActual').value = '';
    document.getElementById('contrasenaNueva').value = '';
}

function handleUpdateError(error) {
    const mensaje = error.message || 'Error al actualizar el perfil. Inténtalo de nuevo.';
    showErrorMessage(mensaje);
}

function handleCancel() {
    if (confirm('¿Estás seguro de que quieres cancelar? Se perderán los cambios no guardados.')) {
        // Recargar los datos originales
        loadUserProfile();
        clearMessages();
    }
}

function setupFieldValidation() {
    // Limpiar mensajes cuando el usuario empiece a escribir
    const inputs = document.querySelectorAll('input');
    inputs.forEach(input => {
        input.addEventListener('input', clearMessages);
    });
}

// Funciones auxiliares
function showErrorMessage(message) {
    const errorDiv = document.getElementById('mensajeError');
    const textSpan = errorDiv.querySelector('.mensaje-texto');
    textSpan.textContent = message;
    errorDiv.style.display = 'block';

    // Scroll al mensaje
    errorDiv.scrollIntoView({ behavior: 'smooth', block: 'center' });
}

function showSuccessMessage(message) {
    const successDiv = document.getElementById('mensajeExito');
    const textSpan = successDiv.querySelector('.mensaje-texto');
    textSpan.textContent = message;
    successDiv.style.display = 'block';

    // Scroll al mensaje
    successDiv.scrollIntoView({ behavior: 'smooth', block: 'center' });
}

function clearMessages() {
    document.getElementById('mensajeError').style.display = 'none';
    document.getElementById('mensajeExito').style.display = 'none';
}

function showLoadingState(loading) {
    const submitBtn = document.querySelector('button[type="submit"]');
    if (loading) {
        submitBtn.disabled = true;
        submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Guardando...';
    } else {
        submitBtn.disabled = false;
        submitBtn.innerHTML = '<i class="fas fa-save me-2"></i>Guardar Cambios';
    }
}
