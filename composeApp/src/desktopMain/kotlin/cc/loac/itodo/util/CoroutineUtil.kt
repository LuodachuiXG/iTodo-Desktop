package cc.loac.itodo.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val mainScope = CoroutineScope(Dispatchers.Main)

/**
 * 在主线程中执行协程
 * @param block 协程体
 */
fun launchMain(block: () -> Unit) {
    mainScope.launch {
        block()
    }
}