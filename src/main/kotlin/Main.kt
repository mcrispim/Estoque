import java.io.File

typealias Produto = Triple<Int, String, Int>
const val ID = 0
const val NOME = 1
const val QUANTIDADE = 2

var estoque = mutableListOf<Produto>()
var nroProdutos = estoque.size

fun main(args: Array<String>) {
    while (true) {
        val comando = leComando()
        when (comando) {
            "adicionar" -> adicionar()
            "editar"    -> editar()
            "mostrar"   -> mostrar()
            "todos"     -> mostarTodos(estoque)
            "ler"       -> {
                estoque = ler("estoque.txt")
                nroProdutos = estoque.size
            }
            "gravar"    -> gravar("estoque.txt", estoque)
            "finalizar" -> break
        }
    }
}

fun adicionar() {
    TODO("Not yet implemented")
}

fun editar() {
    val nroProduto = lerNroProduto()
    val produto = estoque[nroProduto]
}

fun mostrar() {
    val nroProduto = lerNroProduto()
    val produto = estoque[nroProduto]
    println("   ID               PRODUTO               QUANT.")
    println("| %4d | %30s | %6d |".format(produto.first, produto.second, produto.third))
}

fun mostarTodos(estoque: MutableList<Produto>) {
    println("   ID               PRODUTO               QUANT.")
    estoque.forEach() {
        println("| %4d | %30s | %6d |")
    }
}

fun gravar(arq: String, estoque: List<Produto>) {
    val arquivo = File(arq)
    val estoqueSerializado = estoque.map { "${it.first}||${it.second}||${it.third}" }
    estoqueSerializado.forEach { arquivo.writeText(it) }
}

fun ler(arq: String): MutableList<Produto> {
    val arquivo = File(arq)
    val estoqueSerializado = arquivo.readLines()
    val estoque = estoqueSerializado
        .map { it.split("||") }
        .map { Produto(it[ID].toInt(), it[NOME], it[QUANTIDADE].toInt()) }
    return estoque.toMutableList()
}

fun lerNroProduto(): Int {
    // o estoque pode estar vazio
    // o número deve ser entre 1 e o número de produtos
    print("ID (: ")
    val id = readln().toInt()
    return id
}

