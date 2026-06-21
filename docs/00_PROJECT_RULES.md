# 00_PROJECT_RULES.md — Regras de Projeto e Invariantes

Este documento descreve as invariantes absolutas, regras de desenvolvimento permanentes, convenções e regras do projeto **Casa em Dia**.

---

## 1. Regras Permanentes (R1 - R10)

*   **R1 (Funcionalidades Mantidas):** Nunca remover funcionalidades existentes ou simplificar reduzindo recursos do usuário.
*   **R2 (Stack Tecnológica):** Nunca alterar a stack tecnológica oficial sem aprovação explícita do usuário.
*   **R3 (Minimalismo nas Alteraçoes):** Não reescrever arquivos inteiros sem necessidade absoluta; preferir manter o código circundante inalterado.
*   **R4 (Escopo Focado):** Alterar apenas arquivos diretamente relacionados e necessários para resolver a tarefa corrente.
*   **R5 (Documentação Sincronizada):** Sempre atualizar a documentação afetada quando houver alterações no comportamento ou na estrutura do projeto.
*   **R6 (Persistência Room):** Manter compatibilidade rigorosa com o banco de dados Room existente. Não realizar migrações destrutivas sem estratégia de backup/compatibilidade.
*   **R7 (Estabilidade do Build):** O projeto deve permanecer 100% livre de erros de compilação após cada alteração ou entrega.
*   **R8 (Integridade de Dados):** Nunca inventar novas entidades, tabelas de banco de dados ou campos fictícios sem alinhamento com a especificação técnica oficial.
*   **R9 (Refatorações Seguras):** Preferir extrações cirúrgicas de métodos e componentes em vez de refatorações agressivas baseadas em padrões desnecessários.
*   **R10 (Separação de Responsabilidades):** Sempre separar responsabilidades (Lógica de UI vs Lógica de Negócio) quando possível.

---

## 2. Invariantes Absolutas e Convenções

1.  **Local-First Completo:** O aplicativo funciona totalmente offline. Nenhuma API externa ou banco remoto é integrado para as tarefas do MVP.
2.  **Idioma Oficial:** Toda a interface, rótulos (labels), notificações, toasts e logs visíveis no aplicativo devem ser formulados integralmente em **Português do Brasil (pt-BR)**.
3.  **Localização de Recursos:**
    *   Arquivos de interface e Compose em `com.example.ui`
    *   Entidades e banco de dados em `com.example.data`
    *   Regras e ajudantes auxiliares em sub-módulos ou classes helper no mesmo pacote.

---

## Arquivos Críticos

Este projeto possui arquivos de extrema importância funcional e estrutural que requerem cuidados redobrados:
*   `AppDatabase.kt`
*   `MainViewModel.kt`
*   `NotificationScheduler.kt`
*   `AndroidManifest.xml`
*   `MainActivity.kt`

### Diretriz Especial para Alterações em Arquivos Críticos:
Qualquer modificação proposta nestes arquivos exige impreterivelmente:
*   **Atenção redobrada:** cuidado minucioso com a lógica existente;
*   **Validação completa:** verificação minuciosa de compilação e funcionamento;
*   **Atualização documental:** detalhamento rigoroso em caso de impactos estruturais;
*   **Execução do checklist de regressão:** rodar todas as verificações necessárias;
*   **Cuidado especial com compatibilidade Room e notificações:** evitar migrações destrutivas ou quebras nos fluxos de agendamentos e alarmes locais.

---

## 3. Controle de Crescimento de Arquivos

Nenhum arquivo deve crescer indefinidamente. Antes de adicionar código novo em um arquivo existente, a IA deve avaliar:
*   O arquivo já está perto do limite recomendado?
*   A nova lógica pertence realmente a esse arquivo?
*   É possível extrair para componente/helper/use case?
*   A mudança aumenta o acoplamento?
*   A alteração mistura UI, regra de negócio ou persistência?

### Limites de Alerta Recomendados:
*   **Tela Compose:** acima de 300 linhas, avaliar extração.
*   **Componente (UI):** acima de 150 linhas, avaliar divisão.
*   **Dialog (UI):** acima de 100 linhas, avaliar divisão.
*   **ViewModel:** acima de 400 linhas, avaliar use cases/helpers.
*   **DAO/Database:** acima de 400 linhas, avaliar separar entidades, DAO e helpers.
*   **Utils/Helpers:** acima de 200 linhas, avaliar dividir por domínio.

### Regra Obrigatória de Crescimento:
Se um arquivo ultrapassar o limite recomendado, a IA deve responder obrigatoriamente no bloco `RISKS` do formato de resposta:
`FILE GROWTH RISK: <arquivo> ultrapassa ou se aproxima do limite recomendado.`

### Ação Esperada:
Quando houver risco de crescimento, preferir:
*   extrair componente;
*   extrair helper;
*   criar arquivo de domínio específico;
*   mover lógica de UI para componente;
*   mover lógica de data/recorrência para util/helper;
*   manter ViewModel apenas como orquestrador.

### Exceção:
Não refatorar agressivamente só por número de linhas. Se a extração aumentar complexidade ou risco de regressão, manter o arquivo e registrar a justificativa em `ASSUMPTIONS` ou `RISKS`.

---

## 4. Formato de Resposta do Agente Requerido

Toda e qualquer entrega futura realizada pela IA deve seguir obrigatoriamente a estrutura descrita abaixo:

### Structure Schema

**Objective**
*   *Descrição concisa de qual foi o objetivo técnico ou funcional desta entrega.*

**Files changed**
*   *Lista contendo os caminhos exatos de todos os arquivos que foram modificados.*

**Contract impact**
*   *Impactos causados sobre contratos públicos, assinaturas de classes, nomes de tabelas ou rotas de navegação.*

**FACTS**
*   *Fatos técnicos reais e rigorosamente comprovados sobre o estado atual do código observado durante o processo.*

**ASSUMPTIONS**
*   *Hipóteses e premissas adotadas deliberadamente para guiar as decisões de desenvolvimento.*

**UNKNOWNS**
*   *Pontos de incerteza do sistema, informações ausentes ou arquiteturas de terceiros que dependem de feedback.*

**RISKS**
*   *Riscos inerentes e regressões possíveis identificados no ecossistema (e.g. quebra de compilação, compatibilidade do Room ou falhas de permissão de alarmes).*

**Acceptance status**
*   *Verificação rápida de critérios de aceite obrigatórios.*

**Updated handoff**
*   *Status atualizado pronto para a próxima iteração técnica da IA.*

### Regra de Informação Ausente:
> [!IMPORTANT]
> Se houver qualquer informação crítica ausente para a execução segura da tarefa, a IA deve retornar imediatamente uma resposta informando `MISSING INFO` antes de alterar o projeto de qualquer forma.

---

## 5. Referência Cruzada de Documentos
*   Para regras específicas do produto, veja [01_PRODUCT_SPEC.md](01_PRODUCT_SPEC.md).
*   Para diagrama de arquitetura técnica, veja [02_ARCHITECTURE.md](02_ARCHITECTURE.md).
*   Para o estado atual funcional de entregas, consulte [03_CURRENT_STATE.md](03_CURRENT_STATE.md).
*   Para entender a pirâmide e os padrões de teste, consulte [11_TEST_STRATEGY.md](11_TEST_STRATEGY.md).
