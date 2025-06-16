package zyntex;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Código fonte de exemplo na linguagem Zyntex
        String sourceCode = "vx idade := 25;\n" + //
                        "zf (idade >> 18) {\n" + //
                        "  out(\"Maior de idade\");\n" + //
                        "} zl {\n" + //
                        "  out(\"Menor de idade\");\n" + //
                        "}";

        System.out.println("Analisando o código fonte:");
        System.out.println("-------------------------");
        System.out.println(sourceCode);
        System.out.println("-------------------------");
        System.out.println("Tokens Gerados:");

        Lexer lexer = new Lexer(sourceCode);
        List<Token> tokens = lexer.scanTokens();

        for (Token token : tokens) {
            System.out.println(token);
        }
         Parser parser = new Parser(tokens);
         List<Stmt> statements = parser.parse();

         System.out.println("-------------------------");
         System.out.println("Árvore de Sintaxe Gerada:");
         System.out.println(new AstVerbosePrinter().print(statements));
    }
}
