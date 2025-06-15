import java.util.List;

class AstPrinter implements ExprVisitor<String>, StmtVisitor<String> {

    String print(List<Stmt> statements) {
        StringBuilder builder = new StringBuilder();
        for (Stmt statement : statements) {
            if (statement != null) {
                builder.append(statement.accept(this)).append("\n");
            }
        }
        return builder.toString();
    }

    @Override
    public String visitBlockStmt(Stmt.Block stmt) {
        StringBuilder builder = new StringBuilder();
        builder.append("(bloco");
        for (Stmt statement : stmt.statements) {
            builder.append(" ").append(statement.accept(this));
        }
        builder.append(")");
        return builder.toString();
    }

    @Override
    public String visitExpressionStmt(Stmt.Expression stmt) {
        return parenthesize("expr_stmt", stmt.expression);
    }

    @Override
    public String visitIfStmt(Stmt.If stmt) {
        if (stmt.elseBranch == null) {
            return parenthesize("zf", stmt.condition, stmt.thenBranch);
        }
        return parenthesize("zf-zl", stmt.condition, stmt.thenBranch, stmt.elseBranch);
    }

    @Override
    public String visitPrintStmt(Stmt.Print stmt) {
        return parenthesize("out", stmt.expression);
    }

    @Override
    public String visitVarStmt(Stmt.Var stmt) {
        if (stmt.initializer == null) {
            return parenthesize("vx " + stmt.name.lexeme());
        }
        return parenthesize("vx " + stmt.name.lexeme() + " :=", stmt.initializer);
    }

    @Override
    public String visitAssignExpr(Expr.Assign expr) {
        return parenthesize(":= " + expr.name.lexeme(), expr.value);
    }

    @Override
    public String visitVariableExpr(Expr.Variable expr) {
        return expr.name.lexeme();
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return parenthesize(expr.operator.lexeme(), expr.left, expr.right);
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return parenthesize("group", expr.expression);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if (expr.value == null) return "null";
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return parenthesize(expr.operator.lexeme(), expr.right);
    }

    private String parenthesize(String name, Object... parts) {
        StringBuilder builder = new StringBuilder();
        builder.append("(").append(name);
        for (Object part : parts) {
            builder.append(" ");
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
        builder.append(")");
        return builder.toString();
    }
}