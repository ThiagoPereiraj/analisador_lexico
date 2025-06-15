public record Token(
    TokenType type,
    String lexeme,
    Object literal,
    int line,
    int column
) {
    @Override
    public String toString() {
        // Se não houver literal
        if (literal == null) {
            return String.format("[%d:%d] %s: '%s'", line, column, type, lexeme);
        }
        // Se houver um literal, o exibe também.
        return String.format("[%d:%d] %s: '%s' -> %s", line, column, type, lexeme, literal);
    }
}