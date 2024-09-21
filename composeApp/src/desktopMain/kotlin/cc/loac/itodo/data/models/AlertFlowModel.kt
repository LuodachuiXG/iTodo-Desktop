package cc.loac.itodo.data.models

/**
 * Alert 流数据类
 * 用于显示弹窗时发送该数据类给 Alert 流
 * @param title 标题
 * @param message 内容
 * @param cancelText 取消按钮文字（为 null 不显示取消按钮）
 * @param confirmText 确认按钮文字
 * @param onCancel 取消按钮点击事件
 * @param onConfirm 确认按钮点击事件
 */
data class AlertFlowModel (
    var title: String = "提示",
    var message: String = "",
    var cancelText: String? = "取消",
    var confirmText: String = "确定",
    var onCancel: () -> Unit = {},
    var onConfirm: () -> Unit = {}
)