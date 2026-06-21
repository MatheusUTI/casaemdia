# 03_CURRENT_STATE.md — Estado Atual do Projeto

* Última atualização: 21/06/2026
* Versão lógica do projeto: CR08
* Status geral: Estável / MVP funcional

Este documento detalha o panorama de progresso e as áreas técnicas críticas do projeto **Casa em Dia**.

---

## 1. Funcionalidades Prontas (100% Entregues)

*   **Lembretes Funcionais:** Fluxo completo de criação, visualização e listagem agrupada na tela Home.
*   **Edição e Exclusão:** Operações de CRUD completas e seguras no banco de dados local.
*   **Conclusão e Registro Histórico:** Finalização de lembretes ativos cria nova instância no histórico sob controle absoluto.
*   **Mecanismo de Recorrência Real:** Repetição inteligente de datas (+1 mês, +3 meses, etc.) implementada no DAO ao concluir, com testagem automatizada cobrindo todos os períodos e datas limite de fim de mês.
*   **Notificações Locais (`AlarmManager`):** Agendamento e alertas baseados no parâmetro de proximidade antecipada, totalmente protegidos por suíte de testes Robolectric simulando agendamentos futuros, cancelamentos e prevenção de disparos no passado.
*   **Estado Reativo do ViewModel (`MainViewModel`):** Camada de dados e estados de UI blindados por suíte de testes Robolectric cobrindo ciclo de vida do CRUD, reatividade de StateFlows, agendamento reativo de alarmes e transição para o histórico.
*   **Restauração de Concluídos:** Recurso que reativa lembrete finalizado a partir do histórico sem perda de dados históricos.
*   **Feedback Visual Integrado:** Exibição de Toasts apropriados ao concluir, salvar, editar, restaurar e excluir alertas no app.

---

## 2. Funcionalidades em Andamento

*   *Nenhuma:* Todos os fluxos principais e requisitos solicitados de usabilidade foram concluídos e polidos com sucesso.

---

## 3. Funcionalidades Pendentes

*   *Nenhuma:* O escopo do MVP e do ciclo corrente CR08 encontram-se plenamente maduros e estáveis.

---

## 4. Áreas Estáveis (Baixo Risco)

*   **Camada de Persistência (Room):** Estrutura estável, livre de falhas de concorrência ou conflitos de chaves PK.
*   **Desenho Canvas & Componentes:** Cards e ilustrações customizadas totalmente independentes e performáticas, mantidas no mesmo pacote.

---

## 5. Áreas Sensíveis e Riscos Conhecidos

*   **Agendamento de Alarmes e Permissões:** Em aparelhos modernos (Android 13+), o canal de notificação requer permissão de notificações ativa e autorização para agendamento de alarmes exatos (`SCHEDULE_EXACT_ALARM`). O código do app está robustamente estruturado com tratamento `try-catch` para evitar travamentos caso essas permissões não estejam autorizadas.

---

## 6. Referência Cruzada de Documentos
*   Veja o que está previsto na fila imediata de desenvolvimento ou handoff em [07_HANDOFF.md](07_HANDOFF.md).
*   Para verificar possíveis correções ou mitigações aplicadas, consulte [08_KNOWN_ISSUES.md](08_KNOWN_ISSUES.md).
*   Consulte a estratégia de testes oficial unificada em [11_TEST_STRATEGY.md](11_TEST_STRATEGY.md).
