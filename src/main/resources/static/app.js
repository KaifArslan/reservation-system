const backend = "http://localhost:8080";  // your Spring Boot server

// Extract restaurantId from URL
const urlParams = new URLSearchParams(window.location.search);
const restaurantId = urlParams.get("restaurantId");

function loadSlots() {
    const date = document.getElementById("reservationDate").value;
    if (!date) {
        alert("Select a date first");
        return;
    }

    fetch(`${backend}/reservations/available-slots?restaurantId=${restaurantId}&date=${date}`)
        .then(res => res.json())
        .then(times => {
            const slotContainer = document.getElementById("slots");
            slotContainer.innerHTML = "";

            times.forEach(time => {
                const btn = document.createElement("button");
                btn.innerText = time;
                btn.onclick = () => selectSlot(time);
                slotContainer.appendChild(btn);
            });
        });
}

let selectedTime = null;

function selectSlot(time) {
    selectedTime = time;
    document.getElementById("form-section").style.display = "block";
}

function submitReservation() {
    const name = document.getElementById("name").value;
    const phone = document.getElementById("phone").value;
    const partySize = document.getElementById("partySize").value;
    const date = document.getElementById("reservationDate").value;

    if (!selectedTime) {
        alert("Select a time first");
        return;
    }

    const body = {
        customerName: name,
        customerPhone: phone,
        startTime: selectedTime,
        endTime: null,  // backend sets automatically
        partySize: partySize,
        reservationDate: date,
        restaurant: { id: restaurantId }
    };

    fetch(`${backend}/reservations`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
    })
        .then(res => res.json())
        .then(data => {
            document.getElementById("step-form").style.display = "none";
            document.getElementById("confirm-section").style.display = "block";

            document.getElementById("confirmMessage").innerHTML =
                `Reservation ID: ${data.id}<br>
                 Name: ${data.customerName}<br>
                 Time: ${data.startTime}<br>
                 Date: ${data.reservationDate}`;
        });
}
