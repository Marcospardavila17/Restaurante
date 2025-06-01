// register.js

document.addEventListener('DOMContentLoaded', function() {
    initializeRegisterForm();
});

function initializeRegisterForm() {
    const registerForm = document.getElementById('registerForm');
    const emailInput = document.getElementById('email');

    registerForm.addEventListener('submit', handleFormSubmit);

    emailInput.addEventListener('blur', validateEmail);

    setupFieldValidation();
}

function handleFormSubmit(e) {
    e.preventDefault();

    const formData = collectFormData();

    if (!validateFormData(formData)) {
        return;
    }

    clearMessages();

    showLoadingState(true);

    registerUser(formData);
}

function collectFormData() {
    return {
        nombre: document.getElementById('nombre').value.trim(),
        apellidos: document.getElementById('apellidos').value.trim(),
        email: document.getElementById('email').value.trim(),
        contrasena: document.getElementById('contrasena').value,
        direccion: document.getElementById('direccion').value.trim() || null,
        poblacion: document.getElementById('poblacion').value.trim() || null,
        provincia: document.getElementById('provincia').value.trim() || null,
        codigoPostal: document.getElementById('codigoPostal').value.trim() || null,
        telefono: document.getElementById('telefono').value.trim() || null,
        numeroTarjetaCredito: document.getElementById('numeroTarjetaCredito').value.trim() || null
    };
}

function validateFormData(formData) {
    if (!formData.nombre || !formData.apellidos || !formData.email || !formData.contrasena) {
        showErrorMessage('Por favor, completa todos los campos obligatorios.');
        return false;
    }

    if (!isValidEmail(formData.email)) {
        showErrorMessage('Por favor, introduce un email válido.');
        return false;
    }

    if (formData.contrasena.length < 6) {
        showErrorMessage('La contraseña debe tener al menos 6 caracteres.');
        return false;
    }

    return true;
}

function registerUser(formData) {
    fetch('/api/auth/register', {
        method: 'POST',
        headers: {
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
            handleRegistrationSuccess(data);
        })
        .catch(error => {
            handleRegistrationError(error);
        })
        .finally(() => {
            showLoadingState(false);
        });
}

function handleRegistrationSuccess(data) {
    showSuccessMessage('¡Cuenta creada exitosamente! Redirigiendo al menú...');

    localStorage.setItem('jwt', data.accessToken);
    localStorage.setItem('userType', data.tipoUsuario);
    localStorage.setItem('userName', data.nombreUsuario);

    // Redirección automática al menú después de 2 segundos
    setTimeout(() => {
        window.location.href = '/menu';
    }, 2000);
}

function handleRegistrationError(error) {
    const mensaje = error.message || 'Error al crear la cuenta. Inténtalo de nuevo.';
    showErrorMessage(mensaje);
}

function validateEmail() {
    const email = document.getElementById('email').value.trim();
    if (email && isValidEmail(email)) {
        checkEmailExists(email);
    }
}

function checkEmailExists(email) {
    const encodedEmail = encodeURIComponent(email);
    fetch(`/api/auth/check-email?email=${encodedEmail}`)
        .then(response => response.json())
        .then(data => {
            if (data.exists) {
                showErrorMessage('Este email ya está registrado.');
            } else {
                clearMessages();
            }
        })
        .catch(() => {
            // Ignorar errores de verificación
        });
}

function setupFieldValidation() {
    const inputs = document.querySelectorAll('input[required]');
    inputs.forEach(input => {
        input.addEventListener('input', clearMessages);
    });
}

function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

function showErrorMessage(message) {
    const errorDiv = document.getElementById('mensajeError');
    const textSpan = errorDiv.querySelector('.mensaje-texto');
    textSpan.textContent = message;
    errorDiv.style.display = 'block';

    errorDiv.scrollIntoView({ behavior: 'smooth', block: 'center' });
}

function showSuccessMessage(message) {
    const successDiv = document.getElementById('mensajeExito');
    const textSpan = successDiv.querySelector('.mensaje-texto');
    textSpan.textContent = message;
    successDiv.style.display = 'block';

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
        submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Creando cuenta...';
    } else {
        submitBtn.disabled = false;
        submitBtn.innerHTML = '<i class="fas fa-user-plus me-2"></i>Crear Cuenta';
    }
}
