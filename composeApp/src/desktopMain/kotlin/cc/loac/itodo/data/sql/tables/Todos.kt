package cc.loac.itodo.data.sql.tables

import cc.loac.itodo.data.models.enums.TodoStatus
import org.jetbrains.exposed.sql.Table

/**
 * 代办事项表
 */
object Todos : Table("todo") {
    /** 表 ID **/
    val id = long("id").autoIncrement()

    /** 标题 **/
    val title = varchar("title", 128).nullable()

    /** 待办事项 **/
    val todo = text("todo")

    /** 待办事项状态 **/
    val status = enumerationByName("status", 24, TodoStatus::class)

    /** 待办事项创建时间戳 **/
    val createTime = long("create_time")

    /** 代办事项截止时间 **/
    val deadline = long("deadline").nullable()

    /** 待办事项完成时间戳 **/
    val finishTime = long("finish_time").nullable()

    /** 是否置顶 **/
    val top = bool("top")

    /** 是否已上传服务器 **/
    val uploaded = bool("uploaded")

    override val primaryKey = PrimaryKey(id)
}