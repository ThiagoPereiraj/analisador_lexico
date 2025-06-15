// zyntex/Parser.java

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Stmt> parse() {
        List<Stmt> statements = new ArrayList<>();
        while (!isAtEnd()) {
            Stmt declaration = declaration();
            if (declaration != null) {
                statements.add(declaration);
            }
        }
        return statements;
    }

    private Stmt declaration() {
        try {
            if (match(TokenType.KEYWORD_VX))
                return varDeclaration();
            return statement();
        } catch (ParseError error) {
            synchronize();
            return null;
        }
    }

    private Stmt varDeclaration() {
        Token name = consume(TokenType.IDENTIFIER, "Esperava nome de variável.");
        if (match(TokenType.ASSIGN_TYPE)) {
            match(TokenType.KEYWORD_INT, TokenType.KEYWORD_FLOAT, TokenType.KEYWORD_STRING, TokenType.KEYWORD_BOOL,
                    TokenType.KEYWORD_CHAR);
        }
        Expr initializer = null;
        if (match(TokenType.ASSIGN_VALUE)) {
            initializer = expression();
        }
        return new Stmt.Var(name, initializer);
    }

    private Stmt statement() {
        if (match(TokenType.KEYWORD_ZF))
            return ifStatement();
        if (match(TokenType.KEYWORD_OUT))
            return printStatement();
        if (match(TokenType.LBRACE))
            return new Stmt.Block(block());
        return expressionStatement();
    }

    private Stmt ifStatement() {
        consume(TokenType.LPAREN, "Esperava '(' após 'zf'.");
        Expr condition = expression();
        consume(TokenType.RPAREN, "Esperava ')' após a condição do if.");
        Stmt thenBranch = statement();
        Stmt elseBranch = null;
        if (match(TokenType.KEYWORD_ZL)) {
            elseBranch = statement();
        }
        return new Stmt.If(condition, thenBranch, elseBranch);
    }

    private Stmt printStatement() {
        consume(TokenType.LPAREN, "Esperava '(' após 'out'.");
        Expr value = expression();
        consume(TokenType.RPAREN, "Esperava ')' após o valor do out.");
        return new Stmt.Print(value);
    }

    private List<Stmt> block() {
        List<Stmt> statements = new ArrayList<>();
        while (!check(TokenType.RBRACE) && !isAtEnd()) {
            statements.add(declaration());
        }
        consume(TokenType.RBRACE, "Esperava '}' para fechar o bloco.");
        return statements;
    }

    private Stmt expressionStatement() {
        Expr expr = expression();
        return new Stmt.Expression(expr);
    }

    private void synchronize() {
        advance();
        while (!isAtEnd()) {
            switch (peek().type()) {
                case KEYWORD_FX:
                case KEYWORD_VX:
                case KEYWORD_ZF:
                case KEYWORD_FR:
                case KEYWORD_WL:
                case KEYWORD_OUT:
                case KEYWORD_IN:
                case KEYWORD_RX:
                    return;
                default:
                    break;
            }
            advance();
        }
    }

    // --- CASCATA DE EXPRESSÕES COMPLETA E CORRIGIDA ---

    private Expr expression() {
        return assignment();
    }

    private Expr assignment() {
        Expr expr = logic_or(); // A atribuição tem a menor precedência
        if (match(TokenType.ASSIGN_VALUE)) {
            Token equals = previous();
            Expr value = assignment();
            if (expr instanceof Expr.Variable) {
                Token name = ((Expr.Variable) expr).name;
                return new Expr.Assign(name, value);
            }
            error(equals, "Alvo de atribuição inválido.");
        }
        return expr;
    }

    private Expr logic_or() {
        Expr expr = logic_and();
        while (match(TokenType.LOGICAL_OR)) {
            Token operator = previous();
            Expr right = logic_and();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr logic_and() {
        Expr expr = equality();
        while (match(TokenType.LOGICAL_AND)) {
            Token operator = previous();
            Expr right = equality();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr equality() {
        Expr expr = comparison();
        while (match(TokenType.NOT_EQUAL, TokenType.EQUAL_TO)) {
            Token operator = previous();
            Expr right = comparison();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr comparison() {
        Expr expr = term();
        while (match(TokenType.GREATER_THAN, TokenType.GREATER_EQUAL, TokenType.LESS_THAN, TokenType.LESS_EQUAL)) {
            Token operator = previous();
            Expr right = term();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr term() {
        Expr expr = factor();
        while (match(TokenType.SUBTRACTION, TokenType.ADDITION)) {
            Token operator = previous();
            Expr right = factor();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr factor() {
        Expr expr = unary();
        while (match(TokenType.DIVISION, TokenType.MULTIPLICATION)) {
            Token operator = previous();
            Expr right = unary();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr unary() {
        if (match(TokenType.LOGICAL_NOT, TokenType.DECREMENT, TokenType.INCREMENT)) {
            Token operator = previous();
            Expr right = unary();
            return new Expr.Unary(operator, right);
        }
        return primary();
    }

    private Expr primary() {
        if (match(TokenType.KEYWORD_FALSE))
            return new Expr.Literal(false);
        if (match(TokenType.KEYWORD_TRUE))
            return new Expr.Literal(true);
        if (match(TokenType.KEYWORD_NULL))
            return new Expr.Literal(null);
        if (match(TokenType.INTEGER_LITERAL, TokenType.FLOAT_LITERAL, TokenType.STRING_LITERAL)) {
            return new Expr.Literal(previous().literal());
        }
        if (match(TokenType.IDENTIFIER)) {
            return new Expr.Variable(previous());
        }
        if (match(TokenType.LPAREN)) {
            Expr expr = expression();
            consume(TokenType.RPAREN, "Esperava ')' depois da expressão.");
            return new Expr.Grouping(expr);
        }
        throw error(peek(), "Expressão não esperada.");
    }

    private static class ParseError extends RuntimeException {
    }

    private Token consume(TokenType type, String message) {
        if (check(type))
            return advance();
        throw error(peek(), message);
    }

    private ParseError error(Token token, String message) {
        System.err.printf("Erro de Sintaxe: %s [linha %d, lexema '%s']%n", message, token.line(), token.lexeme());
        return new ParseError();
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd())
            return false;
        return peek().type() == type;
    }

    private Token advance() {
        if (!isAtEnd())
            current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type() == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }
}