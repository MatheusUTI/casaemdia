# HARNESS.md — Cabresto de Qualidade e Validação Arquitetural

Este guia estabelece o **Cinto de Segurança de Engenharia** (Validation Harness) oficial para o aplicativo **Casa em Dia**. Qualquer modificação de código, refatoração ou adição de novas lógicas deve passar obrigatoriamente por esta matriz de perguntas estruturadas antes de ser consolidada no repositório.

---

## Estrutura de Avaliação Crítica

Antes de iniciar qualquer edição física de arquivos no projeto Casa em Dia, responda e verifique mentalmente ou por escrito os 12 critérios listados abaixo.

---

## A Matriz de Decisão Rigorosa

| # | Pergunta de Conformidade | Consequência / Por quê? | Avaliação |
|---|--------------------------|-------------------------|-----------|
| **1** | A alteração planejada viola alguma diretriz de regras descrita em **`docs/00_PROJECT_RULES.md`** ou do produto em **`docs/01_PRODUCT_SPEC.md`**? | Os documentos de regras e produto delimitam o posicionamento do app, escopo autorizado do MVP e restrições anti-alucinação. | **[SIM / NÃO]** |
| **2** | A alteração planejada viola algum dos fluxos de dados ou padrões definidos em **`docs/02_ARCHITECTURE.md`**? | O documento de arquitetura rege o fluxo unilinear de comunicação obrigatório de dados (`UI` ↔ `ViewModel` ↔ `Repository` ↔ `Mappers` ↔ `Room`). | **[SIM / NÃO]** |
| **3** | A funcionalidade planejada pertence à **fase de desenvolvimento ativa** descrita no **`PHASE_PLAN.md`**? | Não implementar itens de fases futuras de forma prematura. Pular etapas gera instabilidade estrutural e excesso de lógicas mortas ou parciais. | **[SIM / NÃO]** |
| **4** | A implementação necessita adicionar ou atualizar qualquer **dependência ou biblioteca externa**? | Novas bibliotecas incham o APK final, estendem o tempo de build, e criam passivos de obsolescência técnica. Qualquer justificativa para biblioteca nova deve ser formalizada. | **[SIM / NÃO]** |
| **5** | O código gerado introduzirá lógicas redundantes, **código morto**, funções sem utilidade explícita ou fluxos que o usuário não pode acessar? | Código não acionado é uma fonte de confusão, vazamento de depuração e peso inútil nas distribuições de produção. | **[SIM / NÃO]** |
| **6** | A implementação cria um **acoplamento indevido ou direto** entre a camada de Visualização (`UI`) e a camada de Persistência (`Room/Banco`)? | As telas não devem fazer consultas JDBC/Room diretas, possuir dependências das lógicas de anotação de tabelas, ou orquestrar fluxo de coroutines nítidas de gravação sem passar pelas ViewModels. | **[SIM / NÃO]** |
| **7** | A alteração insere **regras de negócio**, cálculos de prazo, alertas ou estatísticas diretamente na interface de layout (`Jetpack Compose`)? | Cálculos de data, diferença de quilômetros de odômetros e status pertencem estritamente às camadas de lógica isoladas de apresentação ou de domínio puro (`ControlStatusCalculator`). | **[SIM / NÃO]** |
| **8** | O arquivo alterado ou criado se tornará **excessivamente grande e inchado** (mais de 1.500 linhas de código acumulado em um único escopo)? | Arquivos maciços violam o princípio da responsabilidade única. Se um arquivo começar a inchar, é obrigatório fatiá-lo em arquivos menores focados em responsabilidades elementares. | **[SIM / NÃO]** |
| **9** | A alteração introduz padrões de **navegação insegura** ou falhas de ciclo de vida (ex: passando IDs inseguros em formato texto sem serialização)? | O sistema deve usar o Navigation Compose com rotas seguras e organizadas no controle de escopo das telas para manter estabilidade absoluta de navegação. | **[SIM / NÃO]** |
| **10** | A funcionalidade planeja orquestrar e decidir o **status de vencimento (OK, ATTENTION, OVERDUE)** de forma descentralizada e fora do `ControlStatusCalculator`? | O `ControlStatusCalculator` é o cérebro único do aplicativo. Qualquer modificação ou interpretação sobre o que está atrasado ou próximo de vencer na UI centraliza-se nesta classe de forma testável. | **[SIM / NÃO]** |
| **11** | A alteração introduz **duplicação de modelos de dados** (ex: usando classes de banco diretamente nas telas sem passar pelo mapeamento de Domínio)? | Cada padrão e camada deve conversar usando seu modelo específico (Modelos de Entidade de Room para o banco; Modelos de Domínio Puros para a UI). | **[SIM / NÃO]** |
| **12** | O plano cria tabelas no Room ou armazena arquivos **fora das 5 entidades arquiteturais previstas** no escopo principal do MVP? | O escopo físico do banco restringe-se às tabelas e entidades Room: `MaintenanceItem`, `AppCode`, `AppNote`, `DocumentItem` e `HistoryEntryEntity`. | **[SIM / NÃO]** |

---

## 🛑 O Gatilho do Freio de Mão Arquitetural

Se a sua resposta para **QUALQUER UMA** das 12 perguntas acima for **SIM**:

> ### 🚫 **PARAR A IMPLEMENTAÇÃO IMEDIATAMENTE!**
> 
> Você deve pausar o desenvolvimento físico do código do applet e:
> 1. Revisar o design conceitual planejado para remover a violação identificada.
> 2. Redesenhar a abordagem para buscar conformidade total de layout e lógica.
> 3. Se a modificação for de fato justificada por alteração de requisitos do negócio, deve-se primeiro atualizar os documentos de governança (`docs/00_PROJECT_RULES.md` e `docs/02_ARCHITECTURE.md`) recebendo anuência do engenheiro principal antes de alterar uma única linha de código fonte.

---

## Mecânica de Verificação Contínua (Em CI/CD ou Desenvolvimento)

Este cabresto deve ser preenchido de forma descritiva e honesta em cada submissão futura de código via ferramenta de controle de versão ou interações com a IA, servindo como uma garantia imutável de que a integridade física de engenharia do applet "Casa em Dia" continuará pura, sem slop e estável durante toda a vida longa do projeto.
