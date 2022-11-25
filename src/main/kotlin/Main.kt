import java.io.File
import java.lang.Exception

typealias Produto = Triple<Int, String, Int>
const val ID = 0
const val NOME = 1
const val QUANTIDADE = 2

// Comandos
const val AJUDA     = "AJUDA"
const val ADICIONAR = "ADICIONAR"
const val EDITAR    = "EDITAR"
const val ESTOCADOS = "ESTOCADOS"
const val TODOS     = "TODOS"
const val LER       = "LER"
const val GRAVAR    = "GRAVAR"
const val FINALIZAR = "FINALIZAR"
val comandos = listOf(AJUDA, ADICIONAR, EDITAR, ESTOCADOS, TODOS, LER, GRAVAR, FINALIZAR)

const val ARQUIVO = "estoque.txt"

var estoque = mutableListOf<Produto>()
var quantProdutos = estoque.size

fun main() {
    while (true) {
        val comando = lerComando()
        when (comando) {
            AJUDA     -> mostrarAjuda()
            ADICIONAR -> adicionar()
            EDITAR    -> editar()
            ESTOCADOS -> mostrarEstocados()
            TODOS     -> mostrarTodos()
            LER       -> estoque = ler("estoque.txt")
            GRAVAR    -> gravar("estoque.txt", estoque)
            FINALIZAR -> break
        }
    }
}

fun lerComando(): String {
    var comando = ""
    while (true) {
        println()
        print("Digite o comando ou AJUDA para ver todos os comandos: ")
        comando = readln().uppercase()
        if (comandos.contains(comando)) {
            break
        }
    }
    println()
    return comando
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
    val idProduto = quantProdutos + 1
    val nomeProduto = responderComTexto("Nome do produto? ")
    val quantProduto = responderComIntNaoNegativo("Quantidade do produto? ")
    estoque.add(Produto(idProduto, nomeProduto, quantProduto))
    quantProdutos++
    println("   Produto adicionado")
}

fun editar() {
    println("Comando: EDITAR")
    if (estoque.isEmpty()) {
        println("   O estoque está vazio")
        return
    }
    val nroProduto = responderComID("ID: ") - 1
    val produto: Produto = estoque[nroProduto]
    var (idProduto, nomeProduto, quantProduto) = produto
    if (respondeSN("Deseja editar o nome do produto (S/N)?") == "S") {
        nomeProduto = responderComTexto("Nome do produto: ")
    }
    if (respondeSN("Deseja editar a quantidade do produto (S/N)?") == "S") {
        quantProduto = responderComIntNaoNegativo("Quantidade: ")
    }
    estoque[nroProduto] = Produto(idProduto, nomeProduto, quantProduto)
}

fun mostrarTodos() {
    println("Comando: Listar TODOS")
    println()
    if (estoque.isEmpty()) {
        println("   O estoque está vazio.")
        return
    }
    println("      ID               PRODUTO               QUANT.")
    println("   ==================================================")
    estoque.forEach() { produto ->
        val (idProduto, nomeProduto, quantProduto) = produto
        println("   | %04d | %-30s | %6d |".format(idProduto, nomeProduto, quantProduto))
    }
    println("   ==================================================")
}

fun mostrarEstocados() {
    println("Comando: Listar ESTOCADOS")
    println()
    val estoqueComProdutos = estoque.filter { produto -> produto.third != 0 }
    if (estoqueComProdutos.isEmpty()) {
        println("   O estoque está vazio.")
        return
    }
    println("      ID               PRODUTO               QUANT.")
    println("   ==================================================")
    estoqueComProdutos.forEach() { produto ->
        val (idProduto, nomeProduto, quantProduto) = produto
        println("   | %04d | %-30s | %6d |".format(idProduto, nomeProduto, quantProduto))
    }
    println("   ==================================================")
}

fun gravar(arq: String, estoque: List<Produto>) {
    println("Comando: GRAVAR")
    val arquivo = File(arq)
    val estoqueSerializado = estoque.map { "${it.first}||${it.second}||${it.third}" }
    var textoSaida = ""
    estoqueSerializado.forEach { produtoSerializado ->
        textoSaida += if (textoSaida.isEmpty()) produtoSerializado else "\n$produtoSerializado"
    }
    arquivo.writeText(textoSaida)
    println("   O estoque foi gravado no arquivo $ARQUIVO")
}

fun ler(arq: String): MutableList<Produto> {
    println("Comando LER")
    val arquivo = File(arq)
    val estoqueSerializado = arquivo.readLines()
    val estoque = estoqueSerializado
        .map { it.split("||") }
        .map { Produto(it[ID].toInt(), it[NOME], it[QUANTIDADE].toInt()) }
    quantProdutos = estoque.size
    println("   Foram lidos $quantProdutos produtos do arquivo $ARQUIVO")
    return estoque.toMutableList()
}

fun respondeSN(pergunta: String): String {
    var resposta = ""
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
    var resposta = -1
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

fun responderComID(pergunta: String): Int {
    var resposta = -1
    while (true) {
        print("   $pergunta")
        try {
            resposta = readln().toInt()
        } catch (e: Exception) {
            println("ERRO -> Resposta deve ser um número inteiro entre 1 e ${quantProdutos}.")
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
