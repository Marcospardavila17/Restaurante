-- Limpiar datos existentes (en orden inverso por las claves foraneas)
DELETE FROM PERSONALIZACION;
DELETE FROM DETALLE_PEDIDO;
DELETE FROM PEDIDO;
DELETE FROM PRODUCTO_INGREDIENTE;
DELETE FROM PRODUCTO;
DELETE FROM USUARIO;
DELETE FROM INGREDIENTE;

-- Reiniciar contadores de auto-incremento para HSQLDB
ALTER TABLE INGREDIENTE ALTER COLUMN id RESTART WITH 1;
ALTER TABLE USUARIO ALTER COLUMN id RESTART WITH 1;
ALTER TABLE PRODUCTO ALTER COLUMN id RESTART WITH 1;
ALTER TABLE PEDIDO ALTER COLUMN id RESTART WITH 1;
ALTER TABLE DETALLE_PEDIDO ALTER COLUMN id RESTART WITH 1;
ALTER TABLE PERSONALIZACION ALTER COLUMN id RESTART WITH 1;

-- INGREDIENTE (debe ir primero)
INSERT INTO INGREDIENTE (id, nombre, cantidad, unidad) VALUES
                                                           (1, 'Harina', 10000, 'gramos'),
                                                           (2, 'Tomate', 5000, 'gramos'),
                                                           (3, 'Mozzarella', 3000, 'gramos'),
                                                           (4, 'Pollo', 2000, 'gramos'),
                                                           (5, 'Lechuga', 1500, 'gramos'),
                                                           (6, 'Pan', 100, 'unidades'),
                                                           (7, 'Queso', 1500, 'gramos'),
                                                           (8, 'Cerveza', 200, 'litros'),
                                                           (9, 'Azucar', 1000, 'gramos'),
                                                           (10, 'Huevo', 500, 'unidades');

-- USUARIO (debe ir antes de PEDIDO)
INSERT INTO USUARIO (id, tipo, nombre, apellidos, email, contrasena, direccion, poblacion, provincia, codigo_postal, telefono, numero_tarjeta_credito) VALUES
                                                                                                                                                           (1, 'Administrador', 'Admin', 'Sistema', 'admin@restaurante.com', '$2a$10$TU_CONTRASEÑA_CIFRADA_AQUI', NULL, NULL, NULL, NULL, NULL, NULL),
                                                                                                                                                           (2, 'Cliente', 'Laura', 'Garcia', 'laura@example.com', '$2a$10$cliente456hash', 'Avenida Sol 2', 'Sevilla', 'Sevilla', '41001', '600456456', '5555666677778888'),
                                                                                                                                                           (3, 'Personal', 'Ana', 'Gomez', 'ana@restaurante.com', '$2a$10$empleado123hash', 'Calle Luna 3', 'Madrid', 'Madrid', '28002', '600789789', NULL),
                                                                                                                                                           (4, 'Administrador', 'Carlos', 'Ruiz', 'carlos.ruiz@restaurante.com', '$2a$10$admin123hash', NULL, NULL, NULL, NULL, NULL, NULL); -- Email cambiado para ser único

-- PRODUCTO (debe ir despues de INGREDIENTE)
INSERT INTO PRODUCTO (id, nombre, descripcion, precio, categoria, imagen, ingredientes, alergenos, tiempo_preparacion) VALUES
                                                                                                                           (1, 'Pizza Margarita', 'Pizza clasica con tomate y mozzarella', 12.99, 'Platos principales', 'pizza.jpg', 'Harina, Tomate, Mozzarella', 'Gluten, Lactosa', 20),
                                                                                                                           (2, 'Hamburguesa de Pollo', 'Hamburguesa con pan, pollo y queso', 9.50, 'Platos principales', 'hamburguesa.jpg', 'Pan, Pollo, Queso', 'Gluten, Lactosa', 15),
                                                                                                                           (3, 'Ensalada Cesar', 'Lechuga, pollo, queso y salsa Cesar', 8.50, 'Entrantes', 'ensalada.jpg', 'Lechuga, Pollo, Queso, Huevo', 'Lactosa, Huevo', 10),
                                                                                                                           (4, 'Cerveza Artesana', 'Cerveza rubia artesanal', 3.00, 'Bebidas', 'cerveza.jpg', NULL, 'Gluten', NULL),
                                                                                                                           (5, 'Tarta de Queso', 'Tarta casera de queso y azucar', 5.00, 'Postres', 'tarta.jpg', 'Queso, Azucar, Huevo', 'Lactosa, Huevo', 5);

-- PRODUCTO_INGREDIENTE (debe ir despues de PRODUCTO e INGREDIENTE)
INSERT INTO PRODUCTO_INGREDIENTE (id_producto, id_ingrediente, cantidad_unitaria) VALUES
                                                                                      (1, 1, 200),
                                                                                      (1, 2, 100),
                                                                                      (1, 3, 100),
                                                                                      (2, 6, 1),
                                                                                      (2, 4, 120),
                                                                                      (2, 7, 50),
                                                                                      (3, 5, 80),
                                                                                      (3, 4, 60),
                                                                                      (3, 7, 30),
                                                                                      (3, 10, 1),
                                                                                      (4, 8, 0.33),
                                                                                      (5, 7, 60),
                                                                                      (5, 9, 20),
                                                                                      (5, 10, 2);

-- PEDIDO (debe ir después de USUARIO)
INSERT INTO PEDIDO (id, id_usuario, fecha, estado, total) VALUES
                                                              (1, 1, CURRENT_TIMESTAMP, 'Pendiente', 23.99),
                                                              (2, 2, CURRENT_TIMESTAMP, 'En preparación', 17.50);

-- DETALLE_PEDIDO (debe ir después de PEDIDO y PRODUCTO)
INSERT INTO DETALLE_PEDIDO (id, id_pedido, id_producto, cantidad, precio_unitario, subtotal) VALUES
                                                                                                 (1, 1, 1, 1, 12.99, 12.99),
                                                                                                 (2, 1, 4, 2, 3.00, 6.00),
                                                                                                 (3, 1, 5, 1, 5.00, 5.00),
                                                                                                 (4, 2, 2, 1, 9.50, 9.50),
                                                                                                 (5, 2, 3, 1, 8.50, 8.50);

-- PERSONALIZACION
INSERT INTO PERSONALIZACION (id, id_detalle_pedido, nombre, descripcion) VALUES
                                                                             (1, 1, 'Sin Mozzarella', 'Quitar el queso mozzarella de la pizza'),
                                                                             (2, 2, 'Sin alcohol', 'Cerveza sin alcohol'),
                                                                             (3, 3, 'Extra azúcar', 'Añadir un extra de azúcar a la tarta'),
                                                                             (4, 4, 'Sin queso', 'Hamburguesa de pollo sin queso'),
                                                                             (5, 5, 'Sin salsa', 'Ensalada César sin salsa');
