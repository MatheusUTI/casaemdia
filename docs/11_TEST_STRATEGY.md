# 11_TEST_STRATEGY.md — Estratégia Oficial de Testes

Este documento estabelece a Estratégia Oficial de Testes para o projeto **Casa em Dia**, consolidando as práticas de qualidade, as suítes de testes existentes e as diretrizes norteadoras para futuras implementações, visando prevenir regressões em fluxos críticos locais.

---

## 1. Objetivo da Estratégia de Testes

Garantir a estabilidade de médio e longo prazo do aplicativo **Casa em Dia** através de uma pirâmide de testes pragmática, eficiente e local (offline). Os testes automatizados no projeto têm os seguintes propósitos claros:
*   **Prevenção de Regressões:** Bloquear quebras acidentais em lógicas cruciais como cálculos de prazos, regras de conclusão/recorrência e agendamento de alarmes.
*   **Velocidade de Feedback:** Utilizar testes que executam rapidamente na JVM local (sem depender de emuladores lentos) para validação contínua durante o desenvolvimento.
*   **Documentação Viva:** Servir como especificação executável dos contratos e comportamentos acordados para o motor e as camadas críticas do applet.

---

## 2. Suítes de Testes Existentes

Atualmente, o projeto possui três suítes de testes robustas e automatizadas que cobrem a lógica central do Casa em Dia de ponta a ponta:

### A. `RecurrenceHelperTest`
*   **Objetivo:** Validar o cálculo determinístico de novas datas de vencimento com base na frequência do lembrete.
*   **Tipo de Teste:** Teste Unitário JVM Puro (Standard JUnit).
*   **Tecnologia:** Kotlin Standard Library (JUnit 4).
*   **O que valida:** 
    *   Incremento correto de períodos: Diário (+1 dia), Semanal (+7 dias), Quinzenal (+15 dias), Mensal (+1 mês), Bimestral (+2 meses), Trimestral (+3 meses), Semestral (+6 meses), Anual (+1 ano).
    *   Casos de limite de fim de mês extremos (ex: 31 de Janeiro adicionando 1 mês deve ser recalculado corretamente para 28 ou 29 de Fevereiro dependendo se o ano é bissexto).
*   **Riscos Cobertos:** Erros de cálculo em datas que causem tarefas duplicadas com datas incorretas, bugs no motor de datas da JVM ou falha de conversão de string de data.

### B. `NotificationSchedulerTest`
*   **Objetivo:** Assegurar que o agendador local (`NotificationScheduler`) registre, atualize e remova alarmes no Android de forma segura e consistente.
*   **Tipo de Teste:** Teste de Integração na JVM.
*   **Tecnologia:** Robolectric (simula a API do Android `AlarmManager` na JVM) + AndroidX Test.
*   **O que valida:** 
    *   Agendamento correto de um alarme com data futura, garantindo que o request code do alarme seja igual ao ID correspondente do lembrete.
    *   Cancelamento seguro de agendamentos no sistema operacional para evitar notificações duplicadas ou indesejadas.
    *   Interrupção graciosa de agendamentos para alertas no passado (onde o limite menos o aviso prévio já expirou).
*   **Riscos Cobertos:** Crashes no `AlarmManager` sob Android moderno (13+), alarmes disparados fora de hora ou com IDs conflitantes e agendamento silencioso de itens que já deveriam ter sido alertados.

### C. `MainViewModelTest`
*   **Objetivo:** Consolidar a integridade dos fluxos de dados, interações com banco local e manipulação de estado do ciclo de vida completo do aplicativo.
*   **Tipo de Teste:** Teste de Integração de Arquitetura (MVVM Integrado).
*   **Tecnologia:** Robolectric + Room In-Memory Datastore + Coroutines Test.
*   **O que valida:** 
    *   **Fluxo de CRUD:** Criação, edição e exclusão de itens de manutenção física com reflexo reativo instantâneo.
    *   **Estabilidade do Histórico:** Transição segura da conclusão de lembretes ativos para registros no histórico de auditoria.
    *   **Mecanismo de Recorrência Integrado:** Auto-geração inteligente e transparente de novas instâncias recorrentes sob a intervenção do ViewModel após a conclusão da atividade anterior.
    *   **Restauração e Exclusão Segura:** Desfazer ações de conclusão sem vazamento de memória ou duplicidade de histórico e exclusão total limpa de dados.
    *   **Emissões Reativas de Estado:** Coleta adequada dos `StateFlow`s, garantindo sincronização sem race conditions entre banco Room e UI.
*   **Riscos Cobertos:** Inconsistência de listas reativas nas telas do app, perda silenciosa de dados históricos ou registros redundantes, descompasso entre banco de dados e agendamento físico das notificações.

