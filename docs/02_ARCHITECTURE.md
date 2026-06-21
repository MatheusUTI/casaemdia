# 02_ARCHITECTURE.md — Arquitetura de Referência Técnica

Este documento fornece as diretrizes arquiteturais do projeto **Casa em Dia** para manter a separação rígida de responsabilidades e alta consistência do ecossistema.

---

## 1. Arquitetura Geral (MVVM)

O aplicativo segue estritamente o padrão arquitetural **MVVM (Model-View-ViewModel)** com fluxo unidirecional de dados (UDF) e estado imutável exposto via `StateFlow` da biblioteca de coroutines do Kotlin.

### Fluxograma de Comunicação

```
  [ UI: Jetpack Compose Screens & Reusable Components ]
             ↑ (Observa State / Emite Eventos de Ação)
             ↓
  [ ViewModel (MainViewModel + Kotlin flows) ]
             ↓ (Interage com persistência / Orquestra regras)
             ↓
  [ Data Sources (Room Local DB & Repositories) ]
             ↓
  [ AlarmManager (Agendamento de Notificações Locais) ]
```

---

## 2. Camada de Persistência (Room Local DB)

A única fonte confiável de dados (Single Source of Truth) reside no banco de dados SQLite interno encapsulado pelo **Room**.

*   **Tabelas Principais:**
    *   `maintenance_items`: Contém registros de lembretes ativos e suas propriedades (recorrência, alertDaysBefore, notes, etc.).
    *   `history_entries`: Guarda registros estáticos de manutenções já concluídas arquivadas para fins estatísticos.
*   **Acesso a Dados (DAOs):**
    *   `MaintenanceDao`: Centraliza operações comuns do banco como inserção, remoção, restauração e consulta de listas ativas e históricas. Exposto reativamente por intermédio de `Flow<List<...>>`.

---

## 3. Camada de Agendamento (Notificação Local)

As notificações de alerta antecipado utilizam canais integrados seguros da plataforma Android:

*   **`NotificationScheduler`:** Responsável por interagir com o `AlarmManager` do sistema Android para criar alarmes de vencimento exatos de acordo com o parâmetro `alertDaysBefore`.
*   **`NotificationReceiver`:** BroadcastReceiver que captura os gatilhos e constrói uma notificação visual na barra de status do usuário.

---

## 4. Dependências Críticas

*   **Jetpack Compose & Material Design 3 (M3):** Biblioteca oficial declarativa para estruturar a experiência visual polida do layout.
*   **Room Database & KSP:** Motor de persistência local altamente performático e livre de tráfego de dados externo.
*   **Kotlin Coroutines & Flows:** Fundamento reativo que garante atualização em tempo real de estados em segundo plano sem travar a Thread principal UI.

---

## 5. Contratos e Interfaces Importantes

*   **`MainViewModel.kt`:** Atua como o ponto focal para processar e exportar os fluxos reativos do banco, expondo as ações `addMaintenanceItem`, `completeItem`, `deleteMaintenanceItem` e `restoreHistoryEntry`.

---

## 6. Referência Cruzada de Documentos
*   Veja o índice de todos os arquivos e fluxos de navegação em [09_FILE_INDEX.md](09_FILE_INDEX.md).
*   Para acompanhar as regras de aceitação de testes locais, verifique [10_TEST_CHECKLIST.md](10_TEST_CHECKLIST.md).
