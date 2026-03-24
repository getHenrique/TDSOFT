const fs = require('fs');
const path = require('path');

/*
* Pipeline Funcional ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
*/

// Extrai o nome do repositório a partir do nome do arquivo
function getRepoName(filePath) {
  return path.basename(filePath, '.json');
}

// Lê o arquivo JSON e retorna os dados como um array de objetos
function readFile(filePath) {
  const data = fs.readFileSync(filePath, 'utf8');
  return JSON.parse(data);
}

// Lê o arquivo JSON (incluindo seu nome) e normaliza os dados para o formato desejado
function normalizeData(filePath)
{

  return readFile(filePath).flatMap(user =>
    user.events.map(event => ({
      repo: getRepoName(filePath),
      user: user.login,
      event_type: event.type,
      event_date: event.date
    }))
  );

}

/*
* Implementação das Análises ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
*/

// 1. Contagem de eventos por tipo
function countEventTypes(events) {
  return events.reduce((accumulator, event) => ({
    ...accumulator,
    [event.event_type]: (accumulator[event.event_type] || 0) + 1
  }), {});
}

/*
* Input e Output ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
*/

// Lista de arquivos
const files = ['beef.json', 'easylist.json', 'gentoo.json'];

// Extrai todos os eventos de todos os arquivos e normaliza os dados
const allEvents = files.flatMap(file => normalizeData(path.join(__dirname, file)));

// Exportar processamento do pipeline para arquivo JSON
//fs.writeFileSync('output.json', JSON.stringify(allEvents, null, 2));

// Contar eventos por tipo
const eventCounts = countEventTypes(allEvents);
const eventCountsSorted = countEventTypesSorted(allEvents);

// Exibir o resultado
console.log('=== Todos os Eventos ===');
console.log(JSON.stringify(allEvents, null, 2));

console.log('\n=== Contagem por Tipo de Evento ===');
console.log(JSON.stringify(eventCounts, null, 2));

console.log('\n=== Contagem por Tipo (Ordenado) ===');
console.log(JSON.stringify(eventCountsSorted, null, 2));

fs.writeFileSync('event-counts.json', JSON.stringify(eventCountsSorted, null, 2));