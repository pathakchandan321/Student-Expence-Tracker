document.querySelectorAll('.delete-form').forEach(function (form) {
    form.addEventListener('submit', function (event) {
        if (!confirm('Delete this expense?')) event.preventDefault();
    });
});

var spendInput = document.getElementById('spendInput');
if (spendInput) {
    spendInput.addEventListener('input', function () {
        var amount = Number(spendInput.value) || 0;
        document.getElementById('afterSpend').textContent = '₹' + (window.remainingBalance - amount).toFixed(2);
    });
}
