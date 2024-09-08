package cc.loac.itodo.data.sql.dao

import cc.loac.itodo.data.models.Todo
import cc.loac.itodo.data.models.enums.TodoStatus

/**
 * 代办事项表操作接口
 */
interface TodoDao {

    /**
     * 添加代办事项
     * @param todoItem 代办事项
     */
    suspend fun addTodo(todoItem: Todo): Todo?

    /**
     * 删除代办事项
     * @param ids 代办事项 ID 集合
     */
    suspend fun deleteTodo(ids: List<Long>): Boolean

    /**
     * 更新代办事项
     * @param todoItem 代办事项
     */
    suspend fun updateTodo(todoItem: Todo): Boolean

    /**
     * 获取所有代办事项
     * @param status 代办事项状态
     * @param isUpdate 是否已上传服务器
     * @param keyword 关键字
     */
    suspend fun getAllTodo(
        status: TodoStatus? = null,
        isUpdate: Boolean? = null,
        keyword: String? = null
    ): List<Todo>
}