def analisar_lexico(codigo):
    i = 0
    tamanho = len(codigo)

    while i < tamanho:
        c = codigo[i]

        # Ignorar espaços em branco
        if c.isspace():
            i += 1
            continue
  
        # Numeros  
        if c.isdigit():
            numero = ''
            tem_ponto = False
            while i < tamanho and (codigo[i].isdigit() or codigo[i] == '.'):
                if codigo[i] == '.':
                    if tem_ponto:
                        break  # segundo ponto: 9..7 ou 7.3.2 por exemplo não é válido
                    if i + 1 >= tamanho or not codigo[i + 1].isdigit():
                        break  # ponto não seguido por dígito 9. ou 9.3. por exemplo não é válido
                    tem_ponto = True
                numero += codigo[i]
                i += 1
            print(f"TOKEN: NUMERO {numero}")

        # Identificadores e palavras-chave




        # Operadores e símbolos
        




        # Caractere desconhecido
        print(f"TOKEN: DESCONHECIDO {c}")
        i += 1
              

codigo = "if (x == 10.9.) return x + 1;"
analisar_lexico(codigo)
