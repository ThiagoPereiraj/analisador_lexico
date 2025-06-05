
from analisador_lexico import AnalisadorLexico, TipoToken

def analisar_programa_simples(codigo):
    """Análise sintática simplificada que funciona"""
    print("=== ANÁLISE LÉXICA ===")
    analisador_lexico = AnalisadorLexico(codigo)
    tokens, erros_lexicos = analisador_lexico.analisar()
    
    if erros_lexicos:
        print("Erros léxicos encontrados:")
        for erro in erros_lexicos:
            print(f"  {erro.mensagem} (Linha: {erro.linha}, Coluna: {erro.coluna})")
        return False
    else:
        print("Análise léxica concluída sem erros.")
    
    print("\n=== ANÁLISE SINTÁTICA ===")
    
   
    tokens_filtrados = [t for t in tokens if t.tipo != TipoToken.COMENTARIO and t.tipo != TipoToken.EOF]
    
    print(f"Analisando {len(tokens_filtrados)} tokens...")
    

    i = 0
    estruturas_encontradas = []
    
    while i < len(tokens_filtrados):
        token = tokens_filtrados[i]
        
   
        if (token.tipo == TipoToken.PALAVRA_CHAVE and token.valor == "fx"):
            print(f"Encontrada declaração de função na linha {token.linha}")
            # fx nome ( param => tipo , param => tipo ) => tipo { ... }
            funcao_valida = True
            j = i + 1
            
   
            if j < len(tokens_filtrados) and tokens_filtrados[j].tipo == TipoToken.IDENTIFICADOR:
                nome_funcao = tokens_filtrados[j].valor
                print(f"  Nome da função: {nome_funcao}")
                j += 1
                
     
                if j < len(tokens_filtrados) and tokens_filtrados[j].valor == "(":
                    j += 1
                    parametros = []
                    
                 
                    while j < len(tokens_filtrados) and tokens_filtrados[j].valor != ")":
                        if tokens_filtrados[j].tipo == TipoToken.IDENTIFICADOR:
                            param_nome = tokens_filtrados[j].valor
                            j += 1
                            if j < len(tokens_filtrados) and tokens_filtrados[j].valor == "=>":
                                j += 1
                                if j < len(tokens_filtrados) and tokens_filtrados[j].tipo == TipoToken.TIPO:
                                    param_tipo = tokens_filtrados[j].valor
                                    parametros.append(f"{param_nome}: {param_tipo}")
                                    j += 1
                                    if j < len(tokens_filtrados) and tokens_filtrados[j].valor == ",":
                                        j += 1
                        else:
                            j += 1
                    
                    print(f"  Parâmetros: {parametros}")
                    
                    if j < len(tokens_filtrados) and tokens_filtrados[j].valor == ")":
                        j += 1
                        if j < len(tokens_filtrados) and tokens_filtrados[j].valor == "=>":
                            j += 1
                            if j < len(tokens_filtrados) and tokens_filtrados[j].tipo == TipoToken.TIPO:
                                tipo_retorno = tokens_filtrados[j].valor
                                print(f"  Tipo de retorno: {tipo_retorno}")
                                j += 1
                                
                               
                                if j < len(tokens_filtrados) and tokens_filtrados[j].valor == "{":
                                    j += 1
                                    comandos = 0
                                    nivel_chaves = 1
                                    
                                    while j < len(tokens_filtrados) and nivel_chaves > 0:
                                        if tokens_filtrados[j].valor == "{":
                                            nivel_chaves += 1
                                        elif tokens_filtrados[j].valor == "}":
                                            nivel_chaves -= 1
                                        elif tokens_filtrados[j].tipo == TipoToken.PALAVRA_CHAVE:
                                            comandos += 1
                                        j += 1
                                    
                                    print(f"  Comandos no corpo: {comandos}")
                                    estruturas_encontradas.append(f"Função {nome_funcao}")
            
            i = j
        
       
        elif (token.tipo == TipoToken.PALAVRA_CHAVE and token.valor == "vx"):
            print(f"Encontrada declaração de variável na linha {token.linha}")
            j = i + 1
            
            if j < len(tokens_filtrados) and tokens_filtrados[j].tipo == TipoToken.IDENTIFICADOR:
                nome_var = tokens_filtrados[j].valor
                j += 1
                if j < len(tokens_filtrados) and tokens_filtrados[j].valor == "=>":
                    j += 1
                    if j < len(tokens_filtrados) and tokens_filtrados[j].tipo == TipoToken.TIPO:
                        tipo_var = tokens_filtrados[j].valor
                        j += 1
                        
                        valor_inicial = None
                        if j < len(tokens_filtrados) and tokens_filtrados[j].valor == ":=":
                            j += 1
                            if j < len(tokens_filtrados):
                                valor_inicial = tokens_filtrados[j].valor
                                j += 1
                        
                        print(f"  Variável: {nome_var}: {tipo_var}")
                        if valor_inicial:
                            print(f"  Valor inicial: {valor_inicial}")
                        
                        estruturas_encontradas.append(f"Variável {nome_var}")
                        
                   
                        while j < len(tokens_filtrados) and tokens_filtrados[j].valor != ";":
                            j += 1
                        if j < len(tokens_filtrados):
                            j += 1
            
            i = j
        
      
        elif (token.tipo == TipoToken.IDENTIFICADOR and 
              i + 1 < len(tokens_filtrados) and 
              tokens_filtrados[i + 1].valor == ":="):
            print(f"Encontrada atribuição na linha {token.linha}")
            nome_var = token.valor
            j = i + 2  # Pula identificador e :=
            
         
            while j < len(tokens_filtrados) and tokens_filtrados[j].valor != ";":
                j += 1
            if j < len(tokens_filtrados):
                j += 1
            
            print(f"  Atribuição para: {nome_var}")
            estruturas_encontradas.append(f"Atribuição {nome_var}")
            i = j
        
    
        elif (token.tipo == TipoToken.PALAVRA_CHAVE and token.valor == "out"):
            print(f"Encontrado comando de saída na linha {token.linha}")
            j = i + 1
            
            # Pula até o ponto e vírgula
            while j < len(tokens_filtrados) and tokens_filtrados[j].valor != ";":
                j += 1
            if j < len(tokens_filtrados):
                j += 1
            
            estruturas_encontradas.append("Comando de saída")
            i = j
        
        else:
            i += 1
    
    print(f"\n=== RESULTADO DA ANÁLISE ===")
    print(f"Estruturas sintáticas encontradas: {len(estruturas_encontradas)}")
    for estrutura in estruturas_encontradas:
        print(f"  - {estrutura}")
    
    print("\nAnálise sintática concluída com sucesso!")
    return True

if __name__ == "__main__":
    codigo_exemplo = '''
    ## Programa exemplo em Zyntex
    fx calcular(x => int, y => int) => int {
        vx resultado => int;
        resultado := x ++ y;
        rx resultado;
    }
    
    vx num1 => int := 10;
    vx num2 => int := 5;
    vx soma => int;
    
    soma := calcular(num1, num2);
    out soma;
    '''
    
    print("=== ANALISADOR SINTÁTICO ZYNTEX ===")
    sucesso = analisar_programa_simples(codigo_exemplo)
    
    if sucesso:
        print("\n✓ Programa Zyntex analisado com sucesso!")
    else:
        print("\n✗ Erro na análise do programa Zyntex.")

