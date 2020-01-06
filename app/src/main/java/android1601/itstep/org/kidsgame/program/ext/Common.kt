package android1601.itstep.org.kidsgame.program.ext

inline fun <A, B> ifAllNotNull(a: A?, b: B?, block: (A, B) -> Unit) {
    if (a != null && b != null) block(a, b)
}

inline fun <reified T : Any> Any?.letIfIs(run: (T) -> Unit) {
    if (this is T) run(this)
}

inline fun runIf(predicate: Boolean, run: () -> Unit) {
    if (predicate) run()
}

inline fun <T> tryOr(default: T, get: () -> T): T = try {
    get()
} catch (e: Exception) {
    e.printStackTrace()
    default
}

inline fun <T> tryOrNull(get: () -> T) = tryOr(null, get)

inline fun tryTo(run: () -> Unit): Unit = try {
    run()
} catch (e: Exception) {
    e.printStackTrace()
}