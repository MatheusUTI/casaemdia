# 01_PRODUCT_SPEC.md — Especificação de Alinhamento do Produto e Escopo

Este documento centraliza as metas de produto, escopo autorizado do MVP, fronteiras de desenvolvimento, regras lógicas e limitações do aplicativo **Casa em Dia**. Ele atua como a única fonte da verdade (Single Source of Truth) para o escopo e regras de negócio do produto.

---

## 1. Identidade e Propósito do Produto

O **Casa em Dia** destina-se a organizar e gerenciar manutenções preventivas, prazos de vencimento legais e tarefas programadas de ativos de alto valor sentimental ou financeiro: a **residência** (Casa) e o **veículo** (Carro).

*   **Proposta de Valor:** *"Nunca mais esqueça o que dá prejuízo."*
*   **Meta Central:** Prevenir perdas financeiras por atraso (multas, infrações, invalidação de garantias, danos hidráulicos, perda de seguro etc.).
*   **Abordagem Técnica:** *Local-First* completo (dados salvos estritamente na sandbox do usuário) sem conexão de APIs de nuvem obrigatórias ou autenticação de servidores externos.

---

## 2. Escopo do MVP Autorizado

O MVP compreende os seguintes módulos e funcionalidades:

*   **Módulo Carro:** Detalhes de odômetro, pendências e histórico específico do veículo.
*   **Módulo Casa:** Lembretes de caixa d’água, filtros, reformas e afins.
*   **Arquivo Vivo:** Organização e consulta de anotações (chaves e códigos), notas rápidas e referências locais a documentos.
*   **Linha do Tempo de Alertas (Home):** Painel que organiza tarefas por gravidade cronológica (*Atrasados*, *Hoje*, *Futuros / Próximos 7 dias / Próximos 30 dias*).
*   **Histórico de Manutenção:** Registro cronológico de tarefas concluídas, permitindo desfazer conclusão (restaurar) e excluir.
*   **Notificações Locais:** Alertas antecipados com agendamento via `AlarmManager`.
*   **Backup Geral Local:** Importação e exportação de backups por arquivo JSON (via SAF).

---

## 3. O que o App NÃO é (Limitações Cardinais)

Para preservar o design focado no problema original, o app **NÃO** deve conter as seguintes soluções:

*   Planner genérico, organizador diário de hábitos ou aplicativo voltado a TDAH.
*   Lista avançada de supermercado ou app de controle doméstico de faxina (tarefas diárias repetitivas).
*   Rede social familiar ou compartilhamento em tempo real multifamiliar.
*   Notion simplificado com renderização markdown avançada.
*   Controle financeiro completo ou conciliação bancária de faturamento.

---

## 4. Fora de Escopo do MVP (Fases Futuras)

As seguintes mecânicas estão categoricamente proibidas na versão do MVP atual, devendo ser apresentadas apenas através de interfaces pendentes (TODOs/Mock) se muito necessárias:

1.  Autenticação física (Login, Sign-up, email, SSO ou dados de perfis locais).
2.  Bancos de dados remotos na nuvem e sincronização em segundo plano multi-dispositivo.
3.  Funcionalidades dirigidas por Inteligência Artificial (ex: leitores OCR de manuais, assistentes de conversação de diagnóstico ou chatbots).
4.  Integrações com APIs em tempo real governamentais ou comerciais (ex: DETRAN, tabela FIPE, pagamento direto de taxas corporativas).
5.  Mecânicas de gamificação avançada (avatares mascotes, ranking de usuários, streaks ou pontuações).
6.  Módulo pet avançado ou módulo kids.

*Se algum desses itens parecer necessário, criar TODO explícito e pedir confirmação antes de implementar.*

---

## 5. Modelos e Convenções de Domínio

### Tipos de Ativo Permitidos inicialmente (`AssetType`)

A categorização física dos Ativos restringe-se aos enums oficiais:
*   `CAR` (Carro): Foco em veículo, acompanhado por limites reais de odômetros.
*   `HOME` (Casa): Itens de alvenaria, fiação, encanamento e regras de habitação.
*   `DOCUMENT` (Documentação): Agrupamento de registros de expiração livre de ativo físico.
*   `OTHER` (Outro): Ativos ou utilidades de alto valor secundário (ex: equipamentos domésticos caros).

