document.addEventListener('DOMContentLoaded', function() {
    console.log('=== HEADER CARGADO ===');
    if (window.headerInitialized) {
        console.log('Header ya inicializado, saltando...');
        return;
    }
    window.headerInitialized = true;
    setTimeout(() => {
        initializeHeader();
    }, 300);
    setupLogoutHandlers();
    updateCartCount();
});

function initializeHeader() {
    console.log('=== INICIALIZANDO HEADER ===');
    let attempts = 0;
    const maxAttempts = 3;
    function tryInitialize() {
        attempts++;
        const jwt = localStorage.getItem('jwt');
        const userType = localStorage.getItem('userType');
        const userName = localStorage.getItem('userName');

        console.log(`Intento ${attempts} | JWT: ${!!jwt} | userType: ${userType} | userName: ${userName}`);

        // ✅ Condición mejorada
        const isAuthenticated = jwt &&
            userType?.trim() && // Verifica que no sea null, undefined, o cadena vacía
            userName?.trim();   // Usa optional chaining para evitar errores

        if (isAuthenticated) {
            console.log('✅ Usuario autenticado:', userName, userType);
            updateHeaderForAuthenticatedUser(userType, userName);
        } else {
            console.log('❌ Usuario NO autenticado - Intento:', attempts);
            if (attempts < maxAttempts) {
                setTimeout(tryInitialize, 200);
            } else {
                updateHeaderForGuestUser();
            }
        }
    }
    tryInitialize();
}

function updateHeaderForAuthenticatedUser(userType, userName) {
    hideGuestLinks();
    updateUserName(userName);
    showAuthenticatedLinks();
    showLinksForUserType(userType);
}

function updateHeaderForGuestUser() {
    showGuestLinks();
    hideAuthenticatedLinks();
    hideAllRoleLinks();
}

function showLinksForUserType(userType) {
    console.log("Mostrando links por role");
    console.log(userType.trim().toLowerCase());
    let roleClass;
    switch(userType.trim().toLowerCase()) {
        case 'administrador':
            roleClass = 'admin-only'; // ← Clase correcta
            break;
        case 'personal':
            roleClass = 'personal-only';
            break;
        case 'cliente':
            roleClass = 'cliente-only';
            break;
        default:
            return;
    }

    document.querySelectorAll('.' + roleClass).forEach(link => {
        link.style.display = 'block'; // ← Muestra los elementos
        // Si usas Bootstrap, añade:
        link.classList.remove('d-none');
        link.classList.add('d-block');
    });
    console.log("Ya deberian aparecer^^^^^^^^^^^^^^^^^^");
}


function hideAllRoleLinks() {
    console.log("Ocultando links por rol ^^^^^^^^^^^^^^^^^^^^")
    document.querySelectorAll('.admin-only, .personal-only, .cliente-only').forEach(el => {
        el.style.display = 'none'; // ← Oculta las clases correctas
        // Si usas Bootstrap, añade:
        el.classList.add('d-none');
    });
}

