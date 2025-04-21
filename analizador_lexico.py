def analisar_lexico(codigo):
    i = 0
    tamanho = len(codigo)

    while i < tamanho:
        c = codigo[i]

        # Ignorar espaços em branco
        if c.isspace():
            i += 1
            continue
