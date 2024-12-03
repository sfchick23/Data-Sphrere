function previewImage(event) {
    const file = event.target.files ? event.target.files[0] : event.file;

    if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
            const img = document.getElementById("image-preview");
            const deleteBtn = document.getElementById("delete-file-btn");
            const uploadText = document.querySelector(".upload-text");

            // Отображаем превью
            img.src = e.target.result;
            img.classList.remove("hidden");

            // Убираем текст загрузки
            uploadText.style.display = "none";

            // Показываем кнопку "Удалить файл"
            deleteBtn.classList.remove("hidden");
        };

        reader.readAsDataURL(file);
    }
}

// Добавление drag-and-drop
const wrapper = document.querySelector(".image-wrapper");

wrapper.addEventListener("dragover", (e) => {
    e.preventDefault();
    wrapper.style.borderColor = "#4a90e2";
});

wrapper.addEventListener("dragleave", () => {
    wrapper.style.borderColor = "#ccc";
});

wrapper.addEventListener("drop", (e) => {
    e.preventDefault();
    wrapper.style.borderColor = "#ccc";

    const file = e.dataTransfer.files[0];
    if (file) {
        const input = document.getElementById("file");
        input.files = e.dataTransfer.files;
        previewImage({ target: { files: [file] } });
    }
});

// Удаление файла
document.getElementById("delete-file-btn").addEventListener("click", () => {
    const input = document.getElementById("file");
    const img = document.getElementById("image-preview");
    const deleteBtn = document.getElementById("delete-file-btn");
    const uploadText = document.querySelector(".upload-text");

    // Сбрасываем файл
    input.value = "";

    // Скрываем превью
    img.src = "";
    img.classList.add("hidden");

    // Прячем кнопку удаления
    deleteBtn.classList.add("hidden");

    // Показываем текст
    uploadText.style.display = "flex";
});
