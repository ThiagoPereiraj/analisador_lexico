def analisar_lexico(codigo):
    palavras_chave = {"if", "else", "while", "return", "for", "int", "float", "char", "void", "double"}
    operadores = {'+', '-', '*', '/', '=', '==', '!=', '<', '<=', '>', '>=', '&&', '||'}
    simbolos = {'(', ')', '{', '}', ';', ',', '[', ']'}

    i = 0
    tamanho = len(codigo)

    while i < tamanho:
        c = codigo[i]

        # Ignorar espaços em branco
        if c.isspace():
            i += 1
            continue

        # Números
        if c.isdigit():
            numero = ''
            tem_ponto = False
            while i < tamanho and (codigo[i].isdigit() or codigo[i] == '.'):
                if codigo[i] == '.':
                    if tem_ponto:
                        break  # segundo ponto
                    if i + 1 >= tamanho or not codigo[i + 1].isdigit():
                        break  # ponto não seguido por dígito
                    tem_ponto = True
                numero += codigo[i]
                i += 1
            print(f"TOKEN: NUMERO {numero}")
            continue

        # Identificadores e palavras-chave
        if c.isalpha() or c == '_':
            identificador = ''
            while i < tamanho and (codigo[i].isalnum() or codigo[i] == '_'):
                identificador += codigo[i]
                i += 1
            if identificador in palavras_chave:
                print(f"TOKEN: PALAVRA_CHAVE {identificador}")
            else:
                print(f"TOKEN: IDENTIFICADOR {identificador}")
            continue

        # Operadores e símbolos
        dois_caracteres = codigo[i:i+2] if i + 1 < tamanho else ''
        if dois_caracteres in operadores:
            print(f"TOKEN: OPERADOR {dois_caracteres}")
            i += 2
            continue
        elif c in operadores:
            print(f"TOKEN: OPERADOR {c}")
            i += 1
            continue
        elif c in simbolos:
            print(f"TOKEN: SIMBOLO {c}")
            i += 1
            continue

        # Caractere desconhecido
        print(f"TOKEN: DESCONHECIDO {c}")
        i += 1


# Exemplo de uso
codigo = "if (x == 10.9.) return x + 1;"
analisar_lexico(codigo)
