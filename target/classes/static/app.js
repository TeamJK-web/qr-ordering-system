let menuItems = [];
let cart = {}; // { itemId: quantity }
const restaurantSlug = new URLSearchParams(window.location.search).get('restaurant') || 'demo';
const qrTable = new URLSearchParams(window.location.search).get('table');
const orderStorageKey = `customer-orders:${restaurantSlug}:${qrTable || 'manual'}`;

async function loadMenu() {
    try {
        const res = await fetch(`/api/restaurants/${encodeURIComponent(restaurantSlug)}/menu`);
        menuItems = await res.json();
        renderCategoryNav();
        renderMenu();
    } catch (e) {
        document.getElementById('menuContainer').innerHTML =
            '<p style="text-align:center;color:#aaa;padding:40px">Unable to load menu. Please try again.</p>';
    }
}

function getCategories() {
    return [...new Set(menuItems.map(i => i.category))];
}

function renderCategoryNav() {
    const nav = document.getElementById('categoryNav');
    nav.innerHTML = getCategories().map(cat =>
        `<button class="cat-btn" onclick="scrollToCategory('${cat}')">${cat}</button>`
    ).join('');
}

function scrollToCategory(cat) {
    const el = document.getElementById('cat-' + cat.replace(/\s/g, '-'));
    if (el) el.scrollIntoView({ behavior: 'smooth', block: 'start' });
}

function renderMenu() {
    const container = document.getElementById('menuContainer');
    const categories = getCategories();

    container.innerHTML = categories.map(cat => {
        const items = menuItems.filter(i => i.category === cat);
        return `
            <div class="category-section" id="cat-${cat.replace(/\s/g, '-')}">
                <h2 class="category-title">${cat}</h2>
                <div class="menu-grid">
                    ${items.map(item => renderMenuItem(item)).join('')}
                </div>
            </div>`;
    }).join('');
}

function renderMenuItem(item) {
    const qty = cart[item.id] || 0;
    return `
        <div class="menu-item" id="item-${item.id}">
            <div class="item-emoji">${item.emoji}</div>
            <div class="item-info">
                <div class="item-name">${item.name}</div>
                <div class="item-desc">${item.description}</div>
                <div class="item-price">₱${item.price.toFixed(2)}</div>
            </div>
            <div class="item-controls">
                <button class="qty-btn" onclick="changeQty(${item.id}, -1)" ${qty === 0 ? 'style="opacity:0.3"' : ''}>−</button>
                <span class="qty-display">${qty}</span>
                <button class="qty-btn" onclick="changeQty(${item.id}, 1)">+</button>
            </div>
        </div>`;
}

function changeQty(id, delta) {
    const current = cart[id] || 0;
    const next = Math.max(0, current + delta);
    if (next === 0) {
        delete cart[id];
    } else {
        cart[id] = next;
    }
    refreshItemDisplay(id);
    updateCartButton();
    if (document.getElementById('cartModal').classList.contains('open')) {
        renderCartItems();
    }
}

function refreshItemDisplay(id) {
    const item = menuItems.find(i => i.id === id);
    if (!item) return;
    const el = document.getElementById('item-' + id);
    if (!el) return;
    el.outerHTML = renderMenuItem(item);
}

function getCartTotal() {
    return menuItems.reduce((sum, item) => {
        return sum + (item.price * (cart[item.id] || 0));
    }, 0);
}

function getCartCount() {
    return Object.values(cart).reduce((a, b) => a + b, 0);
}

function updateCartButton() {
    const btn = document.getElementById('cartBtn');
    const count = getCartCount();
    const total = getCartTotal();
    document.getElementById('cartCount').textContent = count;
    document.getElementById('cartTotal').textContent = total.toFixed(2);
    btn.style.display = count > 0 ? 'block' : 'none';
}

function openCart() {
    renderCartItems();
    document.getElementById('cartModal').classList.add('open');
}

function closeCart() {
    document.getElementById('cartModal').classList.remove('open');
}

function closeCartOutside(e) {
    if (e.target === document.getElementById('cartModal')) closeCart();
}

