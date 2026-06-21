# docs/START_HERE.md — Ponto de Entrada Universal para IAs

Bem-vinda, IA! Este é o ponto de entrada oficial e definitivo do projeto **Casa em Dia**. Ele foi elaborado para fornecer o contexto necessário de forma imediata e estruturada, permitindo que novas sessões sejam iniciadas de forma produtiva utilizando apenas o comando básico: *"Leia docs/START_HERE.md"*.

Este projeto é **100% Compatível com o Paradigma AISDD (AI System Design Document)**. Para especificações estruturais de alinhamento, níveis PML cadastrados e governança avançada, consulte o arquivo principal **[docs/AISDD_PROJECT.md](AISDD_PROJECT.md)**.

---

## 1. Objetivo do Documento

Centralizar e guiar a leitura inicial de qualquer agente de inteligência artificial de forma otimizada. Ao seguir este guia, a IA compreenderá os limites do escopo, regras de arquitetura, práticas de teste e organização de arquivos, reduzindo o risco de regressões e mantendo a coesão técnica do sistema.

---

## 2. Ordem Mínima Obrigatória de Leitura

Antes de realizar qualquer análise de código ou modificação estrutural, você **DEVE** ler os seguintes arquivos na ordem especificada:

1.  **[docs/00_PROJECT_RULES.md](00_PROJECT_RULES.md):** Contém as diretrizes estritas do projeto, formato de resposta obrigatório do agente, limites de crescimento de arquivos e limites operacionais.
2.  **[docs/07_HANDOFF.md](07_HANDOFF.md):** Apresenta o estado técnico atual das implementações, decisões de design cruciais tomadas recentemente e as próximas tarefas prioritárias.
3.  **[docs/09_FILE_INDEX.md](09_FILE_INDEX.md):** Funciona como o mapa estrutural detalhado do projeto, detalhando a responsabilidade de cada diretório e arquivo crítico.

---

## 3. Leitura Complementar por Cenário

A depender da tarefa solicitada pelo usuário, certifique-se de carregar os arquivos adicionais necessários listados abaixo:

### Nova Funcionalidade / Expansão de Escopo
Se você for criar ou expandir características do produto:
*   **[docs/01_PRODUCT_SPEC.md](01_PRODUCT_SPEC.md):** Descrição das metas de negócio, do comportamento das funcionalidades atuais e das expectativas de UX/UI.
*   **[docs/02_ARCHITECTURE.md](02_ARCHITECTURE.md):** Mapa e visão de fluxo técnico do Casa em Dia.

### Alteração em Arquivos Críticos / Correção de Bugs
Se você for mexer em controllers, modelos reativos ou fluxos de dados principais:
*   **[docs/03_CURRENT_STATE.md](03_CURRENT_STATE.md):** Estado atual da modelagem de dados, progresso funcional e alinhamento de infraestrutura.
*   **[docs/05_ACCEPTANCE_CHECKS.md](05_ACCEPTANCE_CHECKS.md):** Checklist estrito de garantias e critérios de aceitação para homologar modificações.

### Mudança Arquitetural / Histórico de Decisões
Se for preciso alterar o banco de dados Room, gerenciador de alarmes ou a fundação tecnológica do projeto:
*   **[docs/06_DECISIONS_LOG.md](06_DECISIONS_LOG.md):** Registro cronológico de decisões técnicas tomadas (ADRs).

### Implementação / Execução de Testes
Se a tarefa envolver qualidade, testes manuais ou automatizados:
*   **[docs/10_TEST_CHECKLIST.md](10_TEST_CHECKLIST.md):** Roteiro passo a passo de testes manuais integrados ponta a ponta.
*   **[docs/11_TEST_STRATEGY.md](11_TEST_STRATEGY.md):** Pirâmide oficial de testes, cobertura existente, padrões e diretrizes para novas suítes.

---

## 4. Fluxo Padrão de Trabalho

Para garantir consistência nas interações, adote sempre o seguinte ciclo de trabalho:
1.  **Leitura Contextual:** Leia os arquivos obrigatórios e complementares correspondentes ao cenário.
2.  **Passo Único:** Execute apenas uma tarefa ou objetivo claro por ciclo iterativo para evitar sobrecarga de contexto.
3.  **Mínima Mudança Viável:** Altere estritamente o necessário. Não faça refatorações cosméticas agressivas ou não solicitadas.
4.  **Validação Rígida:** Verifique a integridade compilando o projeto e executando os testes automatizados locais.
5.  **Atualização de Documentos:** Documente impactos específicos e atualize a seção de entregas no estado atual e checklist técnicos, se aplicável.
6.  **Handoff de Fechamento:** Atualize o handoff rápido para que o próximo agente ou sessão continue sem atrito.

