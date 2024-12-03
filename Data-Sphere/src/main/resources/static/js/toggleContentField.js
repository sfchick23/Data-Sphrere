function toggleContentField() {
    const checkbox = document.getElementById('toggleContent');
    const contentField = document.getElementById('content');
    if (checkbox.checked) {
        contentField.removeAttribute('disabled'); // Включаем поле
        contentField.setAttribute('required', 'true'); // Добавляем обязательность
    } else {
        contentField.setAttribute('disabled', 'true'); // Отключаем поле
        contentField.removeAttribute('required'); // Убираем обязательность
        contentField.value = ''; // Сбрасываем значение
    }
}