package cc.loac.itodo.data.models

import cc.loac.itodo.data.models.enums.TodoStatus

/**
 * 代办事项数据类
 * @param id 表 ID
 * @param title 标题
 * @param todo 代办事项
 * @param status 待办事项状态
 * @param createTime 创建时间戳
 * @param deadline 截止时间
 * @param finishTime 完成时间戳
 * @param top 是否置顶
 * @param uploaded 是否已上传服务器
 */
data class Todo(
    val id: Long = -1,
    val title: String? = null,
    val todo: String,
    val status: TodoStatus = TodoStatus.UNSTARTED,
    val createTime: Long = System.currentTimeMillis(),
    val deadline: Long? = null,
    val finishTime: Long? = null,
    val top: Boolean = false,
    val uploaded: Boolean = false
)
