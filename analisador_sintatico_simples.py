from analisador_lexico import AnalisadorLexico, TipoToken

def analisar_programa_simples(codigo):
    print("=== ANÁLISE LÉXICA ===")
    analisador = AnalisadorLexico(codigo)
    tokens, erros = analisador.analisar()

    # Tratamento de erros léxicos
    if erros:
        print("Erros léxicos encontrados:")
        for erro in erros:
            print(f"  {erro.mensagem} (Linha: {erro.linha}, Coluna: {erro.coluna})")
        return False

    print("Análise léxica concluída sem erros.")
    tokens_filtrados = [t for t in tokens if t.tipo != TipoToken.COMENTARIO and t.tipo != TipoToken.EOF]

    print("\n=== ANÁLISE SINTÁTICA ===")
    i = 0
    estruturas = []

    while i < len(tokens_filtrados):
        token = tokens_filtrados[i]

        # Identificação de função fx
        if token.tipo == TipoToken.PALAVRA_CHAVE and token.valor == "fx":
            j = i + 1
            # Nome da função
            if j < len(tokens_filtrados) and tokens_filtrados[j].tipo == TipoToken.IDENTIFICADOR:
                nome = tokens_filtrados[j].valor
                j += 1
                # Parêntese de início dos parâmetros
                if j < len(tokens_filtrados) and tokens_filtrados[j].valor == "(":
                    j += 1
                    parametros = []
                    # Leitura dos parâmetros
                    while j < len(tokens_filtrados) and tokens_filtrados[j].valor != ")":
                        if tokens_filtrados[j].tipo == TipoToken.IDENTIFICADOR:
                            nome_param = tokens_filtrados[j].valor
                            j += 1
                            if j < len(tokens_filtrados) and tokens_filtrados[j].valor == "=>":
                                j += 1
                                if j < len(tokens_filtrados) and tokens_filtrados[j].tipo == TipoToken.TIPO:
                                    tipo_param = tokens_filtrados[j].valor
                                    parametros.append(f"{nome_param}: {tipo_param}")
                                    j += 1
                                    # Separador de parâmetros
                                    if j < len(tokens_filtrados) and tokens_filtrados[j].valor == ",":
                                        j += 1
                                else:
                                    break
                            else:
                                break
                        else:
                            j += 1
                    # Parêntese de fim dos parâmetros
                    if j < len(tokens_filtrados) and tokens_filtrados[j].valor == ")":
                        j += 1
                        # Tipo de retorno
                        if j < len(tokens_filtrados) and tokens_filtrados[j].valor == "=>":
                            j += 1
                            if j < len(tokens_filtrados) and tokens_filtrados[j].tipo == TipoToken.TIPO:
                                tipo_retorno = tokens_filtrados[j].valor
                                j += 1
                                # Corpo da função
                                if j < len(tokens_filtrados) and tokens_filtrados[j].valor == "{":
                                    j += 1
                                    nivel = 1
                                    comandos = 0
                                    # Contagem de comandos no corpo
                                    while j < len(tokens_filtrados) and nivel > 0:
                                        if tokens_filtrados[j].valor == "{":
                                            nivel += 1
                                        elif tokens_filtrados[j].valor == "}":
                                            nivel -= 1
                                        elif tokens_filtrados[j].tipo == TipoToken.PALAVRA_CHAVE:
                                            comandos += 1
                                        j += 1
                                    print(f"Função {nome} (params: {parametros}) => {tipo_retorno} com {comandos} comandos.")
                                    estruturas.append(f"Função {nome}")
            i = j

        # Identificação de variável vx
        elif token.tipo == TipoToken.PALAVRA_CHAVE and token.valor == "vx":
            j = i + 1
            # Nome da variável
            if j < len(tokens_filtrados) and tokens_filtrados[j].tipo == TipoToken.IDENTIFICADOR:
                nome = tokens_filtrados[j].valor
                j += 1
                if j < len(tokens_filtrados) and tokens_filtrados[j].valor == "=>":
                    j += 1
                    if j < len(tokens_filtrados) and tokens_filtrados[j].tipo == TipoToken.TIPO:
                        tipo = tokens_filtrados[j].valor
                        j += 1
                        # Atribuição inicial
                        if j < len(tokens_filtrados) and tokens_filtrados[j].valor == ":=":
                            j += 1
                            if j < len(tokens_filtrados):
                                valor = tokens_filtrados[j].valor
                                j += 1
                                print(f"Variável {nome}: {tipo} = {valor}")
                        else:
                            print(f"Variável {nome}: {tipo}")
                        estruturas.append(f"Variável {nome}")
                        # Pular até o fim da declaração
                        while j < len(tokens_filtrados) and tokens_filtrados[j].valor != ";":
                            j += 1
                        j += 1
            i = j

        # Atribuições fora da declaração de variável
        elif token.tipo == TipoToken.IDENTIFICADOR and i + 1 < len(tokens_filtrados) and tokens_filtrados[i + 1].valor == ":=":
            nome = token.valor
            j = i + 2
            while j < len(tokens_filtrados) and tokens_filtrados[j].valor != ";":
                j += 1
            print(f"Atribuição para {nome}")
            estruturas.append(f"Atribuição {nome}")
            j += 1
            i = j

        # Comando de saída (out)
        elif token.tipo == TipoToken.PALAVRA_CHAVE and token.valor == "out":
            print(f"Comando de saída na linha {token.linha}")
            j = i + 1
            while j < len(tokens_filtrados) and tokens_filtrados[j].valor != ";":
                j += 1
            j += 1
            estruturas.append("Comando de saída")
            i = j

        # Qualquer outro caso
        else:
            i += 1

    # Resultado final
    print("\n=== RESULTADO DA ANÁLISE ===")
    for e in estruturas:
        print(f"  - {e}")
    return True
