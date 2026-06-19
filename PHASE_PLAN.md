# PHASE_PLAN.md — Plano de Fases e Roadmap de Evolução do Casa em Dia

Este documento mapeia o ciclo de desenvolvimento incremental do projeto **Casa em Dia**. Ele garante que o produto evolua de forma estruturada, segura e previsível, impossibilitando a inserção precoce de funcionalidades sem validação e maturidade arquitetural prévia.

---

## Regra Fundamental de Evolução Incremental

> **[REGRA CRÍTICA DE ESCOPO]** 
> Nenhuma funcionalidade que pertença a uma fase futura pode ser implementada sem autorização expressa e confirmação documentada. Se houver dependências de infraestrutura, deve-se preparar apenas interfaces neutras ou marcações com `TODO`, nunca códigos reais ou lógica de produção que fujam do escopo da fase ativa corrente.

---

## Roadmap de Desenvolvimento (8 Fases)

### 📌 Fase 1: Fundação do App, Telas Estáticas & Navegação (FASE ATUAL CONCLUÍDA)
**Status:** Concluído / Estável
- **Objetivos:**
  - Criação da infraestrutura nativa Android Kotlin + Jetpack Compose.
  - Implementação completa do arquivo de governança `AI_RULES.md` e regras de conformidade com o produto.
  - Estrutura inicial de pacotes segregados por camadas (`data`, `domain`, `ui`, `navigation`).
  - Navegação fluida entre telas usando Compose Navigation através de abas na barra inferior (`AppBottomNav` para as rotas Início, Módulos e Arquivo).
  - Desenvolvimento das interfaces Material 3 ricas baseadas no design oficial:
    1. `OnboardingScreen` (Painel conceitual de acesso).
    2. `AlertsHomeScreen` (Linha do tempo consolidada por status e agrupamento temporal).
    3. `ModulesScreen` (Direcionamento de blocos de Carro, Casa e Arquivo Vivo).
    4. `VehicleDetailScreen` (Painel descritivo do Palio 1.6 e pendências).
    5. `HomeDetailScreen` (Painel descritivo da residência).
    6. `NewItemScreen` (Formulário para novos controles).
    7. `ArchiveScreen` / `BentoArchiveScreen` (Estruturação das anotações e scans).
  - Alimentação de dados demo estruturados para testes visuais em memória.
  - Desenvolvimento do motor isento de regras matemáticos `ControlStatusCalculator` e cobertura por baterias de testes unitários.

---

### 📅 Fase 2: Integração de Persistência com Room, Domain Model & Repositórios (PRÓXIMA FASE)
**Status:** Planejada / Aguardando Inicialização Formal
- **Objetivos:**
  - Ativação do Banco de Dados local SQLite via **Room Database**.
  - Criação das tabelas baseadas nas entidades de Room: 
    - `AssetEntity`
    - `ControlItemEntity`
    - `AttachmentEntity`
    - `HistoryEntryEntity`
  - Implementação dos DAOs e injeção do padrão Repository Pattern.
  - Estruturação dos Mappers (`Entity` ↔ `Domain Model`) que garantem o isolamento da arquitetura contra contaminações de anotações do Room em camadas de lógica.
  - Transição do `MainViewModel` para salvar e consultar itens de controle diretamente na fonte da verdade persistida.
- **Validação de Transição:**
  - Todos os testes unitários do Room e repositórios passando (via Robolectric local).

---

