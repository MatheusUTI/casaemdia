# 09_FILE_INDEX.md — Mapeamento do Projeto e Código-Fonte

Este documento atua como o mapa principal do projeto **Casa em Dia**, listando a estrutura de pastas, fluxos de dados lógicos essenciais e índices de responsabilidade individual de arquivos.

---

## 1. Estrutura de Pastas de Referência

```
/app/src/main/java/com/example/
  ├─ MainActivity.kt                          # Ponto de partida, NavHost e configuração de temas
  ├─ data/                                    # Camada de Persistência, Modelagem e Agendamento
  │   ├─ AppDatabase.kt                       # Definição do banco Room, tabelas e mappers associados
  │   ├─ BackupHelper.kt                      # Lógica de serialização JSON e exportação de backups locais
  │   ├─ NotificationScheduler.kt             # Lógica integrada para alarmes locais
  │   └─ RecurrenceHelper.kt                  # Auxiliar para próximos saltos de recorrência e labels
  └─ ui/                                      # Telas declarativas Compose, ViewModels e Temas
      ├─ HomeScreen.kt                        # Painel principal consolidado por gravidade de prazo
      ├─ KpiDashboard.kt                      # Dashboard de KPIs no topo da Home (Bento Grid)
      ├─ NewItemScreen.kt                     # Criação e Edição de lembretes ativos
      ├─ ValidatedTextFields.kt               # Campo OutlinedTextField com validações integradas
      ├─ FormComponents.kt                    # Componentes reutilizáveis do formulário (Categoria, Lembrete, etc.)
      ├─ SettingsScreen.kt                    # Painel de controle de backup e restauração local do banco
      ├─ SettingsBackupCards.kt               # Botões, cartões de feedback e linhas de backup
      ├─ SettingsBackupDialogs.kt             # Diálogos de confirmação de overwrite de restore
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

### `KpiDashboard.kt`
*   **Responsabilidade:** Componente de Dashboard isolado contendo cards de indicadores (Bento Grid) para lembretes ativos, concluídos, atrasados, vencendo hoje e próximos 7 dias.
*   **Arquivos relacionados:** `HomeScreen.kt`, `MainViewModel.kt`.
*   **Quando alterar:** Ao precisar modificar a estética, cores, regras de contagem ou inclusão de novos cartões de indicadores de progresso.
*   **Quando evitar:** Lógicas de agendamento de alarmes locais ou regras diretas do Room.

### `NewItemScreen.kt`
*   **Responsabilidade:** Tela única para criar ou atualizar lembretes ativos.
*   **Arquivos relacionados:** `ValidatedTextFields.kt`, `FormComponents.kt`, `RecurrenceSelectors.kt`, `MainViewModel.kt`.
*   **Quando alterar:** Ao mudar o fluxo de navegação ou os estados da tela de criação.
*   **Quando evitar:** Para alterar visualizações exclusivas do histórico ou ajustar as validações visuais diretas dos campos.

### `ValidatedTextFields.kt`
*   **Responsabilidade:** Fornecer o componente `ValidatedTextField` com contagem automática de limites de caracteres e erros de validação visual.
*   **Arquivos relacionados:** `NewItemScreen.kt`.
*   **Quando alterar:** Ao alterar estilos, contadores, ou comportamento padrão dos Inputs de formulário.

### `FormComponents.kt`
*   **Responsabilidade:** Fornecer elementos comuns de formulário como seleção de categorias de chips e o switch do lembrete inteligente.
*   **Arquivos relacionados:** `NewItemScreen.kt`.
*   **Quando alterar:** Ao alterar os estilos visuais destes cartões e seletores.

### `SettingsScreen.kt`
*   **Responsabilidade:** Interface principal das Configurações do app contendo painéis para criação de backups rápidos na sandbox interna e exportação/importação via arquivos JSON (Storage Access Framework).
*   **Arquivos relacionados:** `BackupHelper.kt`, `MainViewModel.kt`, `MainActivity.kt`, `SettingsBackupCards.kt`, `SettingsBackupDialogs.kt`.
*   **Quando alterar:** Ao alterar o fluxo de navegação, roteamento de intents ou estados centrais da tela de configurações.

### `SettingsBackupCards.kt`
*   **Responsabilidade:** Fornecer os cards de introdução, alertas de erro/sucesso rápidos e linhas de listagem para cada arquivo JSON de backup disponível.
*   **Arquivos relacionados:** `SettingsScreen.kt`.
*   **Quando alterar:** Ao alterar estilos visuais das listas de backups ou cards de alerta de configurações.

### `SettingsBackupDialogs.kt`
*   **Responsabilidade:** Diálogo de alerta preventivo para confirmação de overwrite antes de restaurar qualquer backup e sobrescrever a base Room.
*   **Arquivos relacionados:** `SettingsScreen.kt`.
*   **Quando alterar:** Para mudar as mensagens de aviso ou regras de interrupção visual de diálogos.

### `BackupHelper.kt`
*   **Responsabilidade:** Lógica isolada de serialização JSON, carregamento de streams IO, leitura de sandbox interna e inserção/destruição sequencial do banco de dados Room. Exclui a necessidade de acoplamento pesado na ViewModel principal.
*   **Arquivos relacionados:** `AppDatabase.kt`, `SettingsScreen.kt`.
*   **Quando alterar:** Ao precisar modificar a semântica/estrutura do arquivo exportado ou adicionar/remover novas tabelas no escopo do backup.

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
*   Consulte as diretrizes e regras do padrão em [AISDD_PROJECT.md](AISDD_PROJECT.md).
*   Veja o detalhe das metas funcionais do produto em [01_PRODUCT_SPEC.md](01_PRODUCT_SPEC.md).
*   Consulte os logs de decisões de arquitetura em [06_DECISIONS_LOG.md](06_DECISIONS_LOG.md).
*   Consulte a estratégia de testes oficial detalhada em [11_TEST_STRATEGY.md](11_TEST_STRATEGY.md).
