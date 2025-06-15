# Zyntex

## Propósito, Objetivo, Paradigma
Zyntex é uma linguagem de programação multi-paradigma criada com o propósito de oferecer aos estudantes de programação uma forma prática e direta para experimentar e consolidar seu entendimento dos conceitos fundamentais dos paradigmas imperativo (como variáveis, atribuições e controle de fluxo) e funcional (como funções de primeira classe, imutabilidade e funções de ordem superior), permitindo que estudantes escrevam e executem pequenos programas. A combinação desses paradigmas visa permitir que os estudantes comparem diferentes abordagens para a resolução de problemas e apreciem as vantagens de cada estilo.

## Elementos principais (tokens)
### Tipos de dados
| Tipo de dado | Sintaxe |
| :--- | :--- |
| Número inteiro | `int` |
| Número decimal | `float` |
| Caractere | `char` |
| Cadeia de caracteres | `string` |
| Boolean | `bool` |
| Array | `arr` |
| Lista | `list` |
| Função | `function`|
| Nulo | `null` |
| Vazio (void) | `void` |
| Dicionários | `dict` |

### Operadores
**Unário**
| Função | Sintaxe |
| :--- | :--- |
| Incremento | `+>` |
| Decremento | `->` |

**Aritméticos**
| Função | Sintaxe |
| :--- | :--- |
| Soma | `++` |
| Subtração | `--` |
| Multiplicação | `**` |
| Divisão | `//` |
| Radiciação | `rd` |
| Exponenciação | `exp` |

**Relacionais**
| Função | Sintaxe |
| :--- | :--- |
| Igual a | `=?` |
| Diferente de | `!=?` |
| Menor que | `<<` |
| Maior que | `>>` |
| Menor ou igual | `<=` |
| Maior ou igual | `>=` |

**Lógicos**
| Função | Sintaxe |
| :--- | :--- |
| AND | `&&` |
| OR | `\|\|` |
| NOT | `!` |

**Atribuição**
| Função | Sintaxe |
| :--- | :--- |
| Atribuição de valor | `:=` |
| Atribuição de tipo | `=>` |

### Palavras-chave
| Função | Sintaxe |
| :--- | :--- |
| Definir função | `fx` |
| Definir variável | `vx` |
| Condicional 'if' | `zf` |
| Condicional 'else' | `zl` |
| Laço 'for' | `fr` |
| Laço 'while' | `wl` |
| Impressão | `out` |
| Entrada de dados | `in` |
| Retorno de função | `rx` |
| Valor booleano verdadeiro | `true` |
| Valor booleano falso | `false` |


### Símbolos
| Função | Sintaxe |
| :--- | :--- |
| Delimitador de início de bloco | `{` |
| Delimitador de fim de bloco | `}` |
| Início de comentário | `##` |
| Parênteses | `()` |

---

## Regras Lexicais e de Formação de Tokens

Esta seção define as regras para a formação de tokens a partir do código-fonte.

### 1. Identificadores
Nomes de variáveis, funções, etc.
* Devem começar com uma letra ( `a-z`, `A-Z` ) ou underscore ( `_` ).
* Podem ser seguidos por qualquer combinação de letras, números ( `0-9` ) e underscores.
* São case-sensitive (`minhaVar` é diferente de `minhavar`).
* **Exemplos:** `soma`, `_valor`, `calculo1`

### 2. Literais
Representação de valores fixos no código.
* **Inteiro (`int`):** Uma sequência de um ou mais dígitos. Ex: `10`, `42`, `999`.
* **Decimal (`float`):** Uma sequência de dígitos, um ponto (`.`), e outra sequência de dígitos. Ex: `3.14`, `0.5`, `100.0`.
* **Cadeia de Caracteres (`string`):** Qualquer sequência de caracteres entre aspas duplas (`"`). Ex: `"Olá mundo"`, `"Zyntex"`.
* **Caractere (`char`):** Um único caractere entre aspas simples (`'`). Ex: `'A'`, `'z'`, `'7'`.
* **Booleano (`bool`):** As palavras-chave `true` e `false`.
* **Nulo (`null`):** A palavra-chave `null`.

### 3. Comentários
* Um comentário é iniciado com `##` e continua até o final da linha.
* O analisador léxico deve ignorar completamente os comentários.
* **Exemplo:** `vx idade := 18 ## Define a maioridade penal`

### 4. Espaçamento (Whitespace)
* Espaços (` `), tabulações (`\t`) e quebras de linha (`\n`) são usados para separar tokens.
* Múltiplos espaços são tratados como um só separador e são ignorados pelo analisador (exceto para contar o número das linhas).

### 5. Ambiguidade e "Casamento Mais Longo" (Longest Match)
O analisador deve sempre tentar formar o token mais longo possível a partir da entrada.
* Ao encontrar `-`, ele deve verificar se o próximo caractere é `>` (formando `->`) ou `-` (formando `--`).
* Ao encontrar `!`, ele deve verificar se os próximos são `=?` (formando `!=?`). Se não, o token é apenas `!`.
* Uma sequência de letras como `out` deve primeiro ser comparada com a lista de palavras-chave. Se não for nenhuma delas, será classificada como um `IDENTIFICADOR`.

---

## Exemplo de Código Zyntex

```zyntex
## Exemplo de verificação de idade em Zyntex
vx idade => int
idade := 25

## Estrutura condicional para verificar a idade
zf (idade >> 18) {
  out("Maior de idade")
} zl {
  out("Menor de idade")
}
```

## Como Executar o Projeto no IntelliJ IDEA

Este projeto foi desenvolvido em Java e pode ser facilmente configurado e executado na IDE IntelliJ IDEA. Siga os passos abaixo para configurar o ambiente após clonar o repositório.

1.  **Abrir o Projeto:**
    * Na tela inicial do IntelliJ, escolha a opção **`Open`**.
    * Navegue até a pasta **raiz** do projeto que você clonou (a pasta que contém a subpasta `src`) e selecione-a.

2.  **Configurar o SDK do Projeto e a Pasta de Saída:**
    * Com o projeto aberto, vá até o menu `File -> Project Structure...`.
    * Na janela que abrir, na aba **`Project`**, configure os seguintes campos:
        * **`SDK`**: Selecione uma versão do Java (JDK) **17 ou superior**. Se nenhuma estiver disponível, use a opção `Add SDK... -> Download JDK...` para que o IntelliJ baixe uma para você.
        * **`Compiler output`**: Clique no ícone de pasta (📁) e selecione ou crie uma pasta chamada `out` na raiz do projeto. É nesta pasta que o IntelliJ salvará os arquivos `.class` compilados.
    * Clique em `Apply`.

3.  **Marcar a Pasta de Código-Fonte (`src`):**
    * Ainda na janela `Project Structure`, vá para a aba **`Modules`** no menu da esquerda.
    * No painel da direita, selecione a aba **`Sources`**.
    * Na árvore de diretórios, clique na pasta `src`.
    * Com a pasta `src` selecionada, clique no botão **`Sources`** (marcado em azul) no topo. A pasta `src` ficará azul, indicando que o IntelliJ agora a reconhece como a raiz do seu código-fonte.
    * Clique em **`OK`** para fechar a janela.

4.  **Executar o Programa:**
    * Aguarde alguns segundos para o IntelliJ reindexar o projeto.
    * Navegue até `src/zyntex/Main.java` e abra o arquivo.
    * Um ícone de **play verde (▶️)** deve aparecer ao lado do método `main`. Clique nele e selecione **`Run 'Main.main()'`**.
    * A saída do programa será exibida no painel **`Run`** na parte inferior da IDE.