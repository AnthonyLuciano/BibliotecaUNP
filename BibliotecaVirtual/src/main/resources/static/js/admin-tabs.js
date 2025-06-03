function openTab(tabId) {
  const contents = document.querySelectorAll('.tab-content');
  const buttons = document.querySelectorAll('.tab-button');

  contents.forEach(c => c.style.display = 'none');
  buttons.forEach(b => b.classList.remove('active'));

  document.getElementById(tabId).style.display = 'block';
  // Corrigido: encontrar o botão correto e ativar
  buttons.forEach(b => {
    if (b.textContent.includes(tabId.charAt(0).toUpperCase() + tabId.slice(1))) {
      b.classList.add('active');
    }
  });
}

// Ativar aba correta ao carregar, se houver parâmetro na URL
document.addEventListener("DOMContentLoaded", function() {
  const params = new URLSearchParams(window.location.search);
  const aba = params.get('aba');
  if (aba) {
    openTab(aba);
  }

  // Adiciona o listener ao formulário de pesquisa da aba Relatórios
  const relatorioForm = document.querySelector('#relatorios form.search-bar');
  if (relatorioForm) {
    relatorioForm.addEventListener('submit', function(event) {
      event.preventDefault();
      const url = new URL(relatorioForm.action, window.location.origin);
      url.searchParams.set('busca', relatorioForm.busca.value);
      url.searchParams.set('aba', 'relatorios');
      window.location.href = url.toString();
    });
  }
});
