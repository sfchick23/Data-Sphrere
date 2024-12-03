document.addEventListener('DOMContentLoaded', () => {
    const themeToggleButton = document.getElementById('theme-toggle');
    const currentTheme = localStorage.getItem('theme'); // Проверяем сохраненную тему

    // Устанавливаем тему при загрузке страницы
    if (currentTheme) {
        document.body.classList.add(currentTheme);
    } else {
        document.body.classList.add('dark-theme'); // По умолчанию светлая тема
    }

    // Обработчик смены темы
    themeToggleButton.addEventListener('click', () => {
        if (document.body.classList.contains('light-theme')) {
            document.body.classList.remove('light-theme');
            document.body.classList.add('dark-theme');
            localStorage.setItem('theme', 'dark-theme'); // Сохраняем тему
        } else {
            document.body.classList.remove('dark-theme');
            document.body.classList.add('light-theme');
            localStorage.setItem('theme', 'light-theme'); // Сохраняем тему
        }
    });
});
