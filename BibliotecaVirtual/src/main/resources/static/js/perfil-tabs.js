document.addEventListener("DOMContentLoaded", () => {
  const buttons = document.querySelectorAll(".tab-button");
  const contents = document.querySelectorAll(".tab-content");

  buttons.forEach(btn => {
    btn.addEventListener("click", function (e) {
      buttons.forEach(b => b.classList.remove("active"));
      this.classList.add("active");

      contents.forEach(c => {
        if (c.id === this.textContent.trim().toLowerCase()) {
          c.style.display = "block";
          c.classList.add("fade-in");
          setTimeout(() => c.classList.remove("fade-in"), 300);
        } else {
          c.style.display = "none";
        }
      });
    });
  });
});