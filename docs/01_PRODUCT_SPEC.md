# 01_PRODUCT_SPEC.md — Especificação do Produto

Este documento descreve as metas de produto, escopo, regras de negócio gerais e limitações acordadas para o aplicativo **Casa em Dia**.

---

## 1. Objetivo do App
O **Casa em Dia** destina-se a organizar e gerenciar manutenções preventivas, prazos de vencimento legais e tarefas programadas de ativos caros ao usuário: sua **residência** (Casa) e/ou **veículo** (Carro), prevenindo prejuízos financeiros por perda de prazos (multas, danos hidráulicos, perda de garantia).

---

## 2. Público-Alvo
*   Proprietários de imóveis ou inquilinos que gerenciam tarefas domésticas periódicas (e.g. limpar caixa d'água, dedetização, renovação de seguro residencial).
*   Proprietários de veículos de uso cotidiano que monitoram revisões preventivas (e.g. troca de óleo, troca de pneus, licenciamento anual do veículo).

---

## 3. Fluxos Principais do App
1.  **Dashboard de Início (Home):** Linha do tempo dinâmica agrupada por gravidade cronológica:
    *   *Atrasados:* Lembretes cuja data limite foi ultrapassada.
    *   *Hoje:* Lembretes que vencem no dia de hoje.
    *   *Futuros:* Lembretes dos próximos 7 dias e próximos 30 dias.
2.  **Módulos (Seções):** Acesso rápido e segmentado aos ativos de veículo e residência.
3.  **Criação/Edição de Lembrete:** Interface intuitiva contendo título, categoria (Carro/Casa), data limite, recorrência (Nenhuma, Mensal, Trimestral, Semestral, Anual) e alerta antecipado (Hoje, 1 dia, 3 dias, 1 semana, 1 mês antes).
4.  **Conclusão de Lembrete:** Marca o item atual como concluído, registra uma entrada correspondente no histórico (para controle estatístico e logs de auditoria), e se o item for recorrente, agenda automaticamente o próximo ciclo.
5.  **Histórico e Arquivo:** Exibe o registro histórico detalhado de todas as tarefas concluídas. Fornece recursos para restaurar (desfazer conclusão) e excluir definitivamente.

---

## 4. Regras de Negócio Fundamentais
*   **Recorrência Real:** Ao concluir um lembrete configurado com recorrência, gera-se uma nova instância ativa sob o mesmo título e tipo, calculando com precisão matemática o novo prazo (e.g. `+1 mês`, `+3 meses`).
*   **Alerta Antecipado com Notificação Local:** Se configurado "1 dia antes", o aplicativo programa um agendamento no sistema Android (`AlarmManager`) para enviar uma notificação local exatamente no período configurado, otimizando o aviso prévio ao usuário.
*   **Controle de Vencimento:** Atividades devem mudar dinamicamente de cor (Vermelho = Atrasado, Amarelo = Atenção / Prósperos, Verde = Em dia) com base na data calculada em relação ao relógio local do sistema operacional.

---

## 5. Limitações de Produto (Fora de Escopo)
*   **Sem Sincronização em Nuvem:** Todos os dados persistidos residem localmente no sandbox do aplicativo de maneira privada.
*   **Sem Modo Multi-Usuário:** Não oferece suporte para compartilhamento em rede ou sincronização de contas familiares.
*   **Sem Inteligência Artificial Integrada:** O aplicativo executa exclusivamente lógicas determinísticas estruturadas locais.

---

## 6. Referência Cruzada de Documentos
*   Veja regras de nomenclatura de arquivo e arquitetura em [02_ARCHITECTURE.md](02_ARCHITECTURE.md).
*   Para acompanhar as regras de compatibilidade do projeto, consulte [00_PROJECT_RULES.md](00_PROJECT_RULES.md).
