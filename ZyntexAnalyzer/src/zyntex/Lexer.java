package zyntex;

import java.util.ArrayList;
import java.util.List;
// Futuramente, importaremos Map e HashMap para as palavras-chave
// import java.util.Map;
// import java.util.HashMap;

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

            // TODO: Implementar operadores, comentários, strings, números, etc.

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
                // No futuro, isso deverá lançar um erro formal.
                System.err.printf("Erro Léxico: Caractere inesperado '%c' na linha %d, coluna %d%n", c, line, column);
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

    // Para tokens sem valor literal (símbolos, palavras-chave)
    private void addToken(TokenType type) {
        addToken(type, null);
    }

    // Para tokens COM valor literal (strings, números)
    private void addToken(TokenType type, Object literal) {
        String text = source.substring(startOfLexeme, currentPosition);
        // A coluna para o token é a coluna onde ele começou.
        int tokenColumn = column - text.length();
        tokens.add(new Token(type, text, literal, line, tokenColumn));
    }
}