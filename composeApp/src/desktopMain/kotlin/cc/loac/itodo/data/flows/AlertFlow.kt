package cc.loac.itodo.data.flows

import cc.loac.itodo.data.models.AlertFlowModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * 弹窗流
 */
object AlertFlow {

    private val mainScope = CoroutineScope(Dispatchers.Main)

    // Alert 消息流，用于在 APP 中显示全局弹窗
    val flow = MutableSharedFlow<AlertFlowModel>()

    /**
     * 发送一个 AlertFlowModel
     * @param alert Alert 配置信息
     */
    fun alert(alert: AlertFlowModel.() -> Unit) {
        mainScope.launch {
            flow.emit(AlertFlowModel().apply(alert))
        }
    }

}