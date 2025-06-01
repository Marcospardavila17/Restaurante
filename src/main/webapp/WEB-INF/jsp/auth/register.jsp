<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Registro - Restaurante UNED</title>
    <meta charset="UTF-8">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>

<body class="bg-light">

<jsp:include page="../components/header.jsp" />

<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-lg-8 col-xl-7">
            <div class="card shadow">
                <div class="card-header bg-success text-white text-center py-3">
                    <h3 class="mb-0">
                        <i class="fas fa-user-plus me-2"></i>Crear Cuenta
                    </h3>
                    <p class="mb-0 opacity-75">Únete a Restaurante UNED</p>
                </div>

                <div class="card-body p-4">
                    <form id="registerForm">
                        <div class="row mb-4">
                            <div class="col-12">
                                <h5 class="text-success mb-3">
                                    <i class="fas fa-user me-2"></i>Datos Personales
                                </h5>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="nombre" class="form-label">
                                    Nombre <span class="text-danger">*</span>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-text">
                                        <i class="fas fa-user"></i>
                                    </span>
                                    <input type="text" class="form-control" id="nombre" name="nombre" required>
                                </div>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="apellidos" class="form-label">
                                    Apellidos <span class="text-danger">*</span>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-text">
                                        <i class="fas fa-user"></i>
                                    </span>
                                    <input type="text" class="form-control" id="apellidos" name="apellidos" required>
                                </div>
                            </div>
                        </div>

                        <div class="row mb-4">
                            <div class="col-12">
                                <h5 class="text-success mb-3">
                                    <i class="fas fa-key me-2"></i>Credenciales de Acceso
                                </h5>
                            </div>
                            <div class="col-12 mb-3">
                                <label for="email" class="form-label">
                                    Email <span class="text-danger">*</span>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-text">
                                        <i class="fas fa-envelope"></i>
                                    </span>
                                    <input type="email" class="form-control" id="email" name="email" required>
                                </div>
                            </div>
                            <div class="col-12 mb-3">
                                <label for="contrasena" class="form-label">
                                    Contraseña <span class="text-danger">*</span>
                                </label>
                                <div class="input-group">
                                    <span class="input-group-text">
                                        <i class="fas fa-lock"></i>
                                    </span>
                                    <input type="password" class="form-control" id="contrasena" name="contrasena" required minlength="6">
                                </div>
                                <div class="form-text">
                                    <i class="fas fa-info-circle me-1"></i>Mínimo 6 caracteres
                                </div>
                            </div>
                        </div>

                        <div class="row mb-4">
                            <div class="col-12">
                                <h5 class="text-success mb-3">
                                    <i class="fas fa-map-marker-alt me-2"></i>Dirección de Entrega
                                </h5>
                            </div>
                            <div class="col-12 mb-3">
                                <label for="direccion" class="form-label">Dirección</label>
                                <div class="input-group">
                                    <span class="input-group-text">
                                        <i class="fas fa-home"></i>
                                    </span>
                                    <input type="text" class="form-control" id="direccion" name="direccion"
                                           placeholder="Calle, número, piso...">
                                </div>
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="poblacion" class="form-label">Población</label>
                                <input type="text" class="form-control" id="poblacion" name="poblacion">
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="provincia" class="form-label">Provincia</label>
                                <input type="text" class="form-control" id="provincia" name="provincia">
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="codigoPostal" class="form-label">Código Postal</label>
                                <input type="text" class="form-control" id="codigoPostal" name="codigoPostal"
                                       pattern="[0-9]{5}" placeholder="12345">
                            </div>
                        </div>

                        <div class="row mb-4">
                            <div class="col-12">
                                <h5 class="text-success mb-3">
                                    <i class="fas fa-credit-card me-2"></i>Contacto y Pago
                                </h5>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="telefono" class="form-label">Teléfono</label>
                                <div class="input-group">
                                    <span class="input-group-text">
                                        <i class="fas fa-phone"></i>
                                    </span>
                                    <input type="tel" class="form-control" id="telefono" name="telefono"
                                           pattern="[0-9]{9,15}" placeholder="123456789">
                                </div>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="numeroTarjetaCredito" class="form-label">Tarjeta de Crédito</label>
                                <div class="input-group">
                                    <span class="input-group-text">
                                        <i class="fas fa-credit-card"></i>
                                    </span>
                                    <input type="text" class="form-control" id="numeroTarjetaCredito"
                                           name="numeroTarjetaCredito" pattern="[0-9]{16}"
                                           placeholder="1234567890123456">
                                </div>
                                <div class="form-text">
                                    <i class="fas fa-info-circle me-1"></i>16 dígitos sin espacios
                                </div>
                            </div>
                        </div>

                        <div class="d-grid gap-2">
                            <button type="submit" class="btn btn-success btn-lg">
                                <i class="fas fa-user-plus me-2"></i>Crear Cuenta
                            </button>
                        </div>
                    </form>

                    <div id="mensajeError" class="alert alert-danger mt-3" style="display: none;">
                        <i class="fas fa-exclamation-triangle me-2"></i>
                        <span class="mensaje-texto"></span>
                    </div>
                    <div id="mensajeExito" class="alert alert-success mt-3" style="display: none;">
                        <i class="fas fa-check-circle me-2"></i>
                        <span class="mensaje-texto"></span>
                    </div>
                </div>

                <div class="card-footer bg-light text-center py-3">
                    <span class="text-muted">¿Ya tienes cuenta? </span>
                    <a href="${pageContext.request.contextPath}/auth/login" class="text-success text-decoration-none fw-bold">
                        <i class="fas fa-sign-in-alt me-1"></i>Inicia sesión aquí
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="<c:url value='/js/header.js' />"></script>
<script src="<c:url value='/js/register.js' />"></script>
</body>
</html>
