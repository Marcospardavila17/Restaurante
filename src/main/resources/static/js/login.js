// login.js - VERSIÓN SIMPLIFICADA CON LOGS

document.addEventListener('DOMContentLoaded', function() {
    console.log('=== LOGIN CARGADO ===');
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', handleLogin);
    }
});

function handleLogin(event) {
    event.preventDefault();
    console.log('=== INICIANDO LOGIN ===');

    const email = document.getElementById('email').value;
    const contrasena = document.getElementById('contrasena').value;

    console.log('Email:', email);

    const loginData = {
        email: email,
        contrasena: contrasena
    };

    fetch('/api/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(loginData)
    })
        .then(response => {
            console.log('Respuesta del servidor:', response.status);
            if (!response.ok) {
                throw new Error('Credenciales incorrectas');
            }
            return response.json();
        })
        .then(data => {
            console.log('=== LOGIN EXITOSO ===');
            console.log('Datos recibidos:', {
                hasToken: !!data.accessToken,
                userName: data.userName,
                userType: data.userType
            });

            // Guardar datos en localStorage
            localStorage.setItem('jwt', data.accessToken);
            localStorage.setItem('userName', data.userName);
            localStorage.setItem('userType', data.userType);

            console.log('=== DATOS GUARDADOS ===');
            console.log('JWT guardado:', !!localStorage.getItem('jwt'));
            console.log('UserName guardado:', localStorage.getItem('userName'));
            console.log('UserType guardado:', localStorage.getItem('userType'));

            // ✅ REDIRECCIÓN SIMPLE SIN PARÁMETROS
            console.log('Redirigiendo a página principal...');
            setTimeout(() => {
                window.location.href = '/';
            }, 500);
        })
        .catch(error => {
            console.error('❌ Error en login:', error);
            showError(error.message);
        });
}

function showError(message) {
    console.log('Mostrando error:', message);
    const errorDiv = document.getElementById('mensajeError');
    if (errorDiv) {
        errorDiv.textContent = message;
        errorDiv.style.display = 'block';
    }
}
