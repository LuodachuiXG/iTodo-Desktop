package cc.loac.itodo.util

/**
 * 点击防抖工具类
 */
object ShakeUtil {
    private const val DEFAULT_TIME = 500L
    private var lastClickTime: Long? = null

    /**
     * 更新最后点击时间
     */
    fun updateLastClickTime() {
        lastClickTime = System.currentTimeMillis()
    }

    /**
     * 判断是否可以点击
     * @param time 默认 500ms
     */
    fun isCanClick(
        time: Long = DEFAULT_TIME
    ): Boolean {
        val currentTime = System.currentTimeMillis()
        return lastClickTime == null || currentTime - lastClickTime!! > DEFAULT_TIME
    }
}

/**
 * 点击防抖
 * @param block 点击回调
 */
fun canClick(
    block: () -> Unit
) {
    if (ShakeUtil.isCanClick()) {
        block()
        ShakeUtil.updateLastClickTime()
    }
}