package example

import kotlinjs.common.jsonAs
import kotlin.browser.window

fun main(args: Array<String>) {
    val user = jsonAs<User>().apply {
        this.name = "Freewind"
    }
    window.alert(JSON.stringify(user))
    myHello()
}

interface User {
    var name: String
}

@JsName("myHello")
external fun myHello()