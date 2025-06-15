package zyntex;

// A interface Visitor precisa ser declarada primeiro ou em seu próprio arquivo.
// Vamos mantê-la aqui por simplicidade, declarando-a antes da classe que a usa.
interface ExprVisitor<R> {
    R visitAssignExpr(Expr.Assign expr);
    R visitVariableExpr(Expr.Variable expr);
    R visitBinaryExpr(Expr.Binary expr);
    R visitGroupingExpr(Expr.Grouping expr);
    R visitLiteralExpr(Expr.Literal expr);
    R visitUnaryExpr(Expr.Unary expr);
}

public abstract class Expr {
    public abstract <R> R accept(ExprVisitor<R> visitor);

    public static final class Assign extends Expr {
        public final Token name;
        public final Expr value;
        public Assign(Token name, Expr value) { this.name = name; this.value = value; }
        @Override public <R> R accept(ExprVisitor<R> visitor) { return visitor.visitAssignExpr(this); }
    }

    public static final class Variable extends Expr {
        public final Token name;
        public Variable(Token name) { this.name = name; }
        @Override public <R> R accept(ExprVisitor<R> visitor) { return visitor.visitVariableExpr(this); }
    }

    public static final class Binary extends Expr {
        public final Expr left;
        public final Token operator;
        public final Expr right;
        public Binary(Expr left, Token operator, Expr right) { this.left = left; this.operator = operator; this.right = right; }
        @Override public <R> R accept(ExprVisitor<R> visitor) { return visitor.visitBinaryExpr(this); }
    }

    public static final class Grouping extends Expr {
        public final Expr expression;
        public Grouping(Expr expression) { this.expression = expression; }
        @Override public <R> R accept(ExprVisitor<R> visitor) { return visitor.visitGroupingExpr(this); }
    }

    public static final class Literal extends Expr {
        public final Object value;
        public Literal(Object value) { this.value = value; }
        @Override public <R> R accept(ExprVisitor<R> visitor) { return visitor.visitLiteralExpr(this); }
    }

    public static final class Unary extends Expr {
        public final Token operator;
        public final Expr right;
        public Unary(Token operator, Expr right) { this.operator = operator; this.right = right; }
        @Override public <R> R accept(ExprVisitor<R> visitor) { return visitor.visitUnaryExpr(this); }
    }
}