<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<nav class="navbar navbar-expand-lg navbar-dark bg-success shadow-sm">
    <div class="container">
        <!-- Brand -->
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/">
            <i class="fas fa-utensils me-2"></i>Restaurante UNED
        </a>

        <!-- Mobile toggle -->
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>

        <!-- Navigation -->
        <!-- Left side navigation -->
        <ul class="navbar-nav me-auto">
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/">
                    <i class="fas fa-home me-1"></i>Menú
                </a>
            </li>

            <!-- Cliente links -->
            <li class="nav-item cliente-only" style="display: none;">
                <a class="nav-link" href="${pageContext.request.contextPath}/cliente/carrito">
                    <i class="fas fa-shopping-cart me-1"></i>Carrito
                    <span id="carrito-count" class="badge bg-danger ms-1" style="display: none;">0</span>
                </a>
            </li>
            <li class="nav-item cliente-only" style="display: none;">
                <a class="nav-link" href="${pageContext.request.contextPath}/cliente/pedidos">
                    <i class="fas fa-list-alt me-1"></i>Mis Pedidos
                </a>
            </li>

            <!-- Personal links -->
            <li class="nav-item personal-only" style="display: none;">
                <a class="nav-link" href="${pageContext.request.contextPath}/personal/pedidos">
                    <i class="fas fa-tasks me-1"></i>Gestionar Pedidos
                </a>
            </li>

            <!-- Admin links -->
            <li class="nav-item admin-only" style="display: none;">
                <a class="nav-link" href="${pageContext.request.contextPath}/admin/productos">
                    <i class="fas fa-box me-1"></i>Productos
                </a>
            </li>
            <li class="nav-item admin-only" style="display: none;">
                <a class="nav-link" href="#" onclick="irAUsuarios(); return false;">
                    <i class="fas fa-users me-1"></i>Usuarios
                </a>
            </li>
        </ul>

        <!-- Right side navigation -->
        <ul class="navbar-nav">
            <!-- Guest links -->
            <li class="nav-item guest-only">
                <a class="nav-link" href="${pageContext.request.contextPath}/auth/login">
                    <i class="fas fa-sign-in-alt me-1"></i>Iniciar sesión
                </a>
            </li>
            <li class="nav-item guest-only">
                <a class="nav-link" href="${pageContext.request.contextPath}/auth/register">
                    <i class="fas fa-user-plus me-1"></i>Registrarse
                </a>
            </li>

            <!-- Authenticated user dropdown -->
            <li class="nav-item dropdown authenticated-only" style="display: none;">
                <a class="nav-link dropdown-toggle d-flex align-items-center" href="#" id="userDropdown"
                   role="button" data-bs-toggle="dropdown" aria-expanded="false">
                    <i class="fas fa-user-circle me-2 fs-5"></i>
                    <span id="user-name">Usuario</span>
                </a>
                <ul class="dropdown-menu dropdown-menu-end shadow">
                    <li>
                        <h6 class="dropdown-header">
                            <i class="fas fa-user me-1"></i>Mi Cuenta
                        </h6>
                    </li>
                    <li>
                        <a class="dropdown-item" href="#" onclick="irAPerfil(); return false;">
                            <i class="fas fa-edit me-2"></i>Mi Perfil
                        </a>
                    </li>
                    <li><hr class="dropdown-divider"></li>
                    <li>
                        <button type="button" class="dropdown-item text-danger logout-btn">
                            <i class="fas fa-sign-out-alt me-2"></i>Cerrar Sesión
                        </button>
                    </li>
                </ul>
            </li>
        </ul>
    </div>
</nav>

<!-- Toast container para mensajes -->
<div id="toast-container" class="position-fixed top-0 end-0 p-3" style="z-index: 1055;"></div>
