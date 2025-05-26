document.addEventListener("DOMContentLoaded", () => {
    const links = document.querySelectorAll(".nav-link");
    const sections = document.querySelectorAll(".section");

    function showSection(sectionId) {
        sections.forEach(section => {
            if (section.id === sectionId) {
                section.classList.add("active");
                section.classList.remove("fade-out");
            } else {
                section.classList.remove("active");
                section.classList.add("fade-out");
            }
        });
    }

    links.forEach(link => {
        link.addEventListener("click", e => {
            e.preventDefault();
            const target = link.getAttribute("data-target");
            showSection(target);
        });
    });

    // NOVO: Mostrar a seção baseada no atributo 'view' do backend, se existir
    let view = null;
    try {
        view = /*[[${view}]]*/ null;
    } catch (e) {
        view = null;
    }
    if (view && document.getElementById(view)) {
        showSection(view);
    } else if (sections.length > 0) {
        showSection(sections[0].id);
    }
});