function renderCartItems() {
    const body = document.getElementById('cartItems');
    const cartEntries = Object.entries(cart);

    if (cartEntries.length === 0) {
        body.innerHTML = '<p class="empty-cart">Your cart is empty.</p>';
        updateModalTotals(0);
        return;
    }

    body.innerHTML = cartEntries.map(([id, qty]) => {
        const item = menuItems.find(i => i.id === parseInt(id));
        if (!item) return '';
        return `
            <div class="cart-item">
                <span class="cart-item-emoji">${item.emoji}</span>
                <span class="cart-item-name">${item.name}</span>
                <div class="cart-item-controls">
                    <button class="qty-btn" onclick="changeQty(${item.id}, -1)">−</button>
                    <span class="qty-display">${qty}</span>
                    <button class="qty-btn" onclick="changeQty(${item.id}, 1)">+</button>
                </div>
                <span class="cart-item-price">₱${(item.price * qty).toFixed(2)}</span>
            </div>`;
    }).join('');

    updateModalTotals(getCartTotal());
}

function updateModalTotals(subtotal) {
    const tax = subtotal * 0.10;
    const total = subtotal + tax;
    document.getElementById('modalSubtotal').textContent = '₱' + subtotal.toFixed(2);
    document.getElementById('modalTax').textContent = '₱' + tax.toFixed(2);
    document.getElementById('modalTotal').textContent = '₱' + total.toFixed(2);
}

function clearCart() {
    cart = {};
    renderMenu();
    updateCartButton();
    renderCartItems();
}

async function placeOrder() {
    if (getCartCount() === 0) return;

    const tableNumber = document.getElementById('tableNumberInput').value.trim() || '—';
    const subtotal = getCartTotal();
    const tax = subtotal * 0.10;
    const total = subtotal + tax;

    const items = Object.entries(cart).map(([id, qty]) => {
        const item = menuItems.find(i => i.id === parseInt(id));
        return { ...item, quantity: qty };
    });
    const orderItems = items.map(item => ({ menuItemId: item.id, quantity: item.quantity }));

    try {
        const res = await fetch(`/api/restaurants/${encodeURIComponent(restaurantSlug)}/orders`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ tableNumber, items: orderItems })
        });
        if (!res.ok) throw new Error(await res.text());
        const placedOrder = await res.json();
        rememberOrder(placedOrder.id);
    } catch (e) {
        console.error('Failed to send order to kitchen:', e);
        alert('Unable to place the order. Please check the table and try again.');
        return;
    }

    const summaryLines = items.map(item =>
        `<div>${item.emoji} ${item.name} x${item.quantity} — ₱${(item.price * item.quantity).toFixed(2)}</div>`
    ).join('');

    document.getElementById('orderSummary').innerHTML = `
        ${summaryLines}
        <div style="margin-top:10px;padding-top:8px;border-top:1px solid #e0b0a8;font-weight:700;color:#c0392b;">
            Total: ₱${total.toFixed(2)}
        </div>`;

    closeCart();
    document.getElementById('successModal').classList.add('open');

    cart = {};
    document.getElementById('tableNumberInput').value = qrTable || '';
    renderMenu();
    updateCartButton();
}

function closeSuccess() {
    document.getElementById('successModal').classList.remove('open');
}

if (qrTable) {
    const tableInput = document.getElementById('tableNumberInput');
    tableInput.value = qrTable;
    tableInput.readOnly = true;
}
function orderIds() { try { return JSON.parse(localStorage.getItem(orderStorageKey) || '[]'); } catch { return []; } }
function rememberOrder(id) { const ids = [...new Set([...orderIds(), id])]; localStorage.setItem(orderStorageKey, JSON.stringify(ids)); refreshMyOrders(); }
async function refreshMyOrders() {
    const ids = orderIds();
    if (!ids.length) return;
    const results = await Promise.all(ids.map(id => fetch(`/api/restaurants/${encodeURIComponent(restaurantSlug)}/orders/${id}`).then(r => r.ok ? r.json() : null)));
    const orders = results.filter(Boolean);
    const container = document.getElementById('myOrders');
    container.style.display = orders.length ? 'block' : 'none';
    document.getElementById('myOrdersList').innerHTML = orders.map(order => `<div style="background:white;border:1px solid #eee;border-radius:8px;padding:12px;margin:8px 0"><strong>Order #${order.id}</strong> · <strong>${order.status}</strong><br>${order.items.map(item => `${item.emoji} ${item.name} × ${item.quantity}`).join(', ')}<br><span style="color:#c0392b">Total: ₱${Number(order.total).toFixed(2)}</span></div>`).join('');
}
refreshMyOrders();
setInterval(refreshMyOrders, 15000);
loadMenu();
