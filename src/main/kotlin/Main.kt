package com.exemplo.estoque

import java.lang.Exception
import java.security.InvalidParameterException

const val ARQUIVO = "estoque.txt"

var estoque = Estoque()

fun main() {
    while (true) {
        when (Comando.lerComando()) {
            Comando.AJUDA -> mostrarAjuda()
            Comando.ADICIONAR -> adicionar()
            Comando.EDITAR -> editar()
            Comando.ESTOCADOS -> mostrarEstocados()
            Comando.TODOS -> mostrarTodos()
            Comando.LER -> ler(ARQUIVO)
            Comando.GRAVAR -> gravar(ARQUIVO)
            Comando.FINALIZAR -> break
        }
    }
}

fun mostrarAjuda() {
    println("""
        Comandos disponíveis:
            Adicionar - adiciona produtos ao estoque
            Editar - altera o nome de um produto e/ou a quantidade de itens do mesmo no estoque
            Estocados - mostra os produtos com quantidade maior que 0 no estoque
            Todos - mostra a relação de todos os produtos no estoque
            Ler - carrega o estoque do arquivo <estoque.txt>
            Gravar - grava o conteúdo do estoque no arquivo <estoque.txt>
            Ajuda - mostra este texto
            Finalizar - encerra a execução do programa
        
    """.trimIndent())
}

fun adicionar() {
    println("Comando: ADICIONAR")
    val nomeProduto = responderComTexto("Nome do produto? ")
    val quantProduto = responderComIntNaoNegativo("Quantidade do produto? ")
    estoque.adicionarProduto(Produto(nomeProduto, quantProduto))
    println("   Produto adicionado")
}

fun editar() {
    println("Comando: EDITAR")
    if (estoque.estaVazio()) {
        println("   Não existem produtos cadastrados no estoque.")
        return
    }
    val nroProduto = responderComID("ID: ", estoque.contarTodosProdutos())
    val produto: Produto = try {
        estoque.encontrarProduto(nroProduto)
    } catch (e: InvalidParameterException) {
        println("   ${e.message} <== Erro: Produto inexistente")
        return
    }
    println("   Produto: $produto")
    var novoNomeProduto = produto.nome
    if (respondeSN("Deseja editar o nome do produto (S/N)?") == "S") {
        novoNomeProduto = responderComTexto("Nome do produto: ")
    }
    var novaQuantProduto = produto.quantidade
    if (respondeSN("Deseja editar a quantidade do produto (S/N)?") == "S") {
        novaQuantProduto = responderComIntNaoNegativo("Quantidade: ")
    }
    estoque.substituirProduto(produto.id, novoNomeProduto, novaQuantProduto)
    println("   Produto atualizado")
}

fun mostrarTodos() {
    println("Comando: Listar TODOS")
    println()
    if (estoque.estaVazio()) {
        println("   O estoque está vazio.")
        return
    }
    println("      ID               PRODUTO               QUANT.")
    println("   ==================================================")
    estoque.listarTodosProdutos().forEach { produto ->
        println("   | %04d | %-30s | %6d |".format(produto.id, produto.nome, produto.quantidade))
    }
    println("   ==================================================")
}

fun mostrarEstocados() {
    println("Comando: Listar ESTOCADOS")
    println()
    val estoqueReal = estoque.listarProdutosNoEstoque()
    if (estoqueReal.isEmpty()) {
        println("   O estoque está vazio.")
        return
    }
    println("      ID               PRODUTO               QUANT.")
    println("   ==================================================")
    estoqueReal.forEach { produto ->
        println("   | %04d | %-30s | %6d |".format(produto.id, produto.nome, produto.quantidade))
    }
    println("   ==================================================")
}

fun gravar(arq: String = ARQUIVO) {
    println("Comando: GRAVAR")
    estoque.gravar(arq)
    println("   O estoque foi gravado no arquivo $ARQUIVO")
}

fun ler(arq: String) {
    println("Comando LER")
    estoque.ler(arq)
    println("   Foram lidos ${estoque.contarTodosProdutos()} produtos do arquivo $ARQUIVO")
    return
}

fun respondeSN(pergunta: String): String {
    var resposta: String
    while (true) {
        print("   $pergunta ")
        resposta = readln().uppercase()
        if (resposta == "S" || resposta == "N") {
            break
        }
    }
    return resposta
}

fun responderComTexto(pergunta: String): String {
    var resposta = ""
    while (resposta.isEmpty()) {
        print("   $pergunta")
        resposta = readln()
    }
    return resposta
}

fun responderComIntNaoNegativo(pergunta: String, max: Int = 99999): Int {
    var resposta: Int
    while (true) {
        print("   $pergunta")
        try {
            resposta = readln().toInt()
        } catch (e: Exception) {
            println("ERRO -> Resposta deve ser um número inteiro entre 0 e $max.")
            continue
        }
        if (resposta < 0) {
            println("ERRO -> valor deve ser maior ou igual a 0.")
            continue
        }
        if (resposta > max) {
            println("ERRO -> valor deve ser menor ou igual a $max.")
            continue
        }
        break
    }
    return resposta
}

fun responderComID(pergunta: String, idMax:Int): Int {
    var resposta: Int
    while (true) {
        print("   $pergunta")
        try {
            resposta = readln().toInt()
        } catch (e: Exception) {
            println("ERRO -> Resposta deve ser um número inteiro entre 1 e ${idMax}.")
            continue
        }
        if (resposta < 1) {
            println("ERRO -> valor deve ser maior ou igual a 1.")
            continue
        }
        break
    }
    return resposta
}
