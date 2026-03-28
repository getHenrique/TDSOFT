/* Meu Repositório: https://github.com/getHenrique/TDSOFT/tree/master/Atividade%20Avaliativa%20-%20Paradigma%20Funcional */

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
      period: event.date.slice(0, 7),
    }))
  );

}

/*
* Implementação das Análises ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
*/

// Recebe um array de eventos e um string critério. Retorna um objeto com a contagem da ocorrência de cada critéio no evento.
function countEventsBy(events, criteria) {
  return events.sort((a, b) => b[criteria] - a[criteria]).reduce(
    (accumulator, event) => ({
      ...accumulator,
      [event[criteria]]: (accumulator[event[criteria]] || 0) + 1
    }),
  {});
}

// Recebe um array de eventos e retorna 
function rankEventsBy(events, criteria, top, order = 'desc') {
  return Object.entries(countEventsBy(events, criteria))
    .map(([value, eventCount]) => ({ [criteria]: value, eventCount }))
    .sort((a, b) =>
      order === 'desc'
        ? b.eventCount - a.eventCount
        : a.eventCount - b.eventCount
    )
    .slice(0, top);
}

// Recebe um array de eventos e um critério. Retorna um array dos critérios filtrados por valor único.
function filterUniqueOf(events, criteria) {
  return events
    .map(event => event[criteria])
    .filter((value, index, self) => self.indexOf(value) === index);
}

// Recebe um array de eventos e retorna um objeto agrupando eventos por mês e ano
function groupEventsBy(events, criteria) {
  return events.sort((a, b) => b[criteria] - a[criteria]).reduce(
    (accumulator, event) => ({
      ...accumulator,
      [event[criteria]]: [...(accumulator[event[criteria]] || []), event]
    }),
    {}
  );
}

/*
* Input e Output ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
*/

// Array de arquivos
const files = ['beef.json', 'easylist.json', 'gentoo.json'];

// Para cada arquivo da lista: extrai e normaliza os dados em um array unidimensional de objetos
const allEvents = files.flatMap(file => normalizeData(path.join(__dirname, file)));

// Exportar processamento do pipeline para arquivo JSON (para debug)
fileSystem.writeFileSync('output.json', JSON.stringify(allEvents, null, 2));

// Contar eventos por tipo
const eventCounts = countEventsBy(allEvents, 'event_type');
console.log('\n=== Contagem por Tipo de Evento A===');
console.log(JSON.stringify(eventCounts, null, 2));

// Ranking dos top 10 usuários com mais eventos
const topUsers = rankEventsBy(allEvents, 'username', 10, 'desc');
console.log('\n=== Top 10 Usuários com Mais Eventos ===');
console.log(JSON.stringify(topUsers, null, 2));

// Total de eventos por repositório
const totalEvents = countEventsBy(allEvents, 'repo');
console.log('\n=== Total de eventos por repositório ===');
console.log(JSON.stringify(totalEvents, null, 2));

// Número de usuários únicos
const uniqueUserCount = countEventsBy(filterUniqueOf(allEvents, 'username'), 'username');
console.log('\n=== Contagem de Usuários Únicos ===');
console.log(uniqueUserCount);

// Diversidade em eventos
const uniqueEventCount = countEventsBy(filterUniqueOf(allEvents, 'event_type'), 'event_type');
console.log('\n=== Diversidade de Eventos (Número de Diferentes Tipos de Eventos) ===');
console.log(uniqueEventCount);

// Eventos agrupados por período
const eventsByPeriod = groupEventsBy(allEvents, 'period');
fileSystem.writeFileSync('groupedByPeriod.json', JSON.stringify(eventsByPeriod, null, 2));
console.log("\n=== Eventos agrupados por período guardados em \'groupedByPeriod.json\' ===")

// Usuários categorizados
const mediumUsers = rankEventsBy(allEvents, 'username', allEvents.length, 'desc')
  .slice(10, 10 + allEvents.length / 2);
const leastUsers = rankEventsBy(allEvents, 'username', allEvents.length/2, 'asc');
const groupedUsers = 
{
  'topUsers' : topUsers,// Top 10 Users
  'mediumUsers' : mediumUsers,// Upper half users that are not on top 10
  'leastUsers' : leastUsers// Bottom half users
//Yes, it's not the best way to do it, but I only had 3 minutes left
};
fileSystem.writeFileSync('userGroups.json', JSON.stringify(groupedUsers, null, 2));
console.log("\n=== Usuários agrupados por atividade guardados em \'userGroups.json\' ===");

console.log("\nFim\n");