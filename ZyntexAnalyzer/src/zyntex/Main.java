package zyntex;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String sourceCode = """
            ## Exemplo de verificação de idade em Zyntex
            vx idade => int
            idade := 25
                            
            ## Estrutura condicional para verificar a idade
            zf (idade >> 18) {
                out("Maior de idade")
            } zl {
                out("Menor de idade")
            }
            """;

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
        // Parser parser = new Parser(tokens);
        // List<Stmt> statements = parser.parse();

        // System.out.println("-------------------------");
        // System.out.println("Árvore de Sintaxe Gerada:");
        // System.out.println(new AstPrettyPrinter().print(statements));
    }
}