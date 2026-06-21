# 02_ARCHITECTURE.md — Arquitetura de Referência Técnica

Este documento fornece as especificações e diretrizes arquiteturais mais profundas do projeto **Casa em Dia** para manter a separação rígida de responsabilidades, previsibilidade técnica e alta manutenibilidade do ecossistema. Ele atua como a única fonte da verdade (Single Source of Truth) para o design de sistema.

---

## 1. Arquitetura Geral (MVVM)

O aplicativo segue estritamente o padrão arquitetural **MVVM (Model-View-ViewModel)** com fluxo unidirecional de dados (UDF) e estado imutável exposto via `StateFlow` da biblioteca de coroutines do Kotlin.

### Fluxograma de Comunicação Técnica

```
  [ UI / Camada de Escurecimento (Jetpack Compose) ] 
                     ↓↑ (UI State reativos & Events)
  [ ViewModel (Jetpack ViewModel + StateFlow) ]
                     ↓↑ (Domain Models limpos / Flows)
  [ Repository Layer (Repository Interface & Impl) ]
                     ↓↑ (Mappers puros: Entity ↔ Domain Model)
  [ Data Sources / Room Local DB ] ↔ [ AlarmManager (Notificações) ]
```

### Detalhe de Responsabilidades das Camadas:

1.  **Camada de UI (Telas Compose):**
    *   Escrita integralmente em **Jetpack Compose** orientada a Material Design 3.
    *   **Totalmente Declarativa:** É terminantemente proibido realizar consultas de banco diretamente na UI ou fazer cálculos matemáticos de vencimentos ou odômetros. Ela consome dados polidos expostos pelo `ViewModel` via `StateFlow`.
2.  **Camada de ViewModel:**
    *   Orquestrado único do estado de renderização. Converte fluxos do repositório em estados imutáveis seguros de tela.
3.  **Camada de Repositório (Repository):**
    *   Centraliza e abstrai a fonte secundária de dados. Fornece instâncias limpas do Domínio chamando conversores.
4.  **Camada de Mapeamento (ModelMapper):**
    *   Fakes e conversores puros que blindam as regras de domínio imutáveis contra classes de tabelas físicas do Room (evitando poluir a camada visual com anotações de persistência).
5.  **Camada de Persistência (Room Local DB):**
    *   SQLite gerenciado pelo Room. Única fonte de verdade física (*Single Source of Truth*).

---

## 2. Modelos de Domínio Puros (Kotlin Coroutines-Safe)

Estes modelos de dados imutáveis representam as estruturas de negócio puras que transitam da ViewModel até a camada visual:

### 1. `Asset` (Ativo)
Representa uma propriedade monitorada pelo usuário:
*   `id`: `String` (UUID único gerado em tempo de inserção)
*   `name`: `String` (Nome amigável descritivo, ex: "Palio 1.6", "Minha Casa")
*   `type`: `AssetType` (Filtro por enum: `CAR`, `HOME`, `DOCUMENT`, `OTHER`)
*   `description`: `String?` (Comentários gerais opcionais)
*   `identifier`: `String?` (Elemento físico do ativo, ex: placa do carro, código de matrícula)

### 2. `ControlItem` (Item de Controle)
Representa um compromisso, prazo ou manutenção vinculada a um Ativo:
*   `id`: `String` (UUID único)
*   `assetId`: `String` (Vínculo com o Ativo correspondente)
*   `title`: `String` (Nome da manutenção)
*   `type`: `ControlItemType` (Enum: `FIXED_DATE`, `TIME_INTERVAL`, `MILEAGE`, `INFO`, `DOCUMENT`)
*   `status`: `ControlStatus` (Estado calculado: `OK`, `ATTENTION`, `OVERDUE`)
*   `limitDate`: `LocalDate?` (Data do prazo legal)
*   `alertWindowDays`: `Int` (Margem antecipada em dias para aviso, padrão = 7)
*   `predictedMileage`: `Int?` (Odômetro de vencimento da tarefa)
*   `alertWindowMileage`: `Int?` (Margem em km para aviso antecipado, padrão = 1.000)
*   `currentMileage`: `Int?` (Quilometragem capturada na última revisão)
*   `notes`: `String?` (Anotações contextuais de progresso)

### 3. `Attachment` (Anexo virtual)
Fotos ou comprovantes indexados do item:
*   `id`: `String` (UUID)
*   `itemId`: `String` (Vínculo com o Item)
*   `name`: `String` (Nome descritivo)
*   `type`: `String` (Determinação MIME do arquivo)
*   `size`: `String` (Ex: "1.5 MB")
*   `uri`: `String` (Identificador local da Sandbox interna)

### 4. `HistoryEntry` (Linha do Tempo / Registro Histórico)
Entrada que documenta que a manutenção foi consolidada:
*   `id`: `String` (UUID)
*   `itemId`: `String` (UUID do item)
*   `date`: `LocalDate` (Dia exato de conclusão)
*   `title`: `String` (Ex: "Correia de suspensão substituída")
*   `cost`: `Double?` (Custos financeiros informados pelo usuário)
*   `notes`: `String?` (Marca de reposição, notas de marca ou observações)

---

## 3. Estrutura Física do Banco de Dados local (Room)

Para garantir simplicidade técnica absoluto e transição estável, os modelos lógicos são persistidos no Room através das seguintes representações físicas:

*   **Tabela `maintenance_items` (Entity: `MaintenanceItem`):** Consolida em uma estrutura unificada o ativo e o item de controle (com categorização rápida como "CARRO", "CASA" ou "OUTRO"), otimizando o carregamento visual reativo.
*   **Tabela `app_codes` (Entity: `AppCode`):** Registra tokens, identificadores de segurança e chaves do Arquivo Vivo.
*   **Tabela `app_notes` (Entity: `AppNote`):** Armazena notas e comentários rápidos do Arquivo Vivo.
*   **Tabela `document_items` (Entity: `DocumentItem`):** Referências locais a caminhos digitados do Arquivo Vivo.
*   **Tabela `history_entries` (Entity: `HistoryEntryEntity`):** Tabela histórica de ações consolidadas para relatórios e auditorias de conciliação.

---

## 4. Agendador e Alarmes locais (`AlarmManager`)

As notificações de alertas antecipados operam integradas ao processador de tarefas físicas do celular Android:
*   **`NotificationScheduler`:** Responsável técnico por criar agendamentos imperativos exatos no `AlarmManager` para acordar o app no período residual configurado pelo usuário (`alertWindowDays`).
*   **`NotificationReceiver`:** `BroadcastReceiver` básico que manipula o processador secundário em background e dispara a notificação visual na barra de status em pt-BR.

---

## 5. Referências Cruzadas
*   Para verificar diagramas de casos e especificações do produto, acesse [docs/01_PRODUCT_SPEC.md](01_PRODUCT_SPEC.md).
*   Para acompanhar as regras de crescimento de arquivos e conformidade de IAs, veja [docs/00_PROJECT_RULES.md](00_PROJECT_RULES.md).
*   Para o índice completo do código e mappers físicos, consulte [docs/09_FILE_INDEX.md](09_FILE_INDEX.md).
