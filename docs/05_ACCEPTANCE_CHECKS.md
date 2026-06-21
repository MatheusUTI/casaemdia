# 05_ACCEPTANCE_CHECKS.md — Critérios de Aceitação Obrigatórios

Use este checklist como guia de teste e verificação obrigatória de regressão antes de aceitar qualquer modificação ou commits de novas funcionalidades.

---

## Checklist Geral de Quality Assurance (QA)

- [ ] **Compilação sem Erros:** O projeto compila integralmente no primeiro build sem erros na sintaxe Kotlin ou dependências Gradle quebradas.
- [ ] **Criar Residência (Casa):** Cadastrar e vincular uma residência principal de forma persistente.
- [ ] **Editar Residência:** Redefinir valores ou dados cadastrais do imóvel associado.
- [ ] **Excluir Residência:** Remover vínculo assegurando que os itens dependentes permaneçam estáveis ou limpos.
- [ ] **Criar Lembrete:** Funcionalidade de criar novos alertas sob as categorias CASA ou CARRO, especificando datas limites reais.
- [ ] **Editar Lembrete:** Alterar propriedades de lembretes ativos (nomes, notas, recorrências, etc) atualizando a tela de detalhes.
- [ ] **Excluir Lembrete:** Excluir fisicamente o registro de atividades pendentes.
- [ ] **Concluir Lembrete:** Marcar item como realizado, transferindo o registro para a tabela histórica.
- [ ] **Restaurar Lembrete:** Desfazer a operação de conclusão através do histórico, reativando a tarefa ativa.
- [ ] **Histórico Funcional:** Listagem organizada chronologicamente contendo data e valor estatístico dos itens concluídos.
- [ ] **Recorrência Matemática:** Verificação da auto-replicação inteligente e projeção futura da próxima manutenção baseada no termo de tempo selecionado.
- [ ] **Notificações em Funcionamento:** Envio de Toasts e agendamentos no sistema local para alertas antecipados.

---

## Checklist de Controle de Crescimento de Arquivos

- [ ] **Nenhum arquivo crítico cresceu sem justificativa:** Lógica mantida concisa e focada.
- [ ] **Arquivos acima do limite sinalizados:** Se aplicável, inserido aviso no bloco `RISKS`.
- [ ] **Responsabilidade única respeitada:** Nova lógica colocada no menor arquivo responsável possível.
- [ ] **Sem misturar responsabilidades:** Nenhuma lógica de UI, dados ou regras de negócio misturadas indevidamente.

---

## Referência Cruzada de Documentos
*   Veja o roteiro passo a passo passo para testar em [10_TEST_CHECKLIST.md](10_TEST_CHECKLIST.md).
*   Consulte os fluxos de dados mapeados por arquivos em [09_FILE_INDEX.md](09_FILE_INDEX.md).
