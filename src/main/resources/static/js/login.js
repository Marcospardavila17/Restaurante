// login.js

document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');
    const mensajeError = document.getElementById('mensajeError');
    const emailInput = document.getElementById('email');
    const contrasenaInput = document.getElementById('contrasena');

    // Función principal de login
    loginForm.addEventListener('submit', function(e) {
        e.preventDefault();

        const email = emailInput.value;
        const contrasena = contrasenaInput.value;

        // Limpiar mensajes anteriores
        clearErrorMessage();

        // Validación básica
        if (!email || !contrasena) {
            showErrorMessage('Por favor, completa todos los campos.');
            return;
        }

        // Enviar petición de login
        authenticateUser(email, contrasena);
    });

    // Función para autenticar usuario
    function authenticateUser(email, contrasena) {
        fetch('/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email, contrasena })
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Credenciales inválidas');
                }
                return response.json();
            })
            .then(data => {
                handleLoginSuccess(data);
            })
            .catch(error => {
                handleLoginError(error);
            });
    }

    // Función para manejar login exitoso
    function handleLoginSuccess(data) {
        // Guardar datos de autenticación
        localStorage.setItem('jwt', data.accessToken);
        localStorage.setItem('userType', data.tipoUsuario);
        localStorage.setItem('userName', data.nombreUsuario);

        // Redirigir según el tipo de usuario
        redirectUser(data.tipoUsuario);
    }

    // Función para manejar errores de login
    function handleLoginError(error) {
        showErrorMessage(error.message || 'Error al iniciar sesión. Inténtalo de nuevo.');
    }

    // Función para redirigir según tipo de usuario
    function redirectUser(tipoUsuario) {
        let redirectUrl = '/menu'; // Por defecto al menú

        if (tipoUsuario === 'Cliente') {
            redirectUrl = '/menu';
        } else if (tipoUsuario === 'Personal') {
            redirectUrl = '/personal/pedidos';
        } else if (tipoUsuario === 'Administrador') {
            redirectUrl = '/admin/usuarios';
        }

        window.location.href = redirectUrl;
    }

    // Funciones auxiliares para mostrar mensajes
    function showErrorMessage(message) {
        mensajeError.textContent = message;
        mensajeError.style.display = 'block';
    }

    function clearErrorMessage() {
        mensajeError.textContent = '';
        mensajeError.style.display = 'none';
    }

    // Limpiar mensaje de error cuando el usuario empiece a escribir
    emailInput.addEventListener('input', clearErrorMessage);
    contrasenaInput.addEventListener('input', clearErrorMessage);
});
