# 04_NEXT_TASK.md — Fila Ativa de Execução Autônoma (NT-015CD)

Este documento atua como o ponto oficial e definitivo de atribuição para o próximo ciclo de desenvolvimento do **Casa em Dia**. Sob o paradigma **AISDD**, qualquer agente de IA que iniciar uma nova sessão deve ler este arquivo para identificar e executar a tarefa de maior prioridade atual listada abaixo.

---

## Atribuição Ativa da Fila (ID: NT-015CD)

*   **Status:** Planejado / Pronto para Iniciar Proposta de Mudança (CR14)
*   **Prioridade:** Alta (Estatísticas Avançadas)
*   **Nível PML Alvo:** PML-3 (Expansão Funcional de Requisitos de Negócio)
*   **Objetivo:** Implementar o painel reativo de Estatísticas Avançadas, provendo insights quantitativos e qualitativos sobre cuidados preventivos de manutenção.

*   **Escopo:**
    *   **Indicador de Percentual Concluído:** Mediar o progresso geral ou por período comparativo.
    *   **Percentual de Atraso:** Destacar gargalos e pontos que requerem atenção crítica.
    *   **Categoria mais frequente:** Calcular e exibir qual das principais frentes (Casa ou Carro) é a detentora de maior recorrência.
    *   **Próximo vencimento iminente:** Indicar textualmente qual tarefa está agendada para expirar mais rapidamente, ordenando no topo.
    *   **Totalização por Categoria:** Sumarizar quantitativo específico para propósitos informativos.
*   **Critérios de aceite:**
    *   O dashboard continua perfeitamente modular na UI e reativo a mudanças na base.
    *   KPIs e dados estatísticos são calculados reativamente e em tempo real.
    *   Sem alterações estruturais ou migrações complexas requeridas nas tabelas físicas do banco de dados MVP.

---

## Histórico Recente de Atribuições Concluídas

### Atribuição NT-014CD (CR13) — [Concluído com Sucesso]
*   **Objetivo:** Empty States elegantes de alta fidelidade e refinamento estético de UX.
*   **Entrega:** Criação do componente dinâmico reutilizável `FriendlyEmptyState.kt` alimentado por três belíssimas ilustrações desenhadas no vetor Canvas nativo (`HomeEmptyIllustration`, `HistoryEmptyIllustration`, `SettingsEmptyIllustration`), acompanhado de mensagens acolhedoras e transições suaves de fade-in via `AnimatedVisibility`. Integrado na Home Screen, Timeline Archive e Settings Screen.

### Atribuição NT-013CD — [Concluído com Sucesso]
*   **Objetivo:** Ampliação da cobertura de testes unitários do Robolectric simulando falhas físicas do Storage Access Framework (SAF) e JSONs malformatados resguardando transatibilidade Room.

---

## Referência Cruzada de Documentos
*   Consulte as diretrizes e regras do padrão em [docs/AISDD_PROJECT.md](AISDD_PROJECT.md).
*   Consulte as invariantes permanentes de build em [docs/00_PROJECT_RULES.md](00_PROJECT_RULES.md).
*   Consulte o histórico de entregas concluídas da CR13 em [docs/03_CURRENT_STATE.md](03_CURRENT_STATE.md).