*Nota: PERSON pode existir residualmente como definição futura de domínio, mas nunca como módulo no MVP.*

### Tipos de Item de Controle (`ControlItemType`)

*   `FIXED_DATE`: Data única de expiração legal sem auto-replicação recorrente (ex: IPTU do ano corrente, data limite do IPVA).
*   `TIME_INTERVAL`: Tarefas recorrentes que ao serem dadas como concluídas criam novas tarefas idênticas projetando o termo correto no futuro (ex: dedetização a cada 6 meses).
*   `MILEAGE`: Prazos comandados pelo avanço do deslocamento físico de odômetro de veículos (ex: troca de correias e fluidos).
*   `INFO`: Registro textual estático para consulta sem qualquer fator de vencimento cronológico.
*   `DOCUMENT`: Associação virtual à indexação de manuais e arquivos locais salvos.

---

## 6. Regras de Negócio e de Status

A lógica do `ControlStatusCalculator` atua como cérebro centralizador destas diretrizes matemáticas:

### Status de Alertas Permitidos:

*   🟢 **`OK` (Em dia):** Atividade com situação operacional regular.
*   🟡 **`ATTENTION` (Atenção):** Itens com risco iminente posicionados dentro da janela configurada pelo usuário.
*   🔴 **`OVERDUE` (Atrasado):** Tarefas cujo teto ou data limite foi ultrapassado.

### Critérios de Cálculo de Status:

1.  **Estado `OVERDUE`:**
    *   Para datas limite: `limitDate` é estritamente anterior à data corrente (`currentDate`).
    *   Para odômetros: quilometragem atual do ativo (`currentMileage`) é maior ou igual à quilometragem prevista (`predictedMileage`).
2.  **Estado `ATTENTION`:**
    *   Para datas limite: data é igual à data corrente, ou a margem positiva restante (`ChronoUnit.DAYS.between(currentDate, limitDate)`) é menor ou igual à janela estipulada em `alertWindowDays` (padrão = 7 dias).
    *   Para odômetros: diferença positiva de distância restante (`predictedMileage` - `currentMileage`) é menor ou igual ao residual configurado em `alertWindowMileage` (padrão = 1.000 km).
3.  **Estado `OK`:**
    *   Quando a tarefa não satisfizer nenhuma das restrições de atraso ou atenção anteriores.

---

## 7. Diretrizes para Dados de Simulação (Dados Demo)

O gerador de preenchimento automático para testes locais e simulações de UI deve usar exclusivamente os seguintes exemplos autorizados (em Português do Brasil):

*   **Palio 1.6 / Hatch:** Veículo de exemplo simulando odômetro de teste.
*   **Minha Casa / Apartamento:** Ativo imobiliário de exemplo.
*   **Troca de Óleo / Filtro de Combustível:** Tarefas do veículo por odômetro.
*   **IPVA / Licenciamento Anual:** Prazos por data fixa.
*   **Renovação de Seguro da Caixa / Seguro Auto:** Prazos recorrentes.
*   **Limpeza do Filtro de Água / Caixa d'Água:** Lembretes recorrentes residenciais.
*   **Garantia da Geladeira:** Documento rápido.

*É expressamente PROIBIDO o uso de dados pessoais reais de usuários ou chaves internas no código de teste ou compilação.*

---

## 8. Referências Cruzadas
*   Para verificar regras de processo e condutas técnicas, verifique [docs/00_PROJECT_RULES.md](00_PROJECT_RULES.md).
*   Para arquitetura técnica integrada, consulte [docs/02_ARCHITECTURE.md](02_ARCHITECTURE.md).
*   Para testabilidade e checklists, verifique [docs/11_TEST_STRATEGY.md](11_TEST_STRATEGY.md) e [docs/10_TEST_CHECKLIST.md](10_TEST_CHECKLIST.md).