---

## 5. Regras Essenciais Resumidas (CARDINAIS)

*   **Preservação Funcional Absoluta:** Nunca mude o comportamento esperado de UI ou regras existentes sem requisição explícita.
*   **Sem Alteração Descuidada de Stack:** Kotlin, Jetpack Compose, Room Datastore e Robolectric para testes locais JVM compõem a fundação impermeável do projeto.
*   **Controle de Crescimento de Arquivos:** Respeite rigorosamente os limites estipulados de linhas por tipo de arquivo para evitar "God Objects". Se algum arquivo se aproximar do limite recomendado (Telas Compose: 300, ViewModels: 400), sinalize obrigatoriamente sob o bloco de riscos.
*   **Separação Estrita de Responsabilidades:** Nunca misture regras de negócio ou de dados com lógica visual. Use helpers determinísticos isolados para testagem limpa.
*   **Sem Premissas Sem Rigor:** Se faltar qualquer detalhe crítico, dados não definidos estruturalmente ou objetivos ambíguos na tarefa atual, responda imediatamente apenas com `MISSING INFO` com as dúvidas apropriadas e não realize alterações de código.

---

## 6. Como o Usuário Escreve Prompts Curtos e Eficientes

Você incentiva o usuário a instanciar tarefas usando roteiros curtos, pois sabe interpretar as bases do projeto diretamente. Exemplos recomendados:

### Exemplo 1: Nova feature
```text
Leia docs/START_HERE.md.

Objetivo:
Implementar busca em tempo real nos logs históricos.

Critérios:
- Caixa de entrada no topo da BentoArchiveScreen;
- Ignorar maiúsculas/minúsculas e acentos.
```

### Exemplo 2: Correção de bug localizado
```text
Leia docs/START_HERE.md.

Objetivo:
Corrigir crash ao agendar alarmes recorrentes no dia do vencimento quando a proximidade é < 1 dia.
```

---

## 7. Formato Obrigatório de Resposta

O formato estrito acordado para todas as respostas deste projeto é o seguinte:

```markdown
## Objective
[Descrições concisas e focadas do objetivo principal da sua contribuição atual]

## Files changed
*   [Arquivo 1] - [Ação resumida]
*   [Arquivo 2] - [Ação resumida]

## Contract impact
[Impactos estruturais de assinaturas de interfaces, base Room ou comportamentos públicos do app]

## FACTS
*   [Fato inequívoco observado no codebase]
*   [Fato inequívoco de execução ou compilação]

## ASSUMPTIONS
*   [Premissas lógicas adotadas por falta de escopo ou design detalhado]

## UNKNOWNS
*   [Dúvidas pendentes que podem guiar passos futuros ou refinamentos]

## RISKS
*   [Avaliações de riscos ou de crescimento de arquivos como FILE GROWTH RISK: <arquivo>]

## Acceptance status
- [ ] [Critério de aceite 1 do escopo]
- [ ] [Critério de aceite 2 do escopo]

## Updated handoff
*   **Estado Atual:** [Resumo rápido aproximado do progresso atual]
*   **Decisões Importantes:** [Decisões tomadas nesta rodada]
*   **Arquivos Críticos:** [Principais arquivos ativos do ecossistema técnico]
*   **Próxima Tarefa:** [Ação prioritária seguinte recomendada]
*   **Risco Principal:** [Riscos associados ao andamento técnico do projeto]
```

---

## 8. Referência Cruzada de Documentos

*   [docs/AISDD_PROJECT.md](AISDD_PROJECT.md)
*   [docs/00_PROJECT_RULES.md](00_PROJECT_RULES.md)
*   [docs/01_PRODUCT_SPEC.md](01_PRODUCT_SPEC.md)
*   [docs/02_ARCHITECTURE.md](02_ARCHITECTURE.md)
*   [docs/03_CURRENT_STATE.md](03_CURRENT_STATE.md)
*   [docs/05_ACCEPTANCE_CHECKS.md](05_ACCEPTANCE_CHECKS.md)
*   [docs/06_DECISIONS_LOG.md](06_DECISIONS_LOG.md)
*   [docs/07_HANDOFF.md](07_HANDOFF.md)
*   [docs/08_KNOWN_ISSUES.md](08_KNOWN_ISSUES.md)
*   [docs/09_FILE_INDEX.md](09_FILE_INDEX.md)
*   [docs/10_TEST_CHECKLIST.md](10_TEST_CHECKLIST.md)
*   [docs/11_TEST_STRATEGY.md](11_TEST_STRATEGY.md)
