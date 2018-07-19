package example

import kotlinjs.common.jsonAs
import kotlin.browser.window

fun main(args: Array<String>) {
    val user = jsonAs<User>().apply {
        this.name = "Freewind"
    }
    window.alert(JSON.stringify(user))
}

interface User {
    var name: String
}
