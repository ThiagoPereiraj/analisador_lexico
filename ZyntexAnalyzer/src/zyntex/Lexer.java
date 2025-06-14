// zyntex/Lexer.java (Versão que reconhece NÚMEROS)
package zyntex;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int startOfLexeme = 0;
    private int currentPosition = 0;
    private int line = 1;
    private int column = 1;

    public Lexer(String source) {
        this.source = source;
    }

    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            startOfLexeme = currentPosition;
            scanToken();
        }

        tokens.add(new Token(TokenType.EOF, "", null, line, column));
        return tokens;
    }

    private void scanToken() {
        char c = advance();

        switch (c) {
            // Símbolos simples
            case '(': addToken(TokenType.LPAREN); break;
            case ')': addToken(TokenType.RPAREN); break;
            case '{': addToken(TokenType.LBRACE); break;
            case '}': addToken(TokenType.RBRACE); break;
            case '+': addToken(match('>') ? TokenType.INCREMENT : TokenType.ADDITION); break;
            case '-': addToken(match('>') ? TokenType.DECREMENT : TokenType.SUBTRACTION); break;
            case '*': addToken(match('*') ? TokenType.MULTIPLICATION : null); break; // null vai gerar erro
            case '/': addToken(match('/') ? TokenType.DIVISION : null); break; // null vai gerar erro

            // Operadores com 1, 2 ou 3 caracteres
            case '!': addToken(match('=') && match('?') ? TokenType.NOT_EQUAL : TokenType.LOGICAL_NOT); break;
            case '=': addToken(match('>') ? TokenType.ASSIGN_TYPE : (match('?') ? TokenType.EQUAL_TO : null)); break;
            case ':': addToken(match('=') ? TokenType.ASSIGN_VALUE : null); break;
            case '>': addToken(match('>') ? TokenType.GREATER_THAN : (match('=') ? TokenType.GREATER_EQUAL : null)); break;
            case '<': addToken(match('<') ? TokenType.LESS_THAN : (match('=') ? TokenType.LESS_EQUAL : null)); break;

            // Comentários
            case '#':
                if (match('#')) {
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else {
                    error("'#' sozinho não é um operador válido. Para comentários, use '##'.");
                }
                break;

            // Ignorar espaços em branco
            case ' ': case '\r': case '\t': break;
            case '\n': line++; column = 1; break;

            default:
                if (isDigit(c)) {
                    // Se for um dígito, começa a reconhecer um número.
                    number();
                } else {
                    error(String.format("Caractere inesperado '%c'.", c));
                }
                break;
        }
    }
    
    private void number() {
        while (isDigit(peek())) {
            advance();
        }

        // Verifica se há parte fracionária
        if (peek() == '.' && isDigit(peekNext())) {
            advance();

            while (isDigit(peek())) {
                advance();
            }
            // Adiciona o token como FLOAT
            String valueStr = source.substring(startOfLexeme, currentPosition);
            addToken(TokenType.FLOAT_LITERAL, Double.parseDouble(valueStr));
        } else {
            // Adiciona o token como INTEGER
            String valueStr = source.substring(startOfLexeme, currentPosition);
            addToken(TokenType.INTEGER_LITERAL, Integer.parseInt(valueStr));
        }
    }


    // --- Métodos de Ajuda ---

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private char peekNext() {
        if (currentPosition + 1 >= source.length()) return '\0';
        return source.charAt(currentPosition + 1);
    }

    private boolean isAtEnd() {
        return currentPosition >= source.length();
    }

    private char advance() {
        char c = source.charAt(currentPosition++);
        column++;
        return c;
    }
    
    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(currentPosition) != expected) return false;

        currentPosition++;
        column++;
        return true;
    }

    private char peek() {
        if (isAtEnd()) return '\0'; 
        return source.charAt(currentPosition);
    }
    
    private void error(String message) {
        System.err.printf("Erro Léxico: %s (linha %d, coluna %d)%n", message, line, column);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(startOfLexeme, currentPosition);
        int tokenColumn = column - text.length();
        tokens.add(new Token(type, text, literal, line, tokenColumn));
    }
}