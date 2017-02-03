package other.sandbox.string

fun main(args: Array<String>) {

    reverse("abcd")
    reverse("abc")
    reverse("ab")
    reverse("a")
    reverse("")

    val reversed = "fdsa".toCharArray().foldRight("") { c, s -> "$s$c" }
    println("folded reversed: $reversed")
}

private fun reverse(i: String) {
    val input = i.toCharArray()

    var l = 0
    var r = input.size - 1

    while (l < r) {
        val tmp = input[r]
        input[r] = input[l]
        input[l] = tmp
        l++;r--
    }

    val reversed = String(input)

    println("input is: $i")
    println("reversed is: $reversed\n")
}