### 📅 Fase 3: Central de Notificações Locais & Alarm Manager
**Status:** Fase Futura (Bloqueada)
- **Objetivos:**
  - Substituição de avisos puramente visuais por lembretes físicos no sistema operacional Android.
  - Utilização do `AlarmManager` ou `WorkManager` para agendamento de verificações automáticas diárias de proximidade de vencimento.
  - Disparo de notificações locais ricas em pt-BR com ícone identificador (ex: ícone de alerta de carro para troca de óleo viciada, ícone de gota d'água para dedetização residencial).
  - Implementação de permissões dinâmicas do Android 13+ (`POST_NOTIFICATIONS`) com fluxos de tratamento educativo e tratamento de rejeição elegante.

---

### 📅 Fase 4: Arquivo Vivo Funcional e Sandbox de Salvamento
**Status:** Fase Futura (Bloqueada)
- **Objetivos:**
  - Armazenamento físico real de arquivos digitados no Sandbox interno do app (`context.filesDir`).
  - Mapeamento das imagens de notas fiscais, manuais digitais de fabricante e PDFs das apólices de seguro integradas aos itens de controle.
  - Compressão de imagens antes do salvamento em disco a fim de evitar estouro de armazenamento local.
  - Implementação de exportação localizada de arquivos para o diretório compartilhado do usuário (Download/Documentos).

---

### 📅 Fase 5: Operações Completas de Mutação (Edição, Exclusão e Conclusão de Ciclos)
**Status:** Fase Futura (Bloqueada)
- **Objetivos:**
  - Desenvolvimento de telas de edição rápidas de itens diretamente de seus locais de visualização.
  - Lógica para conclusão de ciclo de tarefas:
    - Ao dar baixa em uma tarefa recorrente baseada em data, o sistema adiciona automaticamente uma entrada no Histórico de Manutenção (com data e custo) e recalcula e agenda a próxima data futura baseada no intervalo.
    - Ao dar baixa de manutenção por odômetro (ex: óleo), solicita-se o odômetro atual para reiniciar a janela preditiva de quilômetros.
  - Fluxo de segurança para exclusão física de ativos ou itens associados (com diálogos de alerta e confirmação claros para impedir perda acidental pelo usuário).

---

### 📅 Fase 6: Sistema de Backup e Restauração Local (Offline-Always)
**Status:** Fase Futura (Bloqueada)
- **Objetivos:**
  - Exclusividade local preservada: sem servidores externos ou provedores de nuvem obrigatórios.
  - Empacotamento de toda a base de dados Room (SQLite) e arquivos da Sandbox anexos em um único arquivo compacto criptografado no formato `.dia` ou ZIP.
  - Interface na área de configurações que permite ao usuário exportar e salvar em seu Android, Google Drive ou enviar via email de forma manual (via `Intent.createChooser`).
  - Validação rigorosa de arquivos importados durante a restauração de backup para detectar corrupção do SQLite ou inserções maliciosas.

---

### 📅 Fase 7: Mecanismo de Configuração de Licenças e Monetização
**Status:** Fase Futura (Bloqueada)
- **Objetivos:**
  - Implementação do Google Play Billing para vendas no modelo Freemium (ex: limite máximo gratuito de 3 ativos e 10 itens de controle por ativo; ativação ilimitada sem plano de assinatura, através de taxa de uso único vitalícia).
  - Verificação de compras totalmente armazenada com chaves de criptografia assinadas localmente a fim de respeitar a integridade offline.

---

### 📅 Fase 8: Melhorias de Vida Útil e Refinamento Pós-MVP
**Status:** Fase Futura (Bloqueada)
- **Objetivos:**
  - Suporte total a múltiplos temas do sistema (Dynamic Colors Material 3 e Dark Theme completo).
  - Widgets dinâmicos para a tela inicial do Android expondo os alertas em atraso imediatos.
  - Atalhos rápidos (*App Shortcuts*) a partir do ícone do inicializador do aplicativo que abrem diretamente a NewItemScreen.

---

## Gestão de Conformidade e Auditoria de Roadmap

Qualquer submissão de código que represente adiantamento de escopo sem assinatura física do cliente será rejeitada de forma automática pelo pipeline de Integração Contínua (CI), forçando o restabelecimento do escopo estável ativo (Fase 1 e Fase 2 na sequência de aprovação). Todo deparar com novos requisitos técnicos deve ser enquadrado nas diretrizes destas fases ou adicionado a este mapa como um TODO explícito.
