# AI_RULES.md — Regras obrigatórias do projeto Casa em Dia

## 1. Identidade do produto

O app se chama Casa em Dia.

Casa em Dia é um app local-first para controlar prazos, manutenções, documentos, garantias e informações importantes da casa e do carro.

O objetivo do app é evitar prejuízo, esquecimento, retrabalho e perda de informações importantes.

## 2. O que o app NÃO é

Este app NÃO é:

- planner genérico
- app de hábitos
- app TDAH
- agenda completa
- app de faxina
- app de tarefas domésticas diárias
- app automotivo para entusiastas
- controle financeiro completo
- rede social familiar
- Notion simplificado

## 3. MVP autorizado

O MVP pode conter apenas:

- Módulo Carro
- Módulo Casa
- Arquivo Vivo
- Linha do Tempo de Alertas
- Histórico de manutenção
- Cadastro rápido de itens
- Banco local
- Notificações locais

## 4. Fora do escopo obrigatório

Não implementar no MVP:

- login
- cadastro de usuário
- nuvem
- sincronização
- multiusuário
- compartilhamento familiar
- IA dentro do app
- chatbot
- APIs externas
- Detran
- Fipe
- pagamento de IPVA
- gamificação
- ranking
- streaks
- mascote
- rede social
- controle financeiro avançado
- lista de compras avançada
- módulo pets
- módulo filhos completo

Se algum desses itens parecer necessário, criar TODO explícito e pedir confirmação antes de implementar.

## 5. Stack obrigatória

- Kotlin
- Jetpack Compose
- Room/SQLite
- MVVM
- Repository Pattern
- Navigation Compose
- WorkManager ou AlarmManager para lembretes locais
- Testes unitários para regras de negócio

## 6. Regras de arquitetura

A IA deve respeitar:

- código simples
- separação clara por camadas
- regra de negócio fora da UI
- cálculo de status isolado e testável
- banco local como fonte da verdade
- telas em português do Brasil
- componentes reutilizáveis quando fizer sentido
- nenhum arquivo gigante desnecessário

## 7. Entidades principais

O projeto deve usar, no mínimo:

- AssetEntity
- ControlItemEntity
- AttachmentEntity
- HistoryEntryEntity

## 8. Tipos de ativo

Tipos permitidos inicialmente:

- CAR
- HOME
- DOCUMENT
- OTHER

PERSON pode existir na arquitetura, mas não deve virar módulo completo no MVP.

## 9. Tipos de item

Tipos permitidos:

- FIXED_DATE
- TIME_INTERVAL
- MILEAGE
- INFO
- DOCUMENT

## 10. Status dos itens

Status permitidos:

- OK
- ATTENTION
- OVERDUE

## 11. Regras de status

OVERDUE:

- data limite anterior à data atual
- ou quilometragem atual maior ou igual à quilometragem prevista

ATTENTION:

- data limite dentro da janela de alerta configurada
- ou quilometragem faltante menor ou igual à janela de alerta por km

OK:

- item não está atrasado
- item não está em atenção

## 12. Regras anti-alucinação

A IA deve seguir obrigatoriamente:

1. Não inventar funcionalidades fora do escopo.
2. Não adicionar bibliotecas externas sem justificar.
3. Não adicionar login, nuvem, IA, APIs externas ou multiusuário.
4. Não alterar arquitetura sem explicar o motivo.
5. Não remover funcionalidade existente sem autorização.
6. Não refatorar arquivos não relacionados à tarefa.
7. Não criar telas que não estejam no MVP.
8. Não criar dados sensíveis reais.
9. Não usar termos em inglês na interface final, salvo nomes técnicos internos.
10. Se faltar informação, criar TODO em vez de inventar.

## 13. Regras de entrega

A cada alteração, a IA deve informar:

- arquivos criados
- arquivos modificados
- regras implementadas
- testes criados
- pendências
- decisões técnicas tomadas

## 14. Testes obrigatórios

Criar testes para:

- item com data vencida
- item vencendo hoje
- item dentro da janela de atenção
- item fora da janela de atenção
- item por quilometragem atrasado
- item por quilometragem em atenção
- item por quilometragem OK
- criação de ativo
- criação de item
- registro de histórico

## 15. Dados demo permitidos

Pode criar dados fictícios para teste:

- Palio
- Minha Casa
- Troca de óleo
- IPVA
- Seguro
- Filtro de água
- Caixa d’água
- Garantia da geladeira

Nenhum dado real pessoal deve ser usado no código.

## Regra de Escopo Incremental

A IA não pode implementar funcionalidades futuras
antes da autorização explícita.

Se uma funcionalidade estiver marcada como
"fase futura", ela deve ser preparada por interfaces
ou TODOs, mas não implementada.

Exemplos:
- Room completo
- WorkManager
- Backup
- Billing
- Cloud Sync
