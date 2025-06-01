// header.js

document.addEventListener('DOMContentLoaded', function() {
    initializeHeader();
    setupLogoutHandlers();
    updateCartCount();
});

// Función principal de inicialización del header
function initializeHeader() {
    const jwt = localStorage.getItem('jwt');
    const userType = localStorage.getItem('userType');
    const userName = localStorage.getItem('userName');

    if (jwt && userType && userName) {
        updateHeaderForAuthenticatedUser(userType, userName);
    } else {
        updateHeaderForGuestUser();
    }
}

// Actualizar header para usuario autenticado
function updateHeaderForAuthenticatedUser(userType, userName) {
    // Ocultar enlaces de invitado
    hideGuestLinks();

    // Mostrar enlaces de usuario autenticado
    showAuthenticatedLinks();

    // Actualizar nombre de usuario
    updateUserName(userName);

    // Mostrar enlaces específicos del rol
    showLinksForUserType(userType);
}

// Actualizar header para usuario invitado
function updateHeaderForGuestUser() {
    showGuestLinks();
    hideAuthenticatedLinks();
}

// Mostrar enlaces específicos según tipo de usuario
function showLinksForUserType(userType) {
    // Ocultar todos los enlaces específicos de rol
    hideAllRoleLinks();

    // Mostrar enlaces del rol actual
    const roleClass = userType.toLowerCase() + '-only';
    const roleLinks = document.querySelectorAll('.' + roleClass);
    roleLinks.forEach(link => {
        link.style.display = 'block';
    });
}

// Configurar manejadores de logout
function setupLogoutHandlers() {
    const logoutButtons = document.querySelectorAll('.logout-btn');
    logoutButtons.forEach(button => {
        button.addEventListener('click', handleLogout);
    });
}

// Manejar logout
function handleLogout(event) {
    event.preventDefault();

    // Limpiar localStorage
    clearAuthenticationData();

    // Mostrar mensaje de confirmación
    showLogoutMessage();

    // Redirigir después de un breve delay
    setTimeout(() => {
        window.location.href = '/';
    }, 1000);
}

// Actualizar contador del carrito
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

// Funciones auxiliares
function hideGuestLinks() {
    const guestLinks = document.querySelectorAll('.guest-only');
    guestLinks.forEach(link => link.style.display = 'none');
}

function showGuestLinks() {
    const guestLinks = document.querySelectorAll('.guest-only');
    guestLinks.forEach(link => link.style.display = 'block');
}

function hideAuthenticatedLinks() {
    const authLinks = document.querySelectorAll('.auth-only');
    authLinks.forEach(link => link.style.display = 'none');
}

function showAuthenticatedLinks() {
    const authLinks = document.querySelectorAll('.auth-only');
    authLinks.forEach(link => link.style.display = 'block');
}

function updateUserName(userName) {
    const userNameSpan = document.getElementById('user-name');
    if (userNameSpan) {
        userNameSpan.textContent = userName;
    }
}

function hideAllRoleLinks() {
    const roleLinks = document.querySelectorAll('.cliente-only, .personal-only, .admin-only');
    roleLinks.forEach(link => {
        link.style.display = 'none';
    });
}

function clearAuthenticationData() {
    localStorage.removeItem('jwt');
    localStorage.removeItem('userType');
    localStorage.removeItem('userName');
    localStorage.removeItem('carrito');
}

function showLogoutMessage() {
    // Crear toast de Bootstrap para mostrar mensaje
    const toastContainer = document.getElementById('toast-container');
    if (toastContainer) {
        const toast = document.createElement('div');
        toast.className = 'toast align-items-center text-white bg-success border-0';
        toast.innerHTML = `
            <div class="d-flex">
                <div class="toast-body">
                    Sesión cerrada correctamente
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>
        `;
        toastContainer.appendChild(toast);

        const bsToast = new bootstrap.Toast(toast);
        bsToast.show();
    }
}

// Función pública para actualizar carrito (llamada desde otras páginas)
window.updateHeaderCartCount = updateCartCount;
