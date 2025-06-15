public enum TokenType {
    // --- Símbolos ---
    LPAREN,      // (
    RPAREN,      // )
    LBRACE,      // {
    RBRACE,      // }
    COMENT,      // ##

    // --- Operadores ---

    // Lógicos
    LOGICAL_NOT,          // !
    LOGICAL_AND,  // &&
    LOGICAL_OR,   // ||

    // Relacionais
    EQUAL_TO,        // =?
    NOT_EQUAL,       // !=?
    GREATER_THAN,    // >>
    GREATER_EQUAL,   // >=
    LESS_THAN,       // <<
    LESS_EQUAL,      // <=

    // Aritméticos
    ADDITION,        // ++
    SUBTRACTION,     // --
    MULTIPLICATION,  // **
    DIVISION,        // //

    // Atribuição
    ASSIGN_VALUE,    // :=
    ASSIGN_TYPE,     // =>

    // Unários
    INCREMENT,       // +>
    DECREMENT,       // ->

    // --- Literais ---
    IDENTIFIER,      // nomes de variáveis, funções, etc.
    STRING_LITERAL,  // "texto"
    INTEGER_LITERAL, // 123
    FLOAT_LITERAL,   // 12.3

    // --- Palavras-Chave ---

    // Definições
    KEYWORD_FX,       // fx
    KEYWORD_VX,       // vx

    // Tipos de Dados
    KEYWORD_INT,      // int
    KEYWORD_FLOAT,    // float
    KEYWORD_CHAR,     // char
    KEYWORD_STRING,   // string
    KEYWORD_BOOL,     // bool
    KEYWORD_ARR,      // arr
    KEYWORD_LIST,     // list
    KEYWORD_FUNCTION, // function
    KEYWORD_DICT,     // dict

    // Condicionais
    KEYWORD_ZF,       // zf (if)
    KEYWORD_ZL,       // zl (else)
    KEYWORD_FR,       // fr (for)
    KEYWORD_WL,       // wl (while)

    // Entrada, Saída e Retorno
    KEYWORD_OUT,      // out
    KEYWORD_IN,       // in
    KEYWORD_RX,       // rx

    // Palavras-chave que funcionam como operadores
    KEYWORD_RD,       // rd (radiciação)
    KEYWORD_EXP,      // exp (exponenciação)
    
    // Valores Constantes
    KEYWORD_TRUE,     // true
    KEYWORD_FALSE,    // false
    KEYWORD_NULL,     // null
    KEYWORD_VOID,     // void

    EOF, //Fim do arquivo 'End Of File'
}