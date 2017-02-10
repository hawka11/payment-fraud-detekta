import iot.jcypher.query.JcQuery
import iot.jcypher.query.writer.Format

fun printCypher(query: JcQuery, klass: Class<out Any>, format: Format): Unit =
        printCypher(query, klass.simpleName, format)

fun printCypher(query: JcQuery, title: String, format: Format) {
    println("QUERY: $title --------------------")
    // map to Cypher
    val cypher = iot.jcypher.util.Util.toCypher(query, format)
    println("CYPHER --------------------")
    println(cypher)

    // map to JSON
    val json = iot.jcypher.util.Util.toJSON(query, format)
    println("")
    println("JSON   --------------------")
    println(json)

    println("")
}