package detekta.payment.fraudresolver

data class ResolveResult(val score: Double, val detail: Any?)

fun aggregateResults(results: List<ResolveResult>): ResolveResult {
    val total = results.fold(0.0) { total, r2 -> total + r2.score }
    return ResolveResult(total, null)
}