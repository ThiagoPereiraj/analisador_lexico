# Zyntex

**Zyntex** é uma linguagem de programação **multi-paradigma**, criada com o objetivo de ajudar estudantes a experimentarem e compreenderem os conceitos fundamentais dos paradigmas **imperativo** e **funcional**. Sua sintaxe simplificada e intuitiva permite escrever, analisar e executar pequenos programas, facilitando o aprendizado prático.

---

## Tópicos Abordados

- Paradigmas de programação da linguagem: imperativo e funcional
- Tipagem estática explícita
- Análise léxica e sintática personalizada
- Implementação de operadores, estruturas de controle, tipos e funções

---

## Como executar

### 1. Clone o repositório

```bash
git clone https://github.com/ThiagoPereiraj/analisador_lexico.git
cd analisador_lexico
```

### 2. Execute o analisador léxico

```bash
python analisador_linguagem_zyntex.py
```

### 3. Execute o analisador sintático

```bash
python analisador_sintatico_simples.py
```

---

## Estrutura da Linguagem

### Tipos de Dados

- Tipos Primitivos: `int`, `float`, `char`, `string`, `bool`, `void`
- Tipos compostos: `arr`, `list`, `dict`, `function`

### Palavras-chave

| Palavra-chave | Significado |
|---------------|-------------|
| `fx`          | Definir função |
| `vx`          | Definir variável |
| `zf`, `zl`    | Condicional `if`, `else` |
| `fr`, `wl`    | Laços `for`, `while` |
| `out`, `in`   | Saída e entrada de dados |
| `rx`          | Retorno de função |

### Operadores

- Aritméticos: `++`, `--`, `**`, `//`, `rd`, `exp`
- Relacionais: `=?`, `!=?`, `<<`, `>>`, `<=`, `>=`
- Lógicos: `&&`, `||`, `!`
- Atribuição: `:=`, `=>`
- Unários: `+>`, `->`
- Delimitadores e Símbolos: `{}`, `()`, `;`, `,`, `##`

---

## Gramática Formal (BNF)

```bnf
<programa> ::= <declaracao>*
<declaracao> ::= <declaracao_funcao> | <declaracao_variavel> | <comando>
```

> A gramática completa está disponível na [documentação PDF](./Documentação%20da%20Linguagem%20Zyntex.pdf)

---

## Exemplos de Código

```zyntex
## Soma simples
vx a => int := 5;
vx b => int := 10;
vx soma => int;
soma := a ++ b;
out soma;

## Função recursiva
fx fatorial(n => int) => int {
  zf (n <= 1) {
    rx 1;
  } zl {
    rx n ** fatorial(n -- 1);
  }
}
```

---

## Componentes do Projeto

| Arquivo                          | Descrição                                 |
|----------------------------------|-------------------------------------------|
| `analisador_linguagem_zyntex.py`| Analisador léxico para identificar tokens |
| `analisador_sintatico_simples.py`| Analisador sintático simplificado         |
| `README.md`                     | Documentação inicial                      |
| `Documentação da Linguagem Zyntex.pdf` | Manual completo com gramática e exemplos |

---
