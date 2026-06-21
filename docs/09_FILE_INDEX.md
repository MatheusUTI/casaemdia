# 09_FILE_INDEX.md — Mapeamento do Projeto e Código-Fonte

Este documento atua como o mapa principal do projeto **Casa em Dia**, listando a estrutura de pastas, fluxos de dados lógicos essenciais e índices de responsabilidade individual de arquivos.

---

## 1. Estrutura de Pastas de Referência

```
/app/src/main/java/com/example/
  ├─ MainActivity.kt                          # Ponto de partida, NavHost e configuração de temas
  ├─ data/                                    # Camada de Persistência, Modelagem e Agendamento
  │   ├─ AppDatabase.kt                       # Definição do banco Room, tabelas e mappers associados
  │   ├─ NotificationScheduler.kt             # Lógica integrada para alarmes locais
  │   └─ RecurrenceHelper.kt                  # Auxiliar para próximos saltos de recorrência e labels
  └─ ui/                                      # Telas declarativas Compose, ViewModels e Temas
      ├─ HomeScreen.kt                        # Painel principal consolidado por gravidade de prazo
      ├─ NewItemScreen.kt                     # Criação e Edição de lembretes ativos
      ├─ ItemDetailScreen.kt                  # Visualização de atributos e ações (editar, concluir, deletar)
      ├─ HistoryDetailScreen.kt               # Detalhes de registros e ação de reativação (restaurar)
      ├─ RecurrenceSelectors.kt               # UI de seleção de recorrência e de alertas antecipados
      ├─ TaskCards.kt                         # Coleção de cards visuais para itens de listas
      ├─ Illustrations.kt                     # Canvas personalizados e ilustrações dinâmicas
      ├─ ExtraComponents.kt                   # Elementos auxiliares, grades e chips de categoria
      ├─ Components.kt                        # Navegadores visuais e cabeçalhos de tela reutilizáveis
      └─ MainViewModel.kt                     # ViewModel centralizador para estados de UI
```

---

## 2. Mapa de Fluxos de Execução Logísticos

*   **Criar Lembrete:** `NewItemScreen` → `MainViewModel` → `MaintenanceDao` → `Room Local DB` → `NotificationScheduler` (AlarmManager agendado se alertDaysBefore > 0).
*   **Concluir Lembrete:** `HomeScreen` ou `ItemDetailScreen` → `MainViewModel` → `MaintenanceDao` → `HistoryEntry` criado + `MaintenanceItem` (excluído de ativos ou recalculado se recorrente) → `NotificationScheduler` (cancela alarmes antigos ou re-agenda pró-ativo).
*   **Restaurar Concluído:** `HistoryDetailScreen` → `MainViewModel` → `MaintenanceDao` (remove histórico e reinsere em ativos).

---

## 3. Registro de Arquivos Principais

### `AppDatabase.kt`
*   **Responsabilidade:** Declarar tabelas persistidas (`MaintenanceItem`, `HistoryEntry`), banco Room e lógicas cruciais do DAO.
*   **Arquivos relacionados:** `MainViewModel.kt`, `RecurrenceHelper.kt`.
*   **Quando alterar:** Ao adicionar ou modificar propriedades de lembrete que requerem persistência.
*   **Quando evitar:** Mudanças estéticas de UI ou de agendamento que não envolvem SQLite direto.

### `MainViewModel.kt`
*   **Responsabilidade:** Expor estados síncronos e reativos da UI conectando ações do usuário aos repositórios.
*   **Arquivos relacionados:** Todas as telas de interface (`ui/`).
*   **Quando alterar:** Nova requisição de eventos da interface com persistência.
*   **Quando evitar:** Edição puramente cosmética ou adição de animações localizadas.

### `NewItemScreen.kt`
*   **Responsabilidade:** Tela única para criar ou atualizar lembretes ativos.
*   **Arquivos relacionados:** `RecurrenceSelectors.kt`, `MainViewModel.kt`.
*   **Quando alterar:** Campos extras inseridos na criação de lembretes.
*   **Quando evitar:** Para ajustar visualizações exclusivas do histórico ou página de início.

---

## 4. Diretrizes de Crescimento de Arquivos

Para garantir que o projeto continue de fácil manutenção, são estabelecidos limites máximos sugeridos de linhas por tipo de arquivo:

*   **Tela Compose:** máximo 300 linhas (comportamento de tela inteira).
*   **Componente (UI):** máximo 150 linhas (UI isolada ou cards).
*   **Dialog (UI):** máximo 100 linhas (diálogos modais).
*   **ViewModel:** máximo 400 linhas (estados e chamadas de fluxo).
*   **DAO/Database:** máximo 400 linhas (entidades e queries SQL).
*   **Utils/Helpers:** máximo 200 linhas (funções utilitárias puras).

Sempre que um limite for atingido, a IA deve preferir a extração cuidadosa de novos arquivos e helpers para evitar o surgimento de classes gigantes (God Objects) e arquivos monolíticos de visualização Compose.

---

## Evolução futura da documentação por feature

Quando o projeto crescer, criar:

`docs/features/`
*   `feature_reminders.md`
*   `feature_history.md`
*   `feature_notifications.md`
*   `feature_vehicles.md`
*   `feature_houses.md`

### Regra de Escala:
Criar documentação por feature apenas quando a funcionalidade ficar grande o suficiente para justificar separação. Não criar agora se não for necessário.

---

## 5. Referência Cruzada de Documentos
*   Veja o detalhe das metas funcionais do produto em [01_PRODUCT_SPEC.md](01_PRODUCT_SPEC.md).
*   Consulte os logs de decisões de arquitetura em [06_DECISIONS_LOG.md](06_DECISIONS_LOG.md).
*   Consulte a estratégia de testes oficial detalhada em [11_TEST_STRATEGY.md](11_TEST_STRATEGY.md).
