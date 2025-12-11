//  Get restaurantId from URL or localStorage
let restaurantId =
    new URLSearchParams(window.location.search).get("restaurantId") ||
    localStorage.getItem("restaurantId");

const base = "http://localhost:8080";

//  Get token
const token = localStorage.getItem("token");

//  Protect page- redirect if not logged in
if (!token) {
    window.location.href = "login.html";
}

// Helper for authenticated fetch
function authFetch(url, options = {}) {
    return fetch(url, {
        ...options,
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`,
            ...(options.headers || {})
        }
    });
}

document.addEventListener("DOMContentLoaded", () => {
    if (!restaurantId) {
        alert("Missing restaurant ID");
        window.location.href = "login.html";
        return;
    }

    loadProfile();
    loadToday();
    loadAll();
//    loadEarliest();
});

// --------------------- THEME TOGGLE ------------------------
function toggleTheme() {
    document.body.classList.toggle("dark");
}

// --------------------- SWITCH SECTION ------------------------
function showSection(id) {
    document.querySelectorAll(".content > div").forEach(s => s.classList.add("hidden"));
    document.getElementById(id).classList.remove("hidden");
}

// --------------------- TODAY'S RESERVATIONS ------------------------
async function loadToday() {
    const today = new Date().toISOString().split("T")[0];

    const res = await authFetch(`${base}/reservations/restaurant/${restaurantId}`);
    const data = await res.json();

    const filtered = data.filter(r => r.reservationDate === today);

    fillTable("todayTable", filtered);
}

// --------------------- ALL RESERVATIONS ------------------------
async function loadAll() {
    const res = await authFetch(`${base}/reservations/restaurant/${restaurantId}`);
    const data = await res.json();

    fillTable("allTable", data);
}

// --------------------- FILL TABLE ------------------------
function fillTable(tableId, rows) {
    let html = `
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Phone</th>
            <th>Date</th>
            <th>Start</th>
            <th>End</th>
            <th>Party</th>
            <th>Status</th>
            <th>Cancel</th>
        </tr>
    `;

    rows.forEach(r => {
        html += `
            <tr>
                <td>${r.id}</td>
                <td>${r.customerName}</td>
                <td>${r.customerPhone}</td>
                <td>${r.reservationDate}</td>
                <td>${r.startTime}</td>
                <td>${r.endTime || "-"}</td>
                <td>${r.partySize}</td>
                <td>${r.status}</td>
                <td>
                    <button onclick="cancelRes(${r.id})">X</button>
                </td>
            </tr>
        `;
    });

    document.getElementById(tableId).innerHTML = html;
}

// --------------------- CANCEL RESERVATION ------------------------
async function cancelRes(id) {
    await authFetch(`${base}/reservations/${id}/cancel`, {
        method: "PATCH"
    });

    loadToday();
    loadAll();
}

// --------------------- PROFILE ------------------------
async function loadProfile() {
    const res = await authFetch(`${base}/restaurants/${restaurantId}`);
    const r = await res.json();

    document.getElementById("restaurantName").innerText = r.name;

    document.getElementById("profileForm").innerHTML = `
        Name: <input id="pName" value="${r.name}"><br><br>
        Address: <input id="pAddress" value="${r.address}"><br><br>
        Opening Time: <input type="time" id="pOpen" value="${r.openingTime}"><br><br>
        Closing Time: <input type="time" id="pClose" value="${r.closingTime}"><br><br>
        Total Tables: <input id="pTables" type="number" value="${r.totalTables}"><br><br>
        Max Party Size: <input id="pMax" type="number" value="${r.maxPartySize}"><br><br>
    `;
}

async function updateProfile() {
    await authFetch(`${base}/restaurants/${restaurantId}`, {
        method: "PUT",
        body: JSON.stringify({
            name: document.getElementById("pName").value,
            address: document.getElementById("pAddress").value,
            openingTime: document.getElementById("pOpen").value,
            closingTime: document.getElementById("pClose").value,
            totalTables: document.getElementById("pTables").value,
            maxPartySize: document.getElementById("pMax").value
        })
    });

    alert("Profile updated!");
    loadProfile();
}

//was analytics code here



// --------------------- ADD RESERVATION ------------------------
async function createReservation() {
    const body = {
        customerName: document.getElementById("addName").value,
        customerPhone: document.getElementById("addPhone").value,
        reservationDate: document.getElementById("addDate").value,
        startTime: document.getElementById("addTime").value,
        partySize: parseInt(document.getElementById("addParty").value),
        restaurant: { id: parseInt(restaurantId) }
    };

    const res = await authFetch(`${base}/reservations`, {
        method: "POST",
        body: JSON.stringify(body)
    });

    if (res.ok) {
        alert("Reservation created!");
        loadToday();
        loadAll();
    } else {
        alert("Error creating reservation");
    }
}

// --------------------- LOGOUT ------------------------
function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("restaurantId");
    window.location.href = "login.html";
}
// --------------------- AVAILABLE SLOTS -----------------------------------
async function loadSlots() {
    try {
        const date = document.getElementById("slotDate").value;
        if (!date) { alert("Select a date"); return; }

        const res = await authFetch(`${base}/reservations/available-slots?restaurantId=${restaurantId}&date=${date}`);
        if (!res.ok) {
            handleAuthError(res);
            return;
        }
        const slots = await res.json();
        const container = document.getElementById("slotsContainer");
        container.innerHTML = "";

        if (slots.length === 0) {
            container.innerText = "No slots available.";
            return;
        }

        slots.forEach(time => {
            const div = document.createElement("div");
            div.className = "slot";
            div.innerText = time;
            // click-to-fill add section time input
            div.onclick = () => {
                document.getElementById("addDate").value = date;
                document.getElementById("addTime").value = time;
                showSection("addSection");
            };
            container.appendChild(div);
        });
    } catch (e) {
        console.error(e);
    }
}

// --------------------- AUTH ERROR HANDLER --------------------------------
async function handleAuthError(response) {
    if (response.status === 401 || response.status === 403) {
        // token invalid or expired
        alert("Authentication expired or not valid. Please login again.");
        logout();
    } else {
        console.error("Request failed:", response.statusText);
    }
}