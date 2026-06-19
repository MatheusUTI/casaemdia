# CHANGE_REQUEST_TEMPLATE.md — Modelo de Solicitação de Alteração

Este modelo serve como o protocolo oficial para propor modificações de recursos, correções de bugs complexos ou adições de requisitos técnicos na base de código do aplicativo **Casa em Dia**. Ele garante que toda alteração proposta venha respaldada por justificativas de engenharia e conformidade com as fases de produto.

---

# Proposta de Alteração: [Título Curto e Descritivo]

## 1. Identificação Geral
- **Data da Solicitação:** DD/MM/AAAA
- **Fase de Destino (PHASE_PLAN.md):** [Ex: Fase 2 - Room Integration]
- **Requisitante:** [Nome / Papel]

---

## 2. Objetivo Principal
[Descreva de forma clara e objetiva o que esta alteração visa alcançar. Qual problema real enfrentado pelo usuário do Casa em Dia será solucionado com esta modificação?]

---

## 3. Justificativa de Negócio / Técnica
[Por que esta mudança é importante agora? Por que ela não pode ser postergada para fases futuras? Como ela se conecta à promessa de valor principal de "nunca esquecer o que dá prejuízo"?]

---

## 4. Escopo da Alteração (Inclusões)
[Liste em tópicos fechados tudo o que SERÁ de fato alterado ou criado por esta solicitação. Lembre-se de manter o escopo enxuto, direto e aderente ao MVP.]
- [ ] Item de escopo 1
- [ ] Item de escopo 2
- [ ] Item de escopo 3

---

## 5. Fora do Escopo (Exclusões Explícitas)
[Defina de forma inequívoca o que esta mudança NÃO fará. Isso impede que ramificações secundárias contaminem o desenvolvimento primário durante o ciclo de fixação.]
* **Exclusão 1:** [Ex: Não integraremos nenhuma API de terceiros]
* **Exclusão 2:** [Ex: Não será criada nenhuma tela adicional ou abas novas de navegação]

---

## 6. Arquivos que Serão Afetados (Mapeamento Prévio)
[Identifique de antemão quais arquivos serão introduzidos ou sofrerão modificações físicas.]
* **Novos Arquivos:**
  - `caminho/do/novo/Arquivo.kt`
* **Arquivos Existentes Modificados:**
  - `caminho/do/arquivo/Existente.kt`

---

## 7. Impacto Arquitetural Planejado
[Descreva se haverá mudanças nas assinaturas de banco de dados, modificação de entidades de domínio, acoplamentos estruturais nas ViewModels ou alteração no grafo da navegação principal.]
- **Impacto em Persistência:** [Ex: Inserção de uma coluna X na tabela Y]
- **Impacto em Lógica de Negócio:** [Ex: Extensão do ControlStatusCalculator para lidar com condição Z]
- **Impacto em UI:** [Ex: Um novo componente na tela de Detalhes]

---

## 8. Critérios de Aceite da Entrega
[Defina métricas inequívocas que determinarão se a alteração foi bem-sucedida ou não. O aceite técnico requer validação em compilação.]
1. [ ] O aplicativo deve compilar sem mensagens de erro em logs (`compile_applet`).
2. [ ] [Critério funcional de comportamento na interface do usuário].
3. [ ] Todos os testes unitários afetados ou criados devem passar com sucesso (`gradle :app:testDebugUnitTest`).

---

## 9. Lista de Verificação Obrigatória — Checklist HARNESS

Para prosseguir, o arquiteto executor deve responder ao checklist abaixo, justificando se houver desvios planejados.

| Critério de Verificação | Análise / Justificativa | Conformidade? (OK) |
|---|---|---|
| **1. Viola o `AI_RULES.md`?** | [Descreva brevemente] | **[ ] SIM / [ ] NÃO** |
| **2. Viola diretrizes do `SDD.md`?** | [Descreva brevemente] | **[ ] SIM / [ ] NÃO** |
| **3. Pertence à fase ativa do `PHASE_PLAN.md`?** | [Descreva brevemente] | **[ ] SIM / [ ] NÃO** |
| **4. Adiciona dependência externa nova?** | [Descreva brevemente] | **[ ] SIM / [ ] NÃO** |
| **5. Cria lógicas ou código morto no applet?** | [Descreva brevemente] | **[ ] SIM / [ ] NÃO** |
| **6. Acopla de forma direta UI e Banco?** | [Descreva brevemente] | **[ ] SIM / [ ] NÃO** |
| **7. Cria regra de negócio dentro do Compose?** | [Descreva brevemente] | **[ ] SIM / [ ] NÃO** |
| **8. Expande arquivos além dos limites toleráveis?** | [Descreva brevemente] | **[ ] SIM / [ ] NÃO** |
| **9. Gera fragilidade na navegação estrutural?** | [Descreva brevemente] | **[ ] SIM / [ ] NÃO** |
| **10. Contorna o `ControlStatusCalculator`?** | [Descreva brevemente] | **[ ] SIM / [ ] NÃO** |
| **11. Duplica modelos de dados sem mappers?** | [Descreva brevemente] | **[ ] SIM / [ ] NÃO** |
| **12. Adiciona dados fora das 4 entidades do MVP?** | [Descreva brevemente] | **[ ] SIM / [ ] NÃO** |

> **Atenção:** Em conformidade com o `HARNESS.md`, caso ocorra resposta **"SIM"** para qualquer uma das perguntas acima sem anuência ou justificativa extrema aprovada previamente pelo engenheiro principal, a proposta será rejeitada de forma automática, e a implementação não poderá ser iniciada.

---

## 10. Assinatura e Liberação para Implementação
- **Aprovação Técnica:** [ ] Sim / [ ] Não
- **Responsável Legal:** [Nome / Assinatura do Proprietário do Produto]
- **Data de Liberação:** DD/MM/AAAA