function hideGuestLinks() {
    document.querySelectorAll('.guest-only').forEach(link => link.style.display = 'none');
}
function showGuestLinks() {
    document.querySelectorAll('.guest-only').forEach(link => link.style.display = 'block');
}
function hideAuthenticatedLinks() {
    document.querySelectorAll('.authenticated-only').forEach(link => link.style.display = 'none');
}
function showAuthenticatedLinks() {
    document.querySelectorAll('.authenticated-only').forEach(link => link.style.display = 'block');
}
function updateUserName(userName) {
    const userNameSpan = document.getElementById('user-name');
    if (userNameSpan) userNameSpan.textContent = userName;
}
function setupLogoutHandlers() {
    document.querySelectorAll('.logout-btn').forEach(button => {
        button.addEventListener('click', handleLogout);
    });
}
function handleLogout(event) {
    event.preventDefault();
    clearAuthenticationData();
    window.location.href = '/';
}
function clearAuthenticationData() {
    localStorage.removeItem('jwt');
    localStorage.removeItem('userType');
    localStorage.removeItem('userName');
    localStorage.removeItem('carrito');
    window.headerInitialized = false;
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
function irAUsuarios() {
    const jwtToken = localStorage.getItem('jwt');
    const userType = localStorage.getItem('userType');
    console.log("^^^^^^^^^^^^^^^^^^^^^^^^^^^^Empezamos irAUsuarios");
    if (jwtToken && userType && userType.trim().toLowerCase() === 'administrador') {
        showLoadingSpinner();
        console.log("Antes del fetch de irAUsuarios");
        fetch('/admin/usuarios', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${jwtToken}`,
                'X-Requested-With': 'XMLHttpRequest'
            }
        })
            .then(response => {
                if (response.ok) {
                    return response.text();
                } else if (response.status === 403) {
                    alert("Acceso denegado. No tienes permisos de administrador.");
                    return null;
                } else if (response.status === 401) {
                    alert("Sesión expirada. Por favor, inicia sesión de nuevo.");
                    handleSessionExpired();
                    return null;
                } else {
                    throw new Error('Error al cargar la página: ' + response.statusText);
                }
            })
            .then(html => {
                if (html) {
                    console.log("Antes de cargar el html**********************");
                    replaceMainContent(html);
                    loadPageSpecificScripts();
                }
            })
            .catch(error => {
                console.log("parece que hay un error");
                console.error('Error:', error);
                alert("Error al cargar la página de usuarios.");
            })
            .finally(() => {
                hideLoadingSpinner();
                console.log("Fin irAUsuarios^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            });
    } else {
        redirectToLogin();
    }
}

// 🔥 FUNCIÓN CLAVE: Reemplazar solo contenido, mantener header
function replaceMainContent(html) {
    // Crear un elemento temporal para parsear el HTML
    const tempDiv = document.createElement('div');
    tempDiv.innerHTML = html;

    // Buscar el container principal (ajusta el selector según tu estructura)
    const newContent = tempDiv.querySelector('.container') || tempDiv.querySelector('main') || tempDiv;

    // Reemplazar solo el contenido principal
    const mainContainer = document.querySelector('.container') || document.querySelector('main');
    if (mainContainer && newContent) {
        mainContainer.innerHTML = newContent.innerHTML;

        // Actualizar el título de la página
        const newTitle = tempDiv.querySelector('title');
        if (newTitle) {
            document.title = newTitle.textContent;
        }
    }

    // 🔥 IMPORTANTE: El header se mantiene intacto
}

// Cargar scripts específicos de la página
function loadPageSpecificScripts() {
    // Si ya existe el script y sus funciones globales, solo re-ejecutamos
    if (window.adminUsuariosLoaded && typeof initializeAdminUsuarios === 'function') {
        console.log('♻️ admin-usuarios.js ya cargado, re-inicializando funciones...');
        initializeAdminUsuarios();
        return;
    }

    const script = document.createElement('script');
    script.src = '/js/admin-usuarios.js';
    script.onload = () => {
        window.adminUsuariosLoaded = true;
        console.log('✅ admin-usuarios.js cargado dinámicamente.');

        // 🔥 CLAVE: Usar un pequeño retraso para asegurar que el DOM
        // inyectado por replaceMainContent() haya sido parseado por el navegador.
        // Opcional: Podrías usar un MutationObserver aquí para ser más preciso,
        // pero un setTimeout es a menudo suficiente.
        setTimeout(() => {
            if (typeof initializeAdminUsuarios === 'function') {
                console.log('🔄 Ejecutando initializeAdminUsuarios...');
                initializeAdminUsuarios();
            } else {
                console.error('❌ initializeAdminUsuarios no es una función después de la carga.');
            }
        }, 50);
    };
    script.onerror = () => {
        console.error('❌ Error al cargar admin-usuarios.js');
    };
    document.head.appendChild(script);
}

// Utilities
function showLoadingSpinner() {
    // Crear o mostrar spinner de loading
    let spinner = document.getElementById('globalSpinner');
    if (!spinner) {
        spinner = document.createElement('div');
        spinner.id = 'globalSpinner';
        spinner.innerHTML = `
            <div class="d-flex justify-content-center align-items-center position-fixed w-100 h-100" 
                 style="top:0;left:0;background:rgba(0,0,0,0.5);z-index:9999">
                <div class="spinner-border text-primary" role="status">
                    <span class="visually-hidden">Cargando...</span>
                </div>
            </div>
        `;
        document.body.appendChild(spinner);
    }
    spinner.style.display = 'block';
}

function hideLoadingSpinner() {
    const spinner = document.getElementById('globalSpinner');
    if (spinner) {
        spinner.style.display = 'none';
    }
}

function handleSessionExpired() {
    clearAuthenticationData();
    redirectToLogin();
}

function redirectToLogin() {
    window.location.href = '/auth/login';
}

