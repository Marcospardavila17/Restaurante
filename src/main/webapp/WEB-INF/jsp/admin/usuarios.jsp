<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Gestión de Usuarios - Restaurante UNED</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<!-- ✅ INCLUIR HEADER COMO COMPONENTE -->
<jsp:include page="../components/header.jsp" />

<div class="container py-5">
    <div class="row">
        <div class="col-12">
            <div class="card shadow">
                <div class="card-header bg-primary text-white">
                    <h3 class="mb-0">
                        <i class="fas fa-users me-2"></i>Gestión de Usuarios
                    </h3>
                </div>

                <div class="card-body">
                    <!-- Botones de acción -->
                    <div class="row mb-4">
                        <div class="col-md-6">
                            <button class="btn btn-success" onclick="cargarUsuarios()">
                                <i class="fas fa-refresh me-2"></i>Cargar Usuarios
                            </button>
                            <button class="btn btn-primary" onclick="mostrarFormularioCrear()">
                                <i class="fas fa-plus me-2"></i>Nuevo Usuario
                            </button>
                        </div>
                    </div>

                    <!-- Formulario para crear/editar usuario -->
                    <div id="formularioUsuario" class="card mb-4" style="display: none;">
                        <div class="card-header">
                            <h5 id="tituloFormulario">Crear Usuario</h5>
                        </div>
                        <div class="card-body">
                            <form id="userForm">
                                <input type="hidden" id="userId">
                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label for="nombre" class="form-label">Nombre</label>
                                        <input type="text" class="form-control" id="nombre" required>
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label for="apellidos" class="form-label">Apellidos</label>
                                        <input type="text" class="form-control" id="apellidos" required>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label for="email" class="form-label">Email</label>
                                        <input type="email" class="form-control" id="email" required>
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label for="tipo" class="form-label">Tipo</label>
                                        <select class="form-control" id="tipo" required>
                                            <option value="Cliente">Cliente</option>
                                            <option value="Personal">Personal</option>
                                            <option value="Administrador">Administrador</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label for="contrasena" class="form-label">Contraseña</label>
                                        <input type="password" class="form-control" id="contrasena">
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label for="telefono" class="form-label">Teléfono</label>
                                        <input type="tel" class="form-control" id="telefono">
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-12 mb-3">
                                        <label for="direccion" class="form-label">Dirección</label>
                                        <input type="text" class="form-control" id="direccion">
                                    </div>
                                </div>
                                <button type="submit" class="btn btn-success">
                                    <i class="fas fa-save me-2"></i>Guardar
                                </button>
                                <button type="button" class="btn btn-secondary" onclick="ocultarFormulario()">
                                    <i class="fas fa-times me-2"></i>Cancelar
                                </button>
                            </form>
                        </div>
                    </div>

                    <!-- Tabla de usuarios -->
                    <div class="table-responsive">
                        <table class="table table-striped">
                            <thead class="table-dark">
                            <tr>
                                <th>ID</th>
                                <th>Nombre</th>
                                <th>Email</th>
                                <th>Tipo</th>
                                <th>Teléfono</th>
                                <th>Acciones</th>
                            </tr>
                            </thead>
                            <tbody id="tablaUsuarios">
                                <tr>
                                    <td colspan="6" class="text-center">
                                        <i class="fas fa-spinner fa-spin me-2"></i>Cargando usuarios...
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                    <!-- Mensajes -->
                    <div id="mensajeError" class="alert alert-danger mt-3" style="display: none;">
                        <i class="fas fa-exclamation-triangle me-2"></i>
                        <span class="mensaje-texto"></span>
                    </div>
                    <div id="mensajeExito" class="alert alert-success mt-3" style="display: none;">
                        <i class="fas fa-check-circle me-2"></i>
                        <span class="mensaje-texto"></span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
