# AISDD_PROJECT.md — Especificação de Alinhamento e Maturidade AISDD

Este documento formaliza o projeto **Casa em Dia** como 100% aderente ao paradigma **AISDD (AI System Design Document)**. Ele centraliza o mapeamento de maturidade do projeto e as convenções que governam o desenvolvimento assistido por agentes autônomos de Inteligência Artificial.

---

## 1. Metadados do Projeto AISDD

*   **Nome do Projeto:** Casa em Dia
*   **Identidade Principal:** Gerenciador offline-first de prazos, manutenção e garantias para Casa e Carro.
*   **Stack Tecnológica:** Kotlin, Jetpack Compose, Room (SQLite), MVVM, Robolectric/Roborazzi para testes locais JVM.
*   **Status de Conformidade:** Totalmente Compatível (100% AISDD-Compliant)
*   **Fila Ativa de Execução:** [docs/04_NEXT_TASK.md](04_NEXT_TASK.md)
*   **Ponto de Entrada Recomendado:** [docs/START_HERE.md](START_HERE.md)

---

## 2. Níveis de Maturidade do Projeto (PML - Project Maturity Levels)

O Casa em Dia suporta e atende rigorosamente a todos os níveis de maturidade projetados para o ciclo de vida de software orientado a IA:

### PML-1: Escopo e Concepção (Scope & Ideation) — [Concluído]
*   **Garantias:** Definição clara do que o produto é e o que ele categoria e restritamente NÃO é, eliminando escopos desnecessários desde a fundação.
*   **Evidências:** [docs/01_PRODUCT_SPEC.md](01_PRODUCT_SPEC.md) e [docs/00_PROJECT_RULES.md](00_PROJECT_RULES.md).

### PML-2: Design e Especificação Técnica (Tech Spec & Design) — [Concluído]
*   **Garantias:** Mapeamento integral dos modelos de domínio, relacionamentos lógicos do banco de dados Room e regras puras de cálculo matemático de alertas isolados da plataforma UI.
*   **Evidências:** [docs/02_ARCHITECTURE.md](02_ARCHITECTURE.md).

### PML-3: Fundação e Navegação de Esqueleto (Scaffolding & Navigation) — [Concluído]
*   **Garantias:** Criação de pacotes Clean, roteamentos baseados em rotas tipadas com Jetpack Navigation e fluxos reativos puros via Flow/StateFlow no ViewModel central.
*   **Evidências:** `MainActivity.kt` e índice de navegação mapeado.

### PML-4: MVP Funcional Completo (Functional MVP) — [Concluído]
*   **Garantias:** CRUD completo reativo, disparador inteligente de lembretes e alarmes futuros (`AlarmManager` via `NotificationScheduler`), painel Bento Grid de KPIs dinâmicos (`KpiDashboard`) e backup offline total local via Storage Access Framework (`BackupHelper` salvando e importando JSON).
*   **Evidências:** [docs/03_CURRENT_STATE.md](03_CURRENT_STATE.md) (100% pronto e polido).

### PML-5: Cobertura de Qualidade e Testes (QA & Verification) — [Concluído]
*   **Garantias:** Testes de persistência em memória, testes de fluxo de conclusão transacional e histórico na JVM utilizando Robolectric, além de testes estéticos rápidos de regressão via Roborazzi.
*   **Evidências:** `ExampleRobolectricTest.kt` e [docs/11_TEST_STRATEGY.md](11_TEST_STRATEGY.md).

---

## 3. Diretrizes de Governança para Agentes (IAs)

1.  **Consulte a Fila Ativa:** O ponto de partida prioritário para qualquer execução autônoma de tarefas é o arquivo [docs/04_NEXT_TASK.md](04_NEXT_TASK.md).
2.  **Siga as Restrições Cardinais:** Nunca adicione APIs na nuvem, autenticação ou dependências extras não autorizadas (veja regras em [docs/00_PROJECT_RULES.md](00_PROJECT_RULES.md)).
3.  **Preserve os Testes:** Qualquer alteração futura deve rodar e passar na bateria automatizada JVM sem quebrar as suítes estáveis existentes.
4.  **Responda no Padrão:** Use sempre o formato de saída mapeado em [docs/00_PROJECT_RULES.md](00_PROJECT_RULES.md) ao reportar entregas.
