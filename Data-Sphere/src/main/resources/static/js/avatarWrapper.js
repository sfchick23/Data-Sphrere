document.addEventListener('DOMContentLoaded', () => {
    const avatar = document.querySelector('.avatar-link'); // Аватар
    const menu = document.getElementById('dropdown-menu'); // Меню

    avatar.addEventListener('click', (event) => {
        event.preventDefault(); // Останавливает переход по ссылке
        menu.classList.toggle('hidden'); // Показывает или скрывает меню
    });

    // Скрыть меню при клике вне аватара или меню
    document.addEventListener('click', (event) => {
        if (!menu.contains(event.target) && !avatar.contains(event.target)) {
            menu.classList.add('hidden');
        }
    });
});