---

## 3. Diretriz de Testes por Camada

A pirâmide de testes e responsabilidade de cada camada é regulada pelas seguintes diretrizes de arquitetura:

| Camada | Estratégia de Teste Recomendada | Motivação e Abordagem |
| :--- | :--- | :--- |
| **Helpers / Utility / Pure Logics** | Testes JVM Unitários Puros | Executados de forma pura na JVM comum. Sem simulações ou overhead, sendo extremamente rápidos e precisos. |
| **NotificationScheduler / OS integration** | Testes Robolectric com Shadows | Uso de Shadows (ex: `ShadowAlarmManager`, `ShadowPendingIntent`) para inspecionar estados do sistema operacional sem hardware real. |
| **MainViewModel / Controllers** | Testes Robolectric + Room In-Memory | Injeção de bancos temporários em memória com executores de sincronização síncronos, simulando as coroutines em background com flows coletados. |
| **DAO / Room Database** | Testes Room In-Memory puros | Instanciação temporária do DB por cada teste para testar queries SQL complexas, chaves estrangeiras e índices sem poluir o armazenamento do usuário. |
| **Compose UI Screens** | Apenas quando requerido explicitamente | Testes de visualização ou interações complexas de layout. Prioriza-se a testabilidade testando-se a ViewModel associada em vez de acoplamento com Compose. |

---

## 4. Regras e Boas Práticas Gerais

Todo novo teste escrito no projeto Casa em Dia deve seguir estritamente as regras de confiabilidade e manutenibilidade abaixo:

*   **Não Testar Detalhes Estéticos:** Não acoplar testes automatizados a cores, fontes, tamanhos de margem ou animações locais do Compose, a menos que haja requisito de visualização crítico. Testar a máquina de estados e as lógicas de negócio subjacentes.
*   **Foco nas Regras de Negócio e Regressões:** O teste deve atuar como uma vacina contra bugs reais que já ocorreram ou que impediriam o usuário de utilizar a funcionalidade.
*   **Eliminar Dependências Pesadas:** Evitar o uso excessivo de frameworks de mocking complicados (como Mockito ou Mockk) para evitar quebras por atualizações da biblioteca. Priorize fakes simples em memória, bancos `inMemory` reais e código Kotlin limpo.
*   **Determinismo Absoluto:** Todos os testes criados devem passar de forma totalmente determinística, independentemente de fusos horários, segundos correntes ou ordem de execução.
*   **Independência de Relógio:** Para lógicas temporais complexas, injetar timestamps ou datas de referência (ex: overload de métodos com data de referência padrão) para permitir simular o tempo de forma estática e controlada.
*   **Dispositivo-Zero:** Não escrever códigos dependentes de hardware real (Wifi, GPS, acelerômetro real) ou de rede externa (APIs de terceiros). Prefira simular essas interfaces com dados mocked locais ou testes de interface virtual (Robolectric).

---

## 5. Próximas Coberturas Recomendadas

Para continuar incrementando a robustez do ecossistema técnico do Casa em Dia conforme o escopo expandir:

1.  **Testes de Validação Completa de DAO:**
    *   Verificação profunda das chaves estrangeiras caso novas tabelas aninhadas de dependências sejam introduzidas.
    *   Queries complexas com filtros e ordenações específicas de criticidade de prazos.
2.  **Validações Integradas nos Formulários de Entrada:**
    *   Testar se strings vazias, valores inadequados de dias de proximidade antecipada ou nomes abusivamente extensos são reatados com sanitização correta e tratamento estrito.
3.  **UI Compose Centrada em Caso de Fluxo Crítico (Screenshot Testing):**
    *   Uso de Roborazzi para registrar telas principais a fim de evitar sobreposição de textos, truncamento de palavras ou estiramento de cards do painel em telas expanded (tablets).
4.  **Testes de Migração Room:**
    *   Caso haja modificação estrutural na base SQLite (exemplo: adição de colunas ou tabelas extras), criar testes simulando a transição entre versões do `AppDatabase` para garantir retenção absoluta de dados anteriores dos usuários.

---

## Referência Cruzada de Documentos
*   Para verificar critérios de aceitação e entrega globais, consulte os [05_ACCEPTANCE_CHECKS.md](05_ACCEPTANCE_CHECKS.md).
*   Para revisar a lista consolidada das responsabilidades de arquivos e suas posições estruturais, veja [09_FILE_INDEX.md](09_FILE_INDEX.md).
*   Para rodar simulações manuais rápidas de validações básicas, utilize o [10_TEST_CHECKLIST.md](10_TEST_CHECKLIST.md).
