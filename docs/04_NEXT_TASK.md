# 04_NEXT_TASK.md — Fila Ativa de Execução Autônoma

Este documento representa a **única fonte oficial da verdade** sobre a próxima tarefa a ser executada.

Sob o paradigma **AISDD**, qualquer agente de IA deve:

1. Ler este documento integralmente.
2. Executar exclusivamente a tarefa marcada como **Pending**.
3. Respeitar todas as regras globais definidas em `00_PROJECT_RULES.md`.
4. Atualizar este documento após a conclusão da tarefa.
5. Promover automaticamente a próxima tarefa planejada para `Pending`.

---

# Tarefa Ativa

## Metadados

| Campo               | Valor                         |
| ------------------- | ----------------------------- |
| ID                  | NT-015CD                      |
| Título              | CR14 — Estatísticas Avançadas |
| Status              | Pending                       |
| Prioridade          | Alta                          |
| Tipo                | Nova Funcionalidade           |
| PML Alvo            | PML-3                         |
| Complexidade        | Média                         |
| Risco               | Baixo                         |
| Build Obrigatório   | Sim                           |
| Testes Obrigatórios | Sim                           |

---

## Objetivo

Implementar um painel avançado de estatísticas reativas capaz de fornecer ao usuário informações quantitativas e qualitativas sobre o estado atual das manutenções cadastradas.

---

## Contexto Funcional

O aplicativo já possui:

* Dashboard simples de KPIs.
* Sistema completo de CRUD.
* Histórico funcional.
* Recorrência automática.
* Backup e restauração local.
* Empty States refinados.
* Sistema de notificações locais.

Esta tarefa deve expandir a capacidade analítica do aplicativo sem alterar a arquitetura existente.

---

## Escopo Obrigatório

Implementar:

### Percentual Concluído

Exibir percentual geral de tarefas concluídas.

### Percentual Atrasado

Exibir percentual de itens ativos atualmente vencidos.

### Categoria Predominante

Exibir qual categoria possui maior incidência de manutenções.

### Próximo Vencimento

Exibir a próxima manutenção iminente.

### Totalização por Categoria

Exibir total de itens Casa e Carro.

---

## Fora de Escopo

Não implementar:

* gráficos complexos;
* bibliotecas externas;
* sincronização em nuvem;
* persistência adicional;
* exportação de estatísticas;
* alterações no schema Room;
* mudanças arquiteturais profundas.

---

## Restrições Técnicas

* Não alterar entidades Room.
* Não criar migrações.
* Não alterar BackupHelper.
* Não alterar notificações.
* Não quebrar KPIs existentes.
* Preservar arquitetura MVVM.
* Preservar Material 3.
* Manter comportamento atual.

---

## Arquivos Provavelmente Impactados

Alta probabilidade:

* HomeScreen.kt
* KpiDashboard.kt
* MainViewModel.kt

Baixa probabilidade:

* AppDatabase.kt

Criar novos componentes quando necessário.

---

## Regras de Crescimento

Caso qualquer arquivo ultrapasse limites recomendados:

* extrair componentes;
* extrair helpers;
* atualizar FILE_INDEX.

Limites:

| Tipo           | Limite     |
| -------------- | ---------- |
| Compose Screen | 300 linhas |
| Component      | 150 linhas |
| Dialog         | 100 linhas |
| Helper         | 200 linhas |
| ViewModel      | 400 linhas |

---

## Critérios de Aceite

A tarefa será considerada concluída somente se:

* [ ] Todos os indicadores forem reativos.
* [ ] Nenhuma regressão funcional ocorrer.
* [ ] Dashboard permanecer modular.
* [ ] Build permanecer verde.
* [ ] Testes existentes permanecerem verdes.
* [ ] Nenhum arquivo crítico crescer sem justificativa.
* [ ] Documentação impactada for atualizada.
* [ ] Handoff for atualizado.

---

## Riscos Conhecidos

* Divergência entre ativos e histórico.
* Necessidade de tratamento para listas vazias.
* Possível crescimento excessivo do KpiDashboard.

---

## Documentação Obrigatória

Atualizar caso impactado:

* docs/03_CURRENT_STATE.md
* docs/07_HANDOFF.md
* docs/09_FILE_INDEX.md

---

## Formato Obrigatório da Resposta

Toda entrega deve conter:

* Objective
* Files changed
* Contract impact
* FACTS
* ASSUMPTIONS
* UNKNOWNS
* RISKS
* Acceptance status
* Updated handoff

---

# Backlog Estruturado

## NT-016CD — CR15 — Exportação de Histórico

Status: Planned

Prioridade: Alta

Objetivo:

Permitir exportar o histórico completo em CSV utilizando SAF.

---

## NT-017CD — CR16 — Estatísticas Históricas

Status: Planned

Prioridade: Alta

Objetivo:

Exibir estatísticas acumuladas por período.

---

## NT-018CD — CR17 — Favoritos e Prioridades

Status: Planned

Prioridade: Média

Objetivo:

Permitir favoritar e fixar itens importantes.

---

## NT-019CD — CR18 — Tags Personalizadas

Status: Planned

Prioridade: Média

Objetivo:

Implementar tags livres para organização avançada.

---

## NT-020CD — CR19 — Busca Global

Status: Planned

Prioridade: Média

Objetivo:

Implementar busca unificada no aplicativo.

---

## NT-021CD — CR20 — Galeria de Documentos

Status: Planned

Prioridade: Média

Objetivo:

Melhorar visualização e organização dos anexos.

---

## NT-022CD — CR21 — Painel de Saúde da Casa

Status: Planned

Prioridade: Alta

Objetivo:

Criar score geral de manutenção patrimonial.

---

## NT-023CD — CR22 — Versionamento de Backups

Status: Planned

Prioridade: Muito Alta

Objetivo:

Garantir compatibilidade entre versões futuras do aplicativo.

---

## NT-024CD — CR23 — Testes de Migração Room

Status: Planned

Prioridade: Muito Alta

Objetivo:

Blindar futuras evoluções do banco de dados.

---

## NT-025CD — CR24 — Centro de Configurações Avançadas

Status: Planned

Prioridade: Média

Objetivo:

Expandir preferências avançadas do usuário.

---

## NT-026CD — CR25 — Preparação para Sincronização Futura

Status: Planned

Prioridade: Baixa

Objetivo:

Preparar arquitetura para sincronização futura sem implementar nuvem.

---

# Histórico Recente

## NT-014CD — Concluído

Empty States elegantes e refinamento visual.

## NT-013CD — Concluído

Cobertura avançada de testes Robolectric para Backup e SAF.

---

# Referência Cruzada

Consultar:

* `docs/AISDD_PROJECT.md`
* `docs/00_PROJECT_RULES.md`
* `docs/03_CURRENT_STATE.md`
* `docs/07_HANDOFF.md`
* `docs/09_FILE_INDEX.md`
