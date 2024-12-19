function showConfirmModal(deleteUrl) {
    const modal = document.getElementById('confirmModal');
    const confirmBtn = document.getElementById('confirmBtn');
    modal.style.display = 'flex';

    confirmBtn.onclick = function () {
        window.location.href = deleteUrl; // Przekierowanie na URL usuwania rezerwacji
        closeModal();
    };
}

function closeModal() {
    const modal = document.getElementById('confirmModal');
    modal.style.display = 'none';
}
