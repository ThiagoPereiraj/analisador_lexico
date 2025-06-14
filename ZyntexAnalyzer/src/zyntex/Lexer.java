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

            // Operadores com 1 ou 2 caracteres
            case '!':
                if (match('=') && match('?')) {
                    addToken(TokenType.NOT_EQUAL);
                } else {
                    addToken(TokenType.LOGICAL_NOT);
                }
                break;
            case '=':
                if (match('>')) {
                    addToken(TokenType.ASSIGN_TYPE);
                } else if (match('?')) {
                    addToken(TokenType.EQUAL_TO);
                } else {
                    error("'=' sozinho não é um operador válido. Você quis dizer '=>' ou '=?'?");
                }
                break;
            case ':':
                if (match('=')) {
                    addToken(TokenType.ASSIGN_VALUE);
                } else {
                    error("':' sozinho não é um operador válido. Você quis dizer ':='?");
                }
                break;
            case '>':
                addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER_THAN);
                break;
            case '<':
                addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS_THAN);
                break;

            // Comentários
            case '#':
                if (match('#')) {
                    // Um comentário vai até o final da linha.
                    while (peek() != '\n' && !isAtEnd()) {
                        advance();
                    }
                } else {
                        error("'#' sozinho não é um operador válido. Para comentários, use '##'.");
                }
                break;


            // Ignorar espaços em branco
            case ' ':
            case '\r':
            case '\t':
                // Simplesmente ignora
                break;
            case '\n':
                line++;
                column = 1; // Reseta a coluna na nova linha
                break;

            default:
            
                error(String.format("Caractere inesperado '%c'.", c));
                break;
        }
    }

    // --- Métodos de Ajuda ---

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
        if (isAtEnd()) return '\0'; // Retorna um caractere nulo se estiver no fim
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