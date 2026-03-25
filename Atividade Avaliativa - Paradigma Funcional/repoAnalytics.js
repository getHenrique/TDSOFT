const fileSystem = require('fs');
const path = require('path');

/*
* Pipeline Funcional ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
*/

// Lê o arquivo JSON e retorna os dados como um array de objetos
function readFile(filePath) {
  // Exportar array da leitura em JSON (para debug)
  // fileSystem.writeFileSync('input.json', JSON.stringify(JSON.parse(fileSystem.readFileSync(filePath, 'utf8')), null, 2));
  return JSON.parse(fileSystem.readFileSync(filePath, 'utf8'));
}

// Lê o arquivo JSON e normaliza os dados em um array unidimensional de objetos
function normalizeData(filePath) {

  return readFile(filePath).flatMap(
      user => user.events.map(
      event => ({
      repo: path.basename(filePath, '.json'),// Extrai o nome do repositório a partir do nome do arquivo
      username: user.login,
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
  return events.reduce(
    (accumulator, event) => ({
      ...accumulator,
      [event.event_type]: (accumulator[event.event_type] || 0) + 1
    }),
  {});
}

// 2. Ranking de Usuários
function rankTopUsers(events) {
  return Object.entries(
    events.reduce(
      (accumulator, event) => ({
        ...accumulator,
        [event.username]: (accumulator[event.username] || 0) + 1
      }),
    {})
  )
    .map(([username, eventCount]) => ({ username, eventCount }))
    .sort((a, b) => b.eventCount - a.eventCount)
    .slice(0, 10);
}

// 3. Análise por Repositório

// Total de Eventos por Repositório
function eventCount(filePath){
  return {
    repo: path.basename(filePath, '.json'),
    eventCount: readFile(filePath).length
  };
}

// Número de Usuários Únicos
function countUniqueUsers(events) {
  return Object.keys(
    events.reduce(
      (accumulator, event) => ({
        ...accumulator,
        [event.username]: true
      }), {})
  ).length;
}

// Diversidade de Tipos de Evento
function countUniqueEvents(events) {
  return Object.keys(
    events.reduce(
      (accumulator, event) => ({
        ...accumulator,
        [event.event_type]: true
      }), {})
  ).length;
}

/*
* Input e Output ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
*/

// Array de arquivos
const files = ['beef.json', 'easylist.json', 'gentoo.json'];

// Para cada arquivo da lista: extrai e normaliza os dados em um array unidimensional de objetos
const allEvents = files.flatMap(file => normalizeData(path.join(__dirname, file)));

// Exportar processamento do pipeline para arquivo JSON (para debug)
// fileSystem.writeFileSync('output.json', JSON.stringify(allEvents, null, 2));

// Contar eventos por tipo
const eventCounts = countEventTypes(allEvents);
console.log('\n=== Contagem por Tipo de Evento ===');
console.log(JSON.stringify(eventCounts, null, 2));

// Ranking dos top 10 usuários com mais eventos
const topUsers = rankTopUsers(allEvents);
console.log('\n=== Top 10 Usuários com Mais Eventos ===');
console.log(JSON.stringify(topUsers, null, 2));

// Total de eventos por repositório
const totalEvents = files.flatMap(file => eventCount(path.join(__dirname, file)));
console.log('\n=== Total de eventos por repositório ===');
console.log(JSON.stringify(totalEvents, null, 2));

// Número de usuários únicos
const uniqueUserCount = countUniqueUsers(allEvents);
console.log('\n=== Contagem de Usuários Únicos ===');
console.log(uniqueUserCount);

// Diversidade em eventos
const uniqueEventCount = countUniqueEvents(allEvents);
console.log('\n=== Diversidade de Eventos (Número de Diferentes Eventos) ===');
console.log(uniqueEventCount);