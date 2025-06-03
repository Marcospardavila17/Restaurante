// header.js - VERSIÓN ANTI-BUCLES

document.addEventListener('DOMContentLoaded', function() {
    console.log('=== HEADER CARGADO ===');

    // ✅ EVITAR MÚLTIPLES INICIALIZACIONES
    if (window.headerInitialized) {
        console.log('Header ya inicializado, saltando...');
        return;
    }
    window.headerInitialized = true;

    // ✅ DELAY PARA PERMITIR QUE localStorage SE ESTABILICE
    setTimeout(() => {
        initializeHeader();
    }, 300);

    setupLogoutHandlers();
    updateCartCount();
});

function initializeHeader() {
    console.log('=== INICIALIZANDO HEADER ===');

    // ✅ VERIFICACIÓN MÚLTIPLE CON REINTENTOS
    let attempts = 0;
    const maxAttempts = 3;

    function tryInitialize() {
        attempts++;
        console.log(`Intento ${attempts} de inicialización`);

        const jwt = localStorage.getItem('jwt');
        const userType = localStorage.getItem('userType');
        const userName = localStorage.getItem('userName');

        console.log('JWT exists:', !!jwt);
        console.log('UserType:', userType);
        console.log('UserName:', userName);
        console.log('UserName type:', typeof userName);

        // ✅ VERIFICACIÓN MÁS ROBUSTA
        if (jwt && userType && userName &&
            userType !== 'undefined' && userName !== 'undefined' &&
            userType !== 'null' && userName !== 'null' &&
            userType.length > 0 && userName.length > 0) {

            console.log('✅ Usuario autenticado:', userName);
            updateHeaderForAuthenticatedUser(userType, userName);
            return true; // Éxito
        } else {
            console.log('❌ Usuario NO autenticado - Intento:', attempts);
            console.log('Datos localStorage:', { jwt: !!jwt, userType, userName });

            // ✅ REINTENTAR SI NO ES EL ÚLTIMO INTENTO
            if (attempts < maxAttempts) {
                console.log('Reintentando en 200ms...');
                setTimeout(tryInitialize, 200);
                return false;
            } else {
                console.log('Máximo de intentos alcanzado, mostrando como invitado');
                updateHeaderForGuestUser();
                return false;
            }
        }
    }

    tryInitialize();
}

// ✅ RESTO DE FUNCIONES SIN CAMBIOS...
function updateHeaderForAuthenticatedUser(userType, userName) {
    console.log('Actualizando header para:', userType, userName);
    hideGuestLinks();
    showAuthenticatedLinks();
    updateUserName(userName);
    showLinksForUserType(userType);
}

function updateHeaderForGuestUser() {
    console.log('Mostrando header para invitado');
    showGuestLinks();
    hideAuthenticatedLinks();
}

function showLinksForUserType(userType) {
    console.log('Mostrando enlaces para tipo:', userType);
    hideAllRoleLinks();
    const roleClass = userType.toLowerCase() + '-only';
    const roleLinks = document.querySelectorAll('.' + roleClass);
    console.log('Enlaces encontrados para', roleClass, ':', roleLinks.length);
    roleLinks.forEach(link => {
        link.style.display = 'block';
    });
}

// ✅ RESTO DE FUNCIONES AUXILIARES...
function hideGuestLinks() {
    const guestLinks = document.querySelectorAll('.guest-only');
    guestLinks.forEach(link => link.style.display = 'none');
}

function showGuestLinks() {
    const guestLinks = document.querySelectorAll('.guest-only');
    guestLinks.forEach(link => link.style.display = 'block');
}

function hideAuthenticatedLinks() {
    const authLinks = document.querySelectorAll('.authenticated-only');
    authLinks.forEach(link => link.style.display = 'none');
}

function showAuthenticatedLinks() {
    const authLinks = document.querySelectorAll('.authenticated-only');
    authLinks.forEach(link => link.style.display = 'block');
}

function updateUserName(userName) {
    console.log('Actualizando nombre de usuario a:', userName);
    const userNameSpan = document.getElementById('user-name');
    if (userNameSpan) {
        userNameSpan.textContent = userName;
        console.log('✅ Nombre actualizado correctamente');
    } else {
        console.error('❌ Elemento user-name no encontrado');
    }
}

function hideAllRoleLinks() {
    const roleLinks = document.querySelectorAll('.cliente-only, .personal-only, .admin-only');
    roleLinks.forEach(link => {
        link.style.display = 'none';
    });
}

function setupLogoutHandlers() {
    const logoutButtons = document.querySelectorAll('.logout-btn');
    logoutButtons.forEach(button => {
        button.addEventListener('click', handleLogout);
    });
}

function handleLogout(event) {
    event.preventDefault();
    console.log('Cerrando sesión...');
    clearAuthenticationData();
    window.location.href = '/';
}

function updateCartCount() {
    const cart = JSON.parse(localStorage.getItem('carrito') || '[]');
    const cartCount = document.getElementById('carrito-count');
    if (cartCount) {
        if (cart.length > 0) {
            cartCount.textContent = cart.length;
            cartCount.style.display = 'inline';
        } else {
            cartCount.style.display = 'none';
        }
    }
}

function clearAuthenticationData() {
    console.log('Limpiando datos de autenticación...');
    localStorage.removeItem('jwt');
    localStorage.removeItem('userType');
    localStorage.removeItem('userName');
    localStorage.removeItem('carrito');
    window.headerInitialized = false; // ✅ RESETEAR FLAG
}
