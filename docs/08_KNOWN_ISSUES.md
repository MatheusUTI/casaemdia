# 08_KNOWN_ISSUES.md — Registro de Problemas Conhecidos

Relação de gargalos técnicos ou inconsistências identificadas, contendo impacto estrutural e rotas planejadas para correção.

---

## Tabela de Problemas e Mitigações

| Problema | Impacto | Status | Solução Planejada |
| :--- | :--- | :--- | :--- |
| **Aviso de Alarme Exato no Android 13+** | O sistema Android pode recusar agendamentos exatos sem autorização explícita em tempo de execução. | **Tratado via try-catch** | Exibir diálogo amigável instruindo o usuário a habilitar a permissão especial de alarmes exatos nas configurações caso o agendamento retorne exceção. |
| **Limitação de Entrada de Odômetro (Quilometragem)** | Na tela de edição é exibido campo baseado unicamente em dias de vencimento ao invés de controle de odômetro em tempo real por carro. | **Mitigado** | O cálculo cronológico por data limite atende plenamente ao MVP e ciclo CR08. O controle por odômetro será adicionado como extensão do módulo de carro em etapas subsequentes se solicitado. |

---

## Referência Cruzada de Documentos
*   Consulte os riscos gerais mapeados em [03_CURRENT_STATE.md](03_CURRENT_STATE.md).
*   Para acompanhar as regras de estabilidade, leia [00_PROJECT_RULES.md](00_PROJECT_RULES.md).
