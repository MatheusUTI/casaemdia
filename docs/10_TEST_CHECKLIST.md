# 10_TEST_CHECKLIST.md — Checklist de Teste de Regressão Rápido

Guia simplificado para verificação funcional rotineira, contendo ações práticas e seus respectivos comportamentos ideais previstos.

---

## Tabela de Testes Locais

| Ação | Resultado Esperado |
| :--- | :--- |
| **Digitar nome vazio, longo ou notas longas e salvar** | Campo de título exibe mensagem de erro se vazio/superior a 50 chars. Campo de descrição (notas) exibe erro se superior a 200 chars. Contadores são atualizados dinamicamente. O botão de salvar permanece desabilitado se houver erros de validação. |
| **Marcar lembrete ativo como completo** | O item desaparece do painel inicial ativamente, gera um Toast de confirmação visual, e uma nova entrada correspondente aparece no Histórico de forma íntegra. |
| **Concluir item configurado como Trimestral** | O item atual é arquivado, e um novo lembrete clonado idêntico com data limite de exata proximidade futura para `+3 meses` é criado em segundo plano de forma instantânea. |
| **Excluir atividade concluída do histórico** | Registro histórico é deletado definitivamente sem afetar outras instâncias ativas ou bancos concorrentes. |
| **Restaurar item concluído de volta à ativa** | A tarefa reaparece com sucesso na lista de início e o registro de auditoria é limpo de maneira limpa. |
| **Executar testes automatizados de recorrência** | Todos os testes em `RecurrenceHelperTest.kt` passam de forma determinística cobrindo o fim de mês e períodos. |
| **Executar testes do agendador de notificações** | Todos os testes em `NotificationSchedulerTest.kt` passam garantindo agendamento futuro, cancelamento e integridade do receptor de alarmes sem crashes. |
| **Executar testes do MainViewModel** | Todos os testes em `MainViewModelTest.kt` passam garantindo CRUD correto, integridade do histórico, reatividade de StateFlows, recorrência e agendamento de alarmes integrados. |
| **Compilar projeto via CLI** | Build finaliza com `:app:assembleDebug` bem-sucedido sem erros ou warnings limitantes. |

---

## Referência Cruzada de Documentos
*   Consulte a estratégia de testes oficial unificada em [11_TEST_STRATEGY.md](11_TEST_STRATEGY.md).
*   Consulte os critérios globais de testes em [00_PROJECT_RULES.md](00_PROJECT_RULES.md).
*   Verifique a lista de verificação obrigatória para entrega em [05_ACCEPTANCE_CHECKS.md](05_ACCEPTANCE_CHECKS.md).
