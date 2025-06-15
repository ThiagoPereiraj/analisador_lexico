// zyntex/AstVerbosePrinter.java
package zyntex;

import java.util.List;

// Uma impressora focada em ser descritiva e didática.
class AstVerbosePrinter implements ExprVisitor<String>, StmtVisitor<String> {

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

    // --- MÉTODOS PARA STATEMENTS ---

    @Override
    public String visitBlockStmt(Stmt.Block stmt) {
        return format("block", stmt.statements.toArray());
    }

    @Override
    public String visitExpressionStmt(Stmt.Expression stmt) {
        // Para atribuições, damos um nome mais específico.
        if (stmt.expression instanceof Expr.Assign) {
            return stmt.expression.accept(this);
        }
        return format("expression_statement", stmt.expression);
    }

    @Override
    public String visitIfStmt(Stmt.If stmt) {
        if (stmt.elseBranch == null) {
            return format("if_statement", stmt.condition, stmt.thenBranch);
        }
        return format("if_else_statement", stmt.condition, stmt.thenBranch, stmt.elseBranch);
    }

    @Override
    public String visitPrintStmt(Stmt.Print stmt) {
        return format("print_statement", stmt.expression);
    }

    @Override
    public String visitVarStmt(Stmt.Var stmt) {
        if (stmt.initializer == null) {
            return format("var_declaration " + stmt.name.lexeme());
        }
        // A atribuição já será tratada como um nó separado.
        return format("var_declaration " + stmt.name.lexeme(), stmt.initializer);
    }

    // --- MÉTODOS PARA EXPRESSÕES ---

    @Override
    public String visitAssignExpr(Expr.Assign expr) {
        // Tratamos a atribuição como um comando para ficar mais claro
        return format("assign_statement", new Expr.Variable(expr.name), expr.value);
    }

    @Override
    public String visitVariableExpr(Expr.Variable expr) {
        // Envolvemos o identificador para ser explícito
        return format("identifier " + expr.name.lexeme());
    }
    
    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if (expr.value instanceof String) {
            return format("string_literal \"" + expr.value.toString() + "\"");
        }
        if (expr.value instanceof Integer || expr.value instanceof Double) {
            return format("integer_literal " + expr.value.toString());
        }
        return expr.value == null ? "null" : expr.value.toString();
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        // Usamos um switch para dar nomes descritivos aos operadores
        String operatorName;
        switch (expr.operator.type()) {
            case GREATER_THAN: operatorName = "greater_than_operator"; break;
            case ADDITION: operatorName = "addition_operator"; break;
            case SUBTRACTION: operatorName = "subtraction_operator"; break;
            case MULTIPLICATION: operatorName = "multiplication_operator"; break;
            case DIVISION: operatorName = "division_operator"; break;
            case EQUAL_TO: operatorName = "equality_operator"; break;
            case NOT_EQUAL: operatorName = "not_equal_operator"; break;
            default: operatorName = expr.operator.lexeme(); break; // fallback
        }
        return format(operatorName, expr.left, expr.right);
    }

    // Os outros visitors apenas chamam o helper 'format'
    @Override public String visitGroupingExpr(Expr.Grouping expr) { return format("group", expr.expression); }
    @Override public String visitUnaryExpr(Expr.Unary expr) { return format(expr.operator.lexeme(), expr.right); }

    // Helper que monta a string com indentação
    private String format(String name, Object... parts) {
        StringBuilder builder = new StringBuilder();
        builder.append("(").append(name);
        if (parts.length > 0) {
            indentLevel++;
            for (Object part : parts) {
                builder.append("\n").append(indent());
                if (part instanceof Expr) {
                    builder.append(((Expr)part).accept(this));
                } else if (part instanceof Stmt) {
                    builder.append(((Stmt)part).accept(this));
                } else {
                    builder.append(part);
                }
            }
            indentLevel--;
            builder.append("\n").append(indent());
        }
        builder.append(")");
        return builder.toString();
    }
}