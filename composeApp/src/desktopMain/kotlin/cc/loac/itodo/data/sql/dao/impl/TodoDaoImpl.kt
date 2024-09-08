package cc.loac.itodo.data.sql.dao.impl

import cc.loac.itodo.data.models.Todo
import cc.loac.itodo.data.models.enums.TodoStatus
import cc.loac.itodo.data.sql.DatabaseSingleton.dbQuery
import cc.loac.itodo.data.sql.dao.TodoDao
import cc.loac.itodo.data.sql.tables.Todos
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList

/**
 * 代办事项表操作接口实现类
 */
class TodoDaoImpl : TodoDao {
    /**
     * 将数据库检索结果转为 [Todo] 代办事项数据类
     */
    private fun resultRowToTodo(row: ResultRow) = Todo(
        id = row[Todos.id],
        title = row[Todos.title],
        todo = row[Todos.todo],
        status = row[Todos.status],
        createTime = row[Todos.createTime],
        deadline = row[Todos.deadline],
        finishTime = row[Todos.finishTime],
        top = row[Todos.top],
        uploaded = row[Todos.uploaded]
    )

    /**
     * 添加代办事项
     * @param todoItem 代办事项
     */
    override suspend fun addTodo(todoItem: Todo): Todo? = dbQuery {
        Todos.insert {
            it[title] = todoItem.title
            it[todo] = todoItem.todo
            it[status] = todoItem.status
            it[createTime] = todoItem.createTime
            it[deadline] = todoItem.deadline
            it[finishTime] = todoItem.finishTime
            it[top] = todoItem.top
            it[uploaded] = todoItem.uploaded
        }.resultedValues?.singleOrNull()?.let {
            resultRowToTodo(it)
        }
    }

    /**
     * 删除代办事项
     * @param ids 代办事项 ID 集合
     */
    override suspend fun deleteTodo(ids: List<Long>): Boolean = dbQuery {
        Todos.deleteWhere {
            id inList ids
        } > 0
    }

    /**
     * 更新代办事项
     * @param todoItem 代办事项
     */
    override suspend fun updateTodo(todoItem: Todo): Boolean = dbQuery {
        Todos.update({
            Todos.id eq todoItem.id
        }) {
            it[title] = todoItem.title
            it[todo] = todoItem.todo
            it[status] = todoItem.status
            it[deadline] = todoItem.deadline
            it[finishTime] = todoItem.finishTime
            it[top] = todoItem.top
            it[uploaded] = todoItem.uploaded
        } > 0
    }

    /**
     * 获取所有代办事项
     * @param status 代办事项状态
     * @param isUpdate 是否已上传服务器
     * @param keyword 关键字
     */
    override suspend fun getAllTodo(status: TodoStatus?, isUpdate: Boolean?, keyword: String?): List<Todo> = dbQuery {
        val query = Todos.selectAll()
        status?.let { query.andWhere { Todos.status eq status } }
        isUpdate?.let { query.andWhere { Todos.uploaded eq isUpdate } }
        keyword?.let { query.andWhere { (Todos.title like "%$keyword%") or (Todos.todo like "%$keyword%") } }
        query.orderBy(Todos.createTime, SortOrder.DESC)
        query.map(::resultRowToTodo)
    }
}