# AUDIT_TEMPLATE.md — Modelo de Relatório de Auditoria Técnica e Conformidade

Este modelo deve ser preenchido integralmente por engenheiros de qualidade ou auditores automatizados ao final de cada iteração de desenvolvimento ou entrega de tarefas estruturais no projeto **Casa em Dia**. Ele documenta as alterações reais efetuadas, sua consistência com as regras e o estado da arquitetura do aplicativo.

---

# Relatório de Auditoria: [Data da Execução]

## 1. Identificação Geral
- **Data da Auditoria:** DD/MM/AAAA
- **Responsável pela Execução:** [Nome / IA Auditora]
- **Hash/Commit de Referência ou Versão do Applet:** [Identificação]

---

## 2. Inventário de Modificações Físicas

### Arquivos Criados:
* [ ] `caminho/do/novo/Arquivo1.kt` - [Breve descrição do intuito do arquivo]
* [ ] `caminho/do/novo/Arquivo2.kt` - [Breve descrição do intuito do arquivo]

### Arquivos Modificados:
* [ ] `caminho/do/modificado/Arquivo3.kt` - [Qual alteração foi de fato realizada?]
* [ ] `caminho/do/modificado/Arquivo4.kt` - [Qual alteração foi de fato realizada?]

### Arquivos Removidos:
* [ ] `caminho/do/arquivo/Removido.kt` - [Qual a justificativa técnica para exclusão do arquivo?]

---

## 3. Inspeção de Dependências e Bibliotecas Externas

### Dependências Adicionadas:
* [ ] **Biblioteca / Versão:** [Ex: androidx.room:room-ktx:2.6.1]
  * **Justificativa de Engenharia:** [Por que foi necessária para o Casa em Dia?]

### Dependências Removidas:
* [ ] **Biblioteca / Versão:** [Descrição]
  * **Justificativa de Engenharia:** [Descrição]

---

## 4. Cobertura de Verificação e Testes Unitários

### Testes Criados:
* [ ] `caminho/testes/NomeDoArquivoDeTeste.kt`
  * **Cenários Cobertos:** [Ex: Verificação de status OVERDUE para odômetro maior que o planejado no Palio 1.6]

### Cobertura de Regras de Negócio:
* [ ] **Regra 1: Item com data vencida (OVERDUE)** -> [ ] Testado com sucesso
* [ ] **Regra 2: Item vencendo hoje (ATTENTION)** -> [ ] Testado com sucesso
* [ ] **Regra 3: Item dentro da janela de atenção (ATTENTION)** -> [ ] Testado com sucesso
* [ ] **Regra 4: Item fora da janela de atenção (OK)** -> [ ] Testado com sucesso
* [ ] **Regra 5: Item por quilometragem atrasado (OVERDUE)** -> [ ] Testado com sucesso
* [ ] **Regra 6: Item por quilometragem em atenção (ATTENTION)** -> [ ] Testado com sucesso
* [ ] **Regra 7: Item por quilometragem OK (OK)** -> [ ] Testado com sucesso
* [ ] **Regra 8: Criação estruturada de Ativo** -> [ ] Testado com sucesso
* [ ] **Regra 9: Criação parametrizada de Item** -> [ ] Testado com sucesso
* [ ] **Regra 10: Registro correto de Histórico** -> [ ] Testado com sucesso

---

## 5. Rastreamento de Código Morto e Limpeza Técnica
[Identifique qualquer classe, método, import, variável ou arquivo estático que tenha restado na base de código após a finalização da tarefa, sem nenhuma chamada física/referencial ativa.]
- [ ] **Import não utilizado:** [Identificar classes e local]
- [ ] **Função não referenciada:** [Identificar arquivos]
- [ ] **Arquivos órfãos:** [Identificar se há algum]

---

## 6. Problemas Técnicos Identificados (Mapeamento de Bugs)
[Descreva anomalias visuais, bugs de navegação Compose, problemas na restauração de estado, problemas de digitação no odômetro ou inconsistência em datas locais.]
* **Bug 1:** [Ex: O teclado não fecha ao salvar uma nova tarefa residência] - **Severidade:** [Baixa / Média / Alta]
* **Bug 2:** [Ex: Barra inferior oculta botões da lista de tarefas no tablet] - **Severidade:** [Baixa / Média / Alta]

---

## 7. Verificador Geral de Viabilidade de Clientes (Regras de Negócio)

### Violações ao `AI_RULES.md`:
* **Resultado:** [ ] Nenhuma violação detectada / [ ] Violações encontradas (Descrever itens):
  * *Violação:* [Descrever o que quebrou as regras do AI_RULES]

### Violações ao `SDD.md` (Integridade de Fluxos):
* **Resultado:** [ ] Nenhuma violação detectada / [ ] Violações encontradas (Descrever itens):
  * *Violação:* [Ex: A camada de UI instanciou diretamente a entidade de Room para popular um texto]

### Violações ao `PHASE_PLAN.md` (Adiantamento de Escopo):
* **Resultado:** [ ] Nenhuma violação detectada / [ ] Violações encontradas (Descrever itens):
  * *Violação:* [Ex: Foi implementado o esqueleto do WorkManager fora da fase estipulada de lembretes]

---

## 8. Pendências Ativas Técnicas
[Dívidas técnicas que deverão ser sanadas na próxima janela de desenvolvimento ou antes de enviar para homologação definitiva.]
- [ ] Item pendente 1
- [ ] Item pendente 2

---

## 9. Próximos Passos Recomendados
1. [Próxima atividade em fila de engenharia baseada no PHASE_PLAN.md]
2. [Correção dos débitos técnicos elencados nesta auditoria]

---

## 10. Veredito de Qualidade Técnico
- **[ ] APROVADO:** Sem violações estruturais ou bugs críticos de compilação. Pronto para integração.
- **[ ] REPROVADO:** Requer refatoração com correção imediata das violações documentadas antes de nova rodada de auditoria.

### Parecer do Auditor Técnico:
*[Deixe um comentário sintetizado e profissional sobre a maturidade geral do código nesta rodada de entrega]*
