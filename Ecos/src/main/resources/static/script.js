const CAMPI_IFPB = [
  'Areia', 'Cabedelo', 'Cajazeiras', 'Campina Grande', 'Catolé do Rocha',
  'Esperança', 'Guarabira', 'Itabaiana', 'Itaporanga', 'João Pessoa',
  'Mangabeira', 'Monteiro', 'Patos', 'Pedras de Fogo', 'Picuí',
  'Princesa Isabel', 'Santa Luzia', 'Santa Rita', 'Soledade', 'Sousa'
];

localStorage.removeItem('ecos_usuarios_demo');
localStorage.removeItem('ecos_registros_demo');

let usuarioAtual = JSON.parse(sessionStorage.getItem('ecos_usuario_atual') || 'null');
let registrosCache = [];

const porId = id => document.getElementById(id);

function escapar(valor) {
  return String(valor ?? '').replace(/[&<>"']/g, caractere => ({
    '&': '&amp;',
    '<': '&lt;',
    '>': '&gt;',
    '"': '&quot;',
    "'": "\'"
  }[caractere]));
}


function dataBR(valor) {
  if (!valor) return 'Não informada';
  return new Date(valor).toLocaleDateString('pt-BR');
}

function preencherCampi() {
  const campos = ['cadastro-campus', 'denuncia-campus', 'atendimento-campus', 'filtro-campus'];
  campos.forEach(id => {
    const select = porId(id);
    if (!select) return;

    const primeiraOpcao = select.options.item(0)?.outerHTML || '<option value="">Selecione</option>';
    const opcoes = CAMPI_IFPB.map(campus => `
      <option value="${escapar(campus)}">Campus ${escapar(campus)}</option>
    `).join('');

    select.innerHTML = primeiraOpcao + opcoes;
    if (id === 'filtro-campus') {
      select.options.item(0).textContent = 'Todos os campi';
    }
  });
}

function navegarPara(idTela) {
  const telasPublicas = ['tela-login', 'tela-cadastro'];
  if (!telasPublicas.includes(idTela) && !usuarioAtual) {
    mostrarMensagem('login-mensagem', 'Entre com sua conta para acessar o sistema.', 'erro');
    idTela = 'tela-login';
  }

  document.querySelectorAll('.tela').forEach(tela => {
    tela.classList.toggle('active', tela.id === idTela);
  });

  const nav = porId('main-nav');
  if (nav) {
    nav.classList.toggle('hidden', !usuarioAtual || telasPublicas.includes(idTela));
  }

   if (idTela === 'tela-home' && usuarioAtual) {
     porId('nome-usuario').textContent = usuarioAtual.nome.split(' ')[0];
   }

  if (idTela === 'tela-listagem') {
    atualizarLista();
  }

  window.scrollTo({ top: 0, behavior: 'smooth' });
}

function mostrarMensagem(id, texto, tipo = 'sucesso') {
  const elemento = porId(id);
  if (!elemento) return;
  elemento.textContent = texto;
  elemento.className = `form-message ${tipo}`;
}

async function criarConta(evento) {
  evento.preventDefault();
  const senha = porId('cadastro-senha').value;
  const confirmar = porId('cadastro-confirmar').value;

  if (senha !== confirmar) {
    mostrarMensagem('cadastro-mensagem', 'As senhas informadas não coincidem.', 'erro');
    return;
  }

  const payload = {
    nome: porId('cadastro-nome').value.trim(),
    matricula: porId('cadastro-matricula').value.trim(),
    campus: porId('cadastro-campus').value,
    perfil: porId('cadastro-perfil').value,
    senha: senha
  };

  try {
    const resp = await fetch('/api/auth/cadastro', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });
    const dados = await resp.json();

    if (!resp.ok) {
      mostrarMensagem('cadastro-mensagem', dados.erro || 'Erro ao criar conta.', 'erro');
      return;
    }

    evento.target.reset();
    mostrarMensagem('login-mensagem', 'Sua conta foi criada. Entre com sua matrícula e senha.', 'sucesso');
    navegarPara('tela-login');
  } catch {
    mostrarMensagem('cadastro-mensagem', 'Não foi possível conectar ao servidor Java.', 'erro');
  }
}

async function realizarLogin(evento) {
  evento.preventDefault();
  const payload = {
    matricula: porId('usuario').value.trim(),
    senha: porId('senha').value
  };

  try {
    const resp = await fetch('/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });
    const dados = await resp.json();

    if (!resp.ok) {
      mostrarMensagem('login-mensagem', dados.erro || 'Matrícula ou senha inválidos.', 'erro');
      return;
    }

    usuarioAtual = dados;
    sessionStorage.setItem('ecos_usuario_atual', JSON.stringify(usuarioAtual));
    porId('login-form').reset();
    mostrarMensagem('login-mensagem', '', 'sucesso');
    navegarPara('tela-home');
  } catch {
    mostrarMensagem('login-mensagem', 'Servidor Java indisponível. Verifique o IntelliJ.', 'erro');
  }
}

function sair() {
  usuarioAtual = null;
  registrosCache = [];
  sessionStorage.removeItem('ecos_usuario_atual');
  navegarPara('tela-login');
  mostrarMensagem('login-mensagem', 'Você saiu da sua conta.', 'sucesso');
}
async function salvarDenuncia(evento) {
  evento.preventDefault();
  const payload = {
    matricula: usuarioAtual.matricula,
    dataOcorrido: porId('denuncia-data').value,
    categoria: porId('denuncia-tipo').value,
    campus: porId('denuncia-campus').value,
    local: porId('denuncia-local').value.trim(),
    descricao: porId('denuncia-desc').value.trim(),
    adicionais: porId('denuncia-pessoal').value.trim()
  };

  try {
    const resp = await fetch('/api/registros/denuncia', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });
    const dados = await resp.json();

    if (!resp.ok) {
      mostrarMensagem('denuncia-mensagem', dados.erro || 'Erro ao enviar denúncia.', 'erro');
      return;
    }

    evento.target.reset();
    mostrarMensagem('denuncia-mensagem', 'Sua denúncia foi enviada. Agradecemos por compartilhar o ocorrido.', 'sucesso');
  } catch {
    mostrarMensagem('denuncia-mensagem', 'Erro de conexão com o servidor Java.', 'erro');
  }
}

