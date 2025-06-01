<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Menú del Restaurante</title>
    <meta charset="UTF-8">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .producto-card { min-height: 320px; }
        .producto-img { max-height: 140px; object-fit: cover; }
    </style>
</head>
<body>

<jsp:include page="../components/header.jsp" />

<div class="container">
    <h1 class="mb-4">Menú del Restaurante</h1>
    <div id="menu-container">
        <div class="text-center">
            <div class="spinner-border text-primary" role="status"></div>
            <span class="ms-2">Cargando menú...</span>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="<c:url value='/js/header.js' />"></script>
<script src="<c:url value='/js/menu.js' />"></script>
</body>
</html>
