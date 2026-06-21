# 06_DECISIONS_LOG.md — Registro de Decisões de Arquitetura (ADR)

Histórico cronológico e justificado das principais escolhas estruturais e técnicas realizadas no projeto **Casa em Dia**.

---

| ID | Data | Decisão | Motivação | Impacto |
| :--- | :--- | :--- | :--- | :--- |
| **ADR-001** | **20/06/2026** | **Persistência Baseada no Room** | Garantir armazenamento persistente local-first robusto e tipado para manutenções e registros de histórico doméstico/veicular. | Segurança absoluta dos dados do usuário, ausência de dependência de conexões de internet e excelente escalabilidade local. |
| **ADR-002** | **20/06/2026** | **Uso de Notificações Locais via AlarmManager** | Garantir agendamentos determinísticos offline precisos para alertas antecipados sem necessidade de servidores push remotos. | Notificações entregues de forma pontual no dispositivo físico baseadas no relógio local. |
| **ADR-003** | **21/06/2026** | **Extração de Helper de Recorrência e Layouts Canvas** | Limpar os arquivos volumosos de UI (`NewItemScreen`, `Components.kt`) mantendo-os em unidades menores altamente focadas e manuteníveis. | Redução drástica da contagem de linhas de telas para menos de ~300 linhas, desacoplamento de lógica de negócio da camada exclusiva de visualização Compose. |

---

## Referência Cruzada de Documentos
*   Veja o impacto estrutural mapeado na arquitetura técnica em [02_ARCHITECTURE.md](02_ARCHITECTURE.md).
*   Consulte os limites recomendados por tipos de arquivos em [00_PROJECT_RULES.md](00_PROJECT_RULES.md).
