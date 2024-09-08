package cc.loac.itodo.data.models.enums

/**
 * 待办事项状态枚举类
 */
enum class TodoStatus {
    // 未开始
    UNSTARTED,

    // 进行中
    PROGRESSING,

    // 已完成
    COMPLETED;

    /**
     * 获取待办事项状态的描述
     */
    fun getDescription(): String {
        return when (this) {
            UNSTARTED -> "未开始"
            PROGRESSING -> "进行中"
            COMPLETED -> "已完成"
        }
    }
}