async function salvarAtendimento(evento) {
  evento.preventDefault();
  const payload = {
    matricula: usuarioAtual.matricula,
    campus: porId('atendimento-campus').value,
    atendimento: porId('atendimento-perfil').value,
    dataPretendida: porId('atendimento-data').value,
    solicitante: porId('atendimento-aluno').value.trim(),
    descricao: porId('atendimento-solic').value.trim()
  };

  try {
    const resp = await fetch('/api/registros/atendimento', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });
    const dados = await resp.json();

    if (!resp.ok) {
      mostrarMensagem('atendimento-mensagem', dados.erro || 'Erro ao enviar solicitação.', 'erro');
      return;
    }

    evento.target.reset();
    mostrarMensagem('atendimento-mensagem', 'Sua solicitação foi enviada. A equipe responsável poderá dar continuidade ao atendimento.', 'sucesso');
  } catch {
    mostrarMensagem('atendimento-mensagem', 'Erro de conexão com o servidor Java.', 'erro');
  }
}

async function atualizarLista() {
  if (!usuarioAtual) return;

  try {
    const resp = await fetch(`/api/registros/${encodeURIComponent(usuarioAtual.matricula)}`);
    registrosCache = resp.ok ? await resp.json() : [];
  } catch {
    registrosCache = [];
  }

  const termo = (porId('busca-input')?.value || '').toLocaleLowerCase('pt-BR');
  const campus = porId('filtro-campus')?.value || '';
  const situacao = porId('filtro-situacao')?.value || '';

  const filtrados = registrosCache.filter(registro => {
    const texto = [
      registro.tipo,
      registro.campus,
      registro.categoria,
      registro.atendimento,
      registro.descricao
    ].join(' ').toLocaleLowerCase('pt-BR');

    return (
      texto.includes(termo) &&
      (!campus || registro.campus === campus) &&
      (!situacao || registro.situacao === situacao)
    );
  });

  porId('lista-vazia').hidden = filtrados.length > 0;
  porId('tabela-wrap').hidden = filtrados.length === 0;

  porId('tabela-corpo').innerHTML = filtrados.map(registro => `
    <tr>
      <td>${escapar(registro.tipo)}</td>
      <td>Campus ${escapar(registro.campus)}</td>
      <td>${dataBR(registro.dataEnvio)}</td>
      <td><span class="status-pill">${escapar(registro.situacao)}</span></td>
      <td>
        <button class="table-action" type="button" onclick="abrirDetalhes('${escapar(registro.id)}')">
          Ver detalhes
        </button>
      </td>
    </tr>
  `).join('');

  if (registrosCache.length && !filtrados.length) {
    porId('lista-vazia').querySelector('p').textContent = 'Nenhum registro corresponde aos filtros selecionados.';
  } else {
    porId('lista-vazia').querySelector('p').textContent = 'Quando você enviar uma denúncia ou solicitação, ela aparecerá nesta área.';
  }
}

function filtrarTabela() {
  atualizarLista();
}

function abrirDetalhes(id) {
  const registro = registrosCache.find(item => item.id === id);
  if (!registro) return;

  let campos = [];
  if (registro.tipo === 'Denúncia') {
    campos = [
      ['Tipo de ocorrência', registro.categoria],
      ['Data do ocorrido', dataBR(registro.dataOcorrido)],
      ['Campus', `Campus ${registro.campus}`],
      ['Local', registro.local],
      ['Descrição', registro.descricao],
      ['Informações adicionais', registro.adicionais || 'Não informado']
    ];
  } else {
    campos = [
      ['Atendimento solicitado', registro.atendimento],
      ['Campus', `Campus ${registro.campus}`],
      ['Data pretendida', registro.dataPretendida ? new Date(registro.dataPretendida).toLocaleString('pt-BR') : 'Não informada'],
      ['Solicitante', registro.solicitante],
      ['Descrição', registro.descricao]
    ];
  }

  porId('detalhe-conteudo').innerHTML = `
    <div class="detail-top">
      <span class="status-pill">${escapar(registro.situacao)}</span>
      <span class="muted">Enviado em ${dataBR(registro.dataEnvio)}</span>
    </div>
    <h3>${escapar(registro.tipo)}</h3>
    <div class="detail-grid">
      ${campos.map(([rotulo, valor]) => `
        <div class="detail-field">
          <span>${escapar(rotulo)}</span>
          <p>${escapar(valor)}</p>
        </div>
      `).join('')}
    </div>
    <div class="actions">
      <button class="btn-secondary" onclick="navegarPara('tela-listagem')">Voltar ao acompanhamento</button>
    </div>
  `;

  navegarPara('tela-detalhes');
}

document.addEventListener('DOMContentLoaded', () => {
  preencherCampi();
  if (usuarioAtual) {
    navegarPara('tela-home');
  }
});
