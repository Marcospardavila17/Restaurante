// menu.js

function renderMenu(productos) {
    // Agrupar productos por categoría
    const categorias = {};
    productos.forEach(p => {
        if (!categorias[p.categoria]) categorias[p.categoria] = [];
        categorias[p.categoria].push(p);
    });

    let html = '';
    Object.keys(categorias).forEach(cat => {
        html += `<h2 class="mt-4">${cat}</h2><div class="row">`;
        categorias[cat].forEach(p => {
            html += `
            <div class="col">
                <div class="card h-100 producto-card shadow-sm border-0">
                    ${p.imagen ? `<img src="${p.imagen}" class="card-img-top producto-img" alt="Imagen de ${p.nombre}">` : ''}
                    <div class="card-body">
                        <div>
                            <h5 class="card-title text-success">${p.nombre}</h5>
                            ${p.descripcion ? `<p class="card-text text-muted card-text-scroll">${p.descripcion}</p>` : ''}
                        </div>
                        <div>
                            <p class="fw-bold mb-1">Precio: <span class="text-danger">${parseFloat(p.precio).toFixed(2)} €</span></p>
                            ${p.ingredientes ? `<small class="d-block text-secondary"><strong>Ingredientes:</strong> ${p.ingredientes}</small>` : ''}
                            ${p.alergenos ? `<small class="d-block text-danger"><strong>Alérgenos:</strong> ${p.alergenos}</small>` : ''}
                        </div>
                    </div>
                </div>
            </div>
            `;
        });
        html += '</div>';
    });
    document.getElementById('menu-container').innerHTML = html;
}

document.addEventListener('DOMContentLoaded', function() {
    fetch('/api/productos')
        .then(resp => {
            if (!resp.ok) throw new Error('No se pudo cargar el menú');
            return resp.json();
        })
        .then(renderMenu)
        .catch(err => {
            document.getElementById('menu-container').innerHTML =
                `<div class="alert alert-danger">Error al cargar el menú: ${err.message}</div>`;
        });
});
