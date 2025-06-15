package zyntex;

import java.util.List;

class AstPrettyPrinter implements ExprVisitor<String>, StmtVisitor<String> {

    private int indentLevel = 0;

    String print(List<Stmt> statements) {
        StringBuilder builder = new StringBuilder();
        for (Stmt statement : statements) {
            if (statement != null) {
                builder.append(statement.accept(this)).append("\n");
            }
        }
        return builder.toString();
    }

    private String indent() {
        return "  ".repeat(indentLevel);
    }


    @Override
    public String visitBlockStmt(Stmt.Block stmt) {
        StringBuilder builder = new StringBuilder();
        builder.append("(bloco");
        indentLevel++;
        for (Stmt statement : stmt.statements) {
            builder.append("\n").append(indent()).append(statement.accept(this));
        }
        indentLevel--;
        builder.append("\n").append(indent()).append(")");
        return builder.toString();
    }
    
    // ... os outros métodos visit... continuam chamando o helper 'format' ...

    // --- MÉTODOS PARA EXPRESSÕES ---

    @Override
    public String visitAssignExpr(Expr.Assign expr) {
        return format(":= " + expr.name.lexeme(), expr.value);
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return format(expr.operator.lexeme(), expr.left, expr.right);
    }
    
    // ... todos os outros métodos de visitação ...
    @Override public String visitExpressionStmt(Stmt.Expression stmt) { return format("expr_stmt", stmt.expression); }
    @Override public String visitIfStmt(Stmt.If stmt) { return stmt.elseBranch == null ? format("zf", stmt.condition, stmt.thenBranch) : format("zf-zl", stmt.condition, stmt.thenBranch, stmt.elseBranch); }
    @Override public String visitPrintStmt(Stmt.Print stmt) { return format("out", stmt.expression); }
    @Override public String visitVarStmt(Stmt.Var stmt) { return stmt.initializer == null ? format("vx " + stmt.name.lexeme()) : format("vx " + stmt.name.lexeme(), stmt.initializer); }
    @Override public String visitVariableExpr(Expr.Variable expr) { return expr.name.lexeme(); }
    @Override public String visitGroupingExpr(Expr.Grouping expr) { return format("group", expr.expression); }
    @Override public String visitLiteralExpr(Expr.Literal expr) { return expr.value == null ? "null" : expr.value.toString(); }
    @Override public String visitUnaryExpr(Expr.Unary expr) { return format(expr.operator.lexeme(), expr.right); }

    // O novo helper que cuida da indentação
    private String format(String name, Object... parts) {
        StringBuilder builder = new StringBuilder();
        builder.append("(").append(name);

        indentLevel++;
        for (Object part : parts) {
            builder.append("\n").append(indent());
            if (part instanceof Expr) {
                builder.append(((Expr)part).accept(this));
            } else if (part instanceof Stmt) {
                builder.append(((Stmt)part).accept(this));
            } else if (part instanceof Token) {
                builder.append(((Token) part).lexeme());
            } else {
                builder.append(part);
            }
        }
        indentLevel--;
        builder.append("\n").append(indent()).append(")");

        return builder.toString();
    }
}