# SDD.md — Especificação de Design de Sistema (System Design Document)

Este documento atua como a especificação técnica oficial e definitiva do projeto **Casa em Dia**. Ele descreve a arquitetura de referência, estruturas de dados, fluxos de execução e as restrições que devem ser rigorosamente respeitadas em qualquer desenvolvimento subsequente.

---

## 1. Visão Geral do Produto

### Propósito do App
O **Casa em Dia** é um aplicativo móvel estruturado sob a filosofia *local-first*, concebido para centralizar e simplificar o controle de prazos críticos, manutenções obrigatórias, documentos essenciais e período de garantias de ativos materiais fundamentais: a habitação (casa) e o veículo (carro).

### Público-Alvo
Proprietários de imóveis, inquilinos, motoristas de uso cotidiano e chefes de família que necessitam gerenciar múltiplos prazos administrativos, residenciais e veiculares, mas preferem a privacidade de dados locais ao preenchimento de planilhas complexas ou plataformas baseadas na nuvem.

### Proposta de Valor
**"Nunca mais esqueça o que dá prejuízo."**
Através de uma interface limpa, intuitiva e preditiva, o aplicativo consolida em um painel único de alertas e notificações os prazos que, se perdidos, acarretam custos financeiros reais, danos graves à infraestrutura ou problemas legais (ex: multas de trânsito por licenciamento vencido, danos hidráulicos por caixa d'água suja, invalidação de garantias de eletrodomésticos).

---

## 2. Objetivos do MVP

O aplicativo deve prover, de forma totalmente funcional offline, uma experiência limpa para:
- **Gerenciamento de Ativos (Assets):** Cadastrar e visualizar veículos (Carro) e residências (Casa).
- **Gerenciamento de Controles (ControlItems):** Associar a cada ativo itens de controle com prazos temporais, intervalos de tempo recorrentes ou limites de quilometragem.
- **Visualização de Alertas Consolidada:** Um painel de entrada ("Início") que organiza dinamicamente pendências e itens que chamam a atenção por proximidade de vencimento.
- **Histórico de Execuções e Manutenções:** Registrar ações realizadas (ex: "Troca de óleo efetuada", "IPTU Pago") armazenando a data e o custo financeiro para fins estatísticos simples.
- **Suporte a Arquivo Vivo:** Mecanismo básico de armazenamento de anotações, chaves, códigos ou identificadores estáticos de documentos.

---

## 3. Fora do Escopo do MVP

Estão categoricamente vedadas as seguintes categorias de soluções para a versão MVP (as quais deverão permanecer estritamente como diretrizes para fases futuras):
- Autenticação remota ou local de usuários (Login, SSO, Email/Senha).
- Sincronização em nuvem, bancos de dados remotos ou suporte multiusuário com compartilhamento familiar.
- Funcionalidades guiadas por Inteligência Artificial (ex: leitores automáticos de manuais de instrução, assistentes de conversação de diagnóstico de defeitos, chatbot).
- Integração em tempo real com APIs governamentais (ex: busca na base do DETRAN, consulta FIPE, pagamento direto de IPVA dentro do app).
- Mecânicas de gamificação (streaks, pontos, medalhas, avatares mascotes).
- Controles financeiros avançados (fluxo de caixa geral, gráficos complexos de gastos familiares, orçamentos, conciliação bancária).
- Módulos paralelos para gestão de Pets ou de Filhos (estes escopos secundários exigem aprovação e design de negócio dedicados).

---

## 4. Arquitetura de Referência

O aplicativo segue os padrões de arquitetura recomendados pela Google, aplicando separação rígida de responsabilidades através do padrão **MVVM (Model-View-ViewModel)** com camadas limpas de dados e domínio.

### Fluxo de Dados Obrigatório

```
  [ UI / Jetpack Compose ] 
            ↓↑ (UI State & Events)
  [ ViewModel (Jetpack ViewModel + StateFlow) ]
            ↓↑ (Domain Models / Flows)
  [ Repository Layer (Repository Interface) ]
            ↓↑ (Mappers: Entity ↔ Domain Model)
  [ Data Sources / Room Local DB ]
```

### Detalhe das Camadas

1. **Camada de UI (Telas e Componentes):**
   - Escrita inteiramente em **Jetpack Compose** seguindo os princípios visuais do Material Design 3 (M3).
   - Deve ser puramente declarativa.
   - **É terminantemente PROIBIDO** realizar consultas de banco de dados diretamente nela, bem como efetuar cálculos matemáticos de dias ou de vencimentos de quilometragem. Ela consome dados pré-formatados através do `ViewModel` expostos via `StateFlow`.

2. **Camada de ViewModel:**
   - Atua como orquestradora de estado de tela. 
   - Recebe eventos da UI e interage com os repositórios obtendo e emitindo os dados processados do domínio.
   - Converte fluxos reativos do banco de dados expostos pelo repositório em estados imutáveis seguros de visualização.

3. **Camada de Repositório (Repository):**
   - Centraliza o acesso de dados de forma abstrata.
   - Fornece dados limpos baseados nas **Entidades de Domínio**.
   - Trata as operações assíncronas do banco de dados (Room via coroutines/flows) convertendo e aplicando regras de mapeamento antes de enviar para as camadas superiores.

4. **Camada de Mapeamento (Mapper):**
   - Conversores puros e isolados que transformam classes persistentes (`Room Entity` terminadas com o sufixo `Entity`) em modelos simples de negócio (`Domain Model`) e vice-versa.
   - Garante que a lógica do banco de dados local (anotações de tabelas, chaves primárias autogeradas) não contamine a regra de negócios.

5. **Camada de Persistência (Room Local Database):**
   - SQLite encapsulado pelo Room do Android Jetpack.
   - Fonte da verdade única.
   - Apenas os DAOs (DataAccessObjects) interagem com os arquivos fisicamente persistidos.

6. **Camada de Lógica Isenta (Status Calculator):**
   - A classe `ControlStatusCalculator` é um **Singleton de Domínio puro** (Kotlin object). Ela não possui dependência com bibliotecas de UI da plataforma Android (ex: Compose ou views) ou classes internas do Room.
   - Centraliza os critérios matemáticos para calcular e projetar se uma tarefa está em dia, próxima de vencer ou vencida.

---

## 5. Modelos de Domínio

Estes são os tipos imutáveis e puramente Kotlin utilizados nas regras de domínio do sistema:

### 1. Asset (Ativo)
Representa um ativo físico de valor monitorado pelo proprietário.
- `id`: `String` (UUID único gerado em tempo de inserção)
- `name`: `String` (Nome descritivo amigável, ex: "Palio 1.6", "Minha Casa")
- `type`: `AssetType` (Enumeração restringindo os domínios permitidos)
- `description`: `String?` (Comentários ou anotações livres opcionais)
- `identifier`: `String?` (Elemento físico identificador exclusivo do ativo, ex: placa do veículo BRA2E19, matrícula do imóvel)

### 2. ControlItem (Item de Controle)
Representa uma tarefa, vencimento legal ou checklist periódico associado a um Ativo.
- `id`: `String` (UUID do item)
- `assetId`: `String` (UUID do Ativo de ligação)
- `title`: `String` (Descrição breve, ex: "Troca de Óleo", "IPTU")
- `type`: `ControlItemType` (Fórmulas para validação de status)
- `status`: `ControlStatus` (Estado atual dinâmico: OK, ATTENTION ou OVERDUE)
- `limitDate`: `LocalDate?` (Data do limite legal para itens por tempo)
- `alertWindowDays`: `Int` (Margem de segurança configurada pelo usuário para chamar a atenção, padrão = 7 dias)
- `predictedMileage`: `Int?` (Odômetro projetado para itens baseados em distância, ex: 50.000 km)
- `alertWindowMileage`: `Int?` (Margem em quilômetros para acender atenção, padrão = 1.000 km)
- `currentMileage`: `Int?` (Registro do odômetro no momento da última atualização do Ativo ou Item)
- `notes`: `String?` (Observações parciais)

### 3. Attachment (Anexo)
Dados lógicos indexadores de documentos ou fotos representativos do ativo.
- `id`: `String`
- `itemId`: `String`
- `name`: `String`
- `type`: `String` (Extensão ou MIME-type)
- `size`: `String` (Ex: "1.2 MB")
- `uri`: `String` (Referência local ao arquivo salvo no Sandbox interno do app)

### 4. HistoryEntry (Registro de Histórico)
Entrada estática que representa a realização de uma atividade.
- `id`: `String`
- `itemId`: `String`
- `date`: `LocalDate` (Dia em que a manutenção ou pagamento foi finalizado)
- `title`: `String` (Ex: "Filtro e óleo trocados")
- `cost`: `Double?` (Armazena valor investido opcionalmente, para planejamento de custo básico)
- `notes`: `String?` (Observações livres, ex: "Marca Selênia 5W30")

---

## 6. Tipos de Ativos (Enums de Referência)

Os tipos de Ativos permitidos estão restritos ao domínio em:
- **`CAR` (Carro):** Direcionado a controle veicular (Odômetros, Revisões, Seguro Automotivo, etc.).
- **`HOME` (Casa):** Direcionado à manutenção predial civil e elétrica (Caixa d'água, ar-condicionado, dedetização).
- **`DOCUMENT` (Documento):** Aglomerado de controle de vencimento estático livre de ativo físico.
- **`OTHER` (Outro):** Ativos customizados de baixo impacto secundário (ex: aparelhos eletrônicos de valor alto).

---

## 7. Tipos de Controles (Enums de Referência)

- **`FIXED_DATE`:** Itens que possuem uma única data fixa pré-definida de vencimento e não se repetem recorrentemente após concluídos (ex: data limite de IPVA).
- **`TIME_INTERVAL`:** Tarefas recorrentes indexadas por datas que nascem novamente após o registro da finalização adicionando um tempo futuro determinado (ex: limpeza de caixa d'água a cada 6 meses).
- **`MILEAGE`:** Prazos ditados pelo deslocamento físico do veículo expressados no odômetro (ex: troca de correia dentada, troca de pastilha de freio).
- **`INFO`:** Entrada persistente de informação crítica sem vencimento fixo cadastrada para fins de consulta expressa rápida (ex: tamanho dos pneus reserva, número de registro da distribuidora de água).
- **`DOCUMENT`:** Registros de arquivos estáticos indexados (ex: apólice de seguro digitalizada).

---

## 8. Status Permitidos

- **`OK` (Em Dia):** Atividades e itens que estão seguros dentro do prazo operacional normal. Representados no design pela cor verde.
- **`ATTENTION` (Atenção):** Itens com risco iminente de atraso, posicionados dentro dos patamares de alerta de distância ou tempo. Representados pela cor amarelo/laranja.
- **`OVERDUE` (Atrasado):** Itens já vencidos e cujo prazo operacional padrão foi ultrapassado. Representados em vermelho.

---

## 9. Navegação do Aplicativo

A navegação deve ser hierárquica e unificada sob um `NavHost` no `MainActivity`, utilizando rotas tipadas para evitar falhas de transmissão de chaves:

1. **`Onboarding`:** Tela inicial apresentada para novos usuários oferecendo a proposta conceitual. Possui botão para "Começar agora" que transiciona para a tela Home consolidada.
2. **`Home` (Alertas Consolidados):** Linha de tempo organizada por gravidade cronológica:
   - *Atrasados*
   - *Hoje*
   - *Próximos 7 dias*
   - *Próximos 30 dias*
3. **`Modules` (Navegador de Seções):** Centraliza os acessos de cards estáticos para o Ativo Veículo, Ativo Casa, e Arquivo Vivo.
4. **`VehicleDetail`:** Tela focada no veículo contendo informações de odômetro, pendências exclusivas atreladas de manutenção do carro e acesso a histórico local.
5. **`HomeDetail`:** Tela com foco na residência, segregando em abas rápidas itens estruturais (elétrica, alvenaria), documentos e datas de pagamento do lar.
6. **`NewItem`:** Interface única de cadastro recebendo descritores de título, categoria associada do Ativo principal (Casa, Carro ou Outro) e data final com antecedência regulada de notificações.
7. **`Archive` (Arquivo Vivo):** Registro de manuais, notas fiscais digitadas e scans ordenados.

---

## 10. Persistência de Dados Conceitual (Room)

No banco de dados Room, as entidades são mapeadas diretamente para:
- Tabela `assets` persistindo o modelo de ativo físico.
- Tabela `control_items` contendo relação de chave estrangeira `asset_id` vinculando ao Ativo principal com índice composto. 
- Tabela `history_entries` vinculada a `item_id`.
- Tabela `attachments` mapeada para arquivos com caminho de link virtual estruturado.

*Nota de Segurança de Dados:* Nenhum dado flui via rede ou APIs de terceiros. A local-first garante privacidade total dos valores informados.

---

## 11. Regras de Negócio de Status

A lógica do `ControlStatusCalculator` segue a especificação algorítmica:

### Estado `OVERDUE` (Atrasado):
Instâncias de validação:
- `limitDate` é estritamente anterior (`isBefore`) à variável imutável `currentDate`.
- **OU** `currentMileage` está registrado como sendo **maior ou igual** à variável `predictedMileage`.

### Estado `ATTENTION` (Atenção):
Instâncias de validação:
- `limitDate` é igual (`isEqualTo`) à variável de consulta `currentDate`.
- **OU** a diferença de dias calculada via `ChronoUnit.DAYS.between(currentDate, limitDate)` é residual positiva e **menor ou igual** à janela definida em `alertWindowDays` (padrão = 7).
- **OU** a diferença de quilômetros restante para o odômetro programado (`predictedMileage` - `currentMileage`) é residual positiva e **menor ou igual** ao limite em `alertWindowMileage` (ex: menor de 1.000 km).

### Estado `OK` (Em Dia):
O item de controle não se encaixa em nenhuma das condições restritivas de atraso ou atenção acima listadas.

---

## 12. Diretrizes de Testes e Aceite

- **Separamento e Pureza de Entregas:** Testes devem cobrir todas as saídas lógicas de dias positivos, negativos, datas nulas e inversão de quilômetros na classe pura sem simulação de contexto do emulador de Android.
- **Portabilidade de Idioma:** Toda a interface do usuário (labels, cards, botões) e estados de mensagens expostos devem ser formulados integralmente na língua nacional: **Português do Brasil (pt-BR)**.
- **Estilo Visual:** Respeitar o minimalismo escuro-azul e cards familiares arredondados para garantir fácil escaneamento visual e navegação confortável, em conformidade com o design oficial Casa em Dia.
