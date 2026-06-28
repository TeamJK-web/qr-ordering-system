let orders = {};

async function loadExistingOrders() {
    try {
        const res = await fetch('/api/orders');
        const list = await res.json();
        list.forEach(order => addOrderCard(order));
        updateEmptyState();
    } catch (e) {
        console.error('Failed to load orders:', e);
    }
}

function connectStream() {
    const es = new EventSource('/api/orders/stream');
    const statusEl = document.getElementById('status');

    es.onopen = () => {
        statusEl.className = 'status';
        statusEl.innerHTML = '<span class="dot"></span> Live';
    };

    es.addEventListener('new-order', e => {
        const order = JSON.parse(e.data);
        addOrderCard(order);
        updateEmptyState();
        playAlert();
    });

    es.addEventListener('order-done', e => {
        const id = JSON.parse(e.data);
        removeOrderCard(id);
        updateEmptyState();
    });

    es.onerror = () => {
        statusEl.className = 'status offline';
        statusEl.innerHTML = '<span class="dot"></span> Reconnecting...';
        es.close();
        setTimeout(connectStream, 3000);
    };
}

function addOrderCard(order) {
    if (orders[order.id]) return;
    orders[order.id] = order;

    const grid = document.getElementById('ordersGrid');
    const card = document.createElement('div');
    card.className = 'order-card';
    card.id = 'order-' + order.id;

    const itemRows = order.items.map(item => `
        <div class="order-item-row">
            <span class="item-emoji">${item.emoji}</span>
            <span class="item-name">${item.name}</span>
            <span class="item-qty">x${item.quantity}</span>
        </div>`).join('');

    card.innerHTML = `
        <div class="card-header">
            <div>
                <div class="order-number">Order #${order.id}</div>
                <div class="order-time">${order.placedAt}</div>
            </div>
            <div class="table-badge">${order.tableNumber}</div>
        </div>
        <div class="card-body">${itemRows}</div>
        <div class="card-footer">
            <div class="order-total">Total: <strong>₱${order.total.toFixed(2)}</strong></div>
            <button class="done-btn" onclick="markDone(${order.id})">✓ Done</button>
        </div>`;

    grid.prepend(card);
}

function removeOrderCard(id) {
    delete orders[id];
    const card = document.getElementById('order-' + id);
    if (!card) return;
    card.classList.add('done');
    setTimeout(() => card.remove(), 500);
}

async function markDone(id) {
    try {
        await fetch('/api/orders/' + id + '/done', { method: 'PATCH' });
        removeOrderCard(id);
        updateEmptyState();
    } catch (e) {
        console.error('Failed to mark order done:', e);
    }
}

function updateEmptyState() {
    const hasOrders = Object.keys(orders).length > 0;
    document.getElementById('emptyState').style.display = hasOrders ? 'none' : 'block';
    document.getElementById('ordersGrid').style.display = hasOrders ? 'grid' : 'none';
}

function playAlert() {
    try {
        const ctx = new AudioContext();
        const osc = ctx.createOscillator();
        const gain = ctx.createGain();
        osc.connect(gain);
        gain.connect(ctx.destination);
        osc.frequency.value = 880;
        gain.gain.setValueAtTime(0.3, ctx.currentTime);
        gain.gain.exponentialRampToValueAtTime(0.001, ctx.currentTime + 0.4);
        osc.start(ctx.currentTime);
        osc.stop(ctx.currentTime + 0.4);
    } catch (e) {}
}

loadExistingOrders();
connectStream();
