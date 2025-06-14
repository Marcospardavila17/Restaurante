-- 1. USUARIO
CREATE TABLE USUARIO (
                         id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                         tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('Cliente', 'Personal', 'Administrador')),
                         nombre VARCHAR(50) NOT NULL,
                         apellidos VARCHAR(100) NOT NULL,
                         email VARCHAR(100) UNIQUE NOT NULL,
                         contrasena VARCHAR(255) NOT NULL,
                         direccion VARCHAR(255),
                         poblacion VARCHAR(100),
                         provincia VARCHAR(100),
                         codigo_postal VARCHAR(10),
                         telefono VARCHAR(20),
                         numero_tarjeta_credito VARCHAR(20)
);

-- 2. INGREDIENTE
CREATE TABLE INGREDIENTE (
                             id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                             nombre VARCHAR(100) NOT NULL,
                             cantidad DECIMAL(10, 2) NOT NULL,
                             unidad VARCHAR(20)
);

-- 3. PRODUCTO
CREATE TABLE PRODUCTO (
                          id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                          nombre VARCHAR(100) NOT NULL,
                          descripcion VARCHAR(255),
                          precio DECIMAL(10, 2) NOT NULL,
                          categoria VARCHAR(50) NOT NULL,
                          imagen VARCHAR(255),
                          ingredientes VARCHAR(255), -- Info rápida, la relación real está en PRODUCTO_INGREDIENTE
                          alergenos VARCHAR(255),
                          tiempo_preparacion INTEGER
);

-- 4. PRODUCTO_INGREDIENTE (relación muchos a muchos)
CREATE TABLE PRODUCTO_INGREDIENTE (
                                      id_producto INTEGER NOT NULL,
                                      id_ingrediente INTEGER NOT NULL,
                                      cantidad_unitaria DECIMAL(10, 2) NOT NULL,
                                      PRIMARY KEY (id_producto, id_ingrediente),
                                      FOREIGN KEY (id_producto) REFERENCES PRODUCTO(id) ON DELETE CASCADE,
                                      FOREIGN KEY (id_ingrediente) REFERENCES INGREDIENTE(id) ON DELETE CASCADE
);

-- 5. PEDIDO
CREATE TABLE PEDIDO (
                        id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                        id_usuario INTEGER NOT NULL,
                        fecha TIMESTAMP NOT NULL,
                        estado VARCHAR(20) NOT NULL CHECK (estado IN ('Pendiente', 'En preparación', 'Enviado', 'Entregado')),
                        total DECIMAL(10, 2) NOT NULL,
                        FOREIGN KEY (id_usuario) REFERENCES USUARIO(id) ON DELETE CASCADE
);

-- 6. DETALLE_PEDIDO
CREATE TABLE DETALLE_PEDIDO (
                                id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                id_pedido INTEGER NOT NULL,
                                id_producto INTEGER NOT NULL,
                                cantidad INTEGER NOT NULL,
                                precio_unitario DECIMAL(10, 2) NOT NULL,
                                subtotal DECIMAL(10, 2) NOT NULL,
                                FOREIGN KEY (id_pedido) REFERENCES PEDIDO(id) ON DELETE CASCADE,
                                FOREIGN KEY (id_producto) REFERENCES PRODUCTO(id) ON DELETE CASCADE
);

-- 7. PERSONALIZACION
CREATE TABLE PERSONALIZACION (
                                 id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                 id_detalle_pedido INTEGER NOT NULL,
                                 nombre VARCHAR(100) NOT NULL,
                                 descripcion VARCHAR(255),
                                 FOREIGN KEY (id_detalle_pedido) REFERENCES DETALLE_PEDIDO(id) ON DELETE CASCADE
);