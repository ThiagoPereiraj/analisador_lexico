// zyntex/Lexer.java

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int startOfLexeme = 0;
    private int currentPosition = 0;
    private int line = 1;
    private int column = 1;

    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("fx", TokenType.KEYWORD_FX);
        keywords.put("vx", TokenType.KEYWORD_VX);
        keywords.put("int", TokenType.KEYWORD_INT);
        keywords.put("float", TokenType.KEYWORD_FLOAT);
        keywords.put("char", TokenType.KEYWORD_CHAR);
        keywords.put("string", TokenType.KEYWORD_STRING);
        keywords.put("bool", TokenType.KEYWORD_BOOL);
        keywords.put("arr", TokenType.KEYWORD_ARR);
        keywords.put("list", TokenType.KEYWORD_LIST);
        keywords.put("function", TokenType.KEYWORD_FUNCTION);
        keywords.put("dict", TokenType.KEYWORD_DICT);
        keywords.put("zf", TokenType.KEYWORD_ZF);
        keywords.put("zl", TokenType.KEYWORD_ZL);
        keywords.put("fr", TokenType.KEYWORD_FR);
        keywords.put("wl", TokenType.KEYWORD_WL);
        keywords.put("out", TokenType.KEYWORD_OUT);
        keywords.put("in", TokenType.KEYWORD_IN);
        keywords.put("rx", TokenType.KEYWORD_RX);
        keywords.put("rd", TokenType.KEYWORD_RD);
        keywords.put("exp", TokenType.KEYWORD_EXP);
        keywords.put("true", TokenType.KEYWORD_TRUE);
        keywords.put("false", TokenType.KEYWORD_FALSE);
        keywords.put("null", TokenType.KEYWORD_NULL);
        keywords.put("void", TokenType.KEYWORD_VOID);
    }

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
            case '(':
                addToken(TokenType.LPAREN);
                break;
            case ')':
                addToken(TokenType.RPAREN);
                break;
            case '{':
                addToken(TokenType.LBRACE);
                break;
            case '}':
                addToken(TokenType.RBRACE);
                break;
            case '+':
                addToken(match('>') ? TokenType.INCREMENT : (match('+') ? TokenType.ADDITION : null));
                break;
            case '-':
                addToken(match('>') ? TokenType.DECREMENT : (match('-') ? TokenType.SUBTRACTION : null));
                break;
            case '*':
                addToken(match('*') ? TokenType.MULTIPLICATION : null);
                break;
            case '/':
                addToken(match('/') ? TokenType.DIVISION : null);
                break;
            case '!':
                addToken(match('=') && match('?') ? TokenType.NOT_EQUAL : TokenType.LOGICAL_NOT);
                break;
            case '=':
                addToken(match('>') ? TokenType.ASSIGN_TYPE : (match('?') ? TokenType.EQUAL_TO : null));
                break;
            case ':':
                addToken(match('=') ? TokenType.ASSIGN_VALUE : null);
                break;
            case '>':
                addToken(match('>') ? TokenType.GREATER_THAN : (match('=') ? TokenType.GREATER_EQUAL : null));
                break;
            case '<':
                addToken(match('<') ? TokenType.LESS_THAN : (match('=') ? TokenType.LESS_EQUAL : null));
                break;
            case '#':
                if (match('#')) {
                    while (peek() != '\n' && !isAtEnd())
                        advance();
                } else {
                    error("'#' sozinho é inválido.");
                }
                break;

            // LÓGICA FALTANTE ADICIONADA AQUI
            case '&':
                if (match('&')) {
                    addToken(TokenType.LOGICAL_AND);
                } else {
                    error("'&' sozinho é inválido.");
                }
                break;
            case '|':
                if (match('|')) {
                    addToken(TokenType.LOGICAL_OR);
                } else {
                    error("'|' sozinho é inválido.");
                }
                break;

            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n':
                line++;
                column = 1;
                break;
            case '"':
                string();
                break;
            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    error(String.format("Caractere inesperado '%c'.", c));
                }
                break;
        }
    }

    private void identifier() {
        while (isAlphaNumeric(peek()))
            advance();
        String text = source.substring(startOfLexeme, currentPosition);
        TokenType type = keywords.get(text);
        if (type == null)
            type = TokenType.IDENTIFIER;
        addToken(type);
    }

    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') {
                line++;
                column = 1;
            }
            advance();
        }
        if (isAtEnd()) {
            error("String não terminada.");
            return;
        }
        advance();
        String value = source.substring(startOfLexeme + 1, currentPosition - 1);
        addToken(TokenType.STRING_LITERAL, value);
    }

    private void number() {
        while (isDigit(peek()))
            advance();
        if (peek() == '.' && isDigit(peekNext())) {
            advance();
            while (isDigit(peek()))
                advance();
            addToken(TokenType.FLOAT_LITERAL, Double.parseDouble(source.substring(startOfLexeme, currentPosition)));
        } else {
            addToken(TokenType.INTEGER_LITERAL, Integer.parseInt(source.substring(startOfLexeme, currentPosition)));
        }
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private char peekNext() {
        if (currentPosition + 1 >= source.length())
            return '\0';
        return source.charAt(currentPosition + 1);
    }

    private boolean isAtEnd() {
        return currentPosition >= source.length();
    }

    private char advance() {
        column++;
        return source.charAt(currentPosition++);
    }

    private boolean match(char expected) {
        if (isAtEnd() || source.charAt(currentPosition) != expected)
            return false;
        currentPosition++;
        column++;
        return true;
    }

    private char peek() {
        if (isAtEnd())
            return '\0';
        return source.charAt(currentPosition);
    }

    private void error(String message) {
        System.err.printf("Erro Léxico: %s (linha %d, coluna %d)%n", message, line, column - 1);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(startOfLexeme, currentPosition);
        int tokenColumn = column - text.length();
        if (tokenColumn < 1)
            tokenColumn = 1;
        tokens.add(new Token(type, text, literal, line, tokenColumn));
    }

}