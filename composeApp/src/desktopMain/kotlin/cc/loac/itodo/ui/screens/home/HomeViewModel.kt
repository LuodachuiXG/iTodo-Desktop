package cc.loac.itodo.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.loac.itodo.data.models.Todo
import cc.loac.itodo.data.models.enums.TodoStatus
import cc.loac.itodo.data.sql.dao.TodoDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

/**
 * 首页 ViewModel
 */
class HomeViewModel : ViewModel() {
    // 待办事项 Dao
    private val todoDao: TodoDao by inject(TodoDao::class.java)

    // 代办事项列表
    private val _todoList = MutableStateFlow<List<Todo>>(emptyList())
    val todoList: StateFlow<List<Todo>> = _todoList


    /**
     * 获取待办事项列表
     * @param status 待办事项状态
     * @param isUpdate 是否已上传到服务器
     * @param keyword 关键字
     */
    fun getTodoList(
        status: TodoStatus? = null,
        isUpdate: Boolean? = null,
        keyword: String? = null
    ) {
        viewModelScope.launch {
            val result = todoDao.getAllTodo(
                status = status,
                isUpdate = isUpdate,
                keyword = keyword
            )
            _todoList.emit(result)
        }
    }

    /**
     * 插入一条待办事项
     * @param todo 待办事项
     */
    fun insertTodo(todo: Todo) {
        viewModelScope.launch {
            todoDao.addTodo(todo)

            // 插入后刷新
            getTodoList()
        }
    }

    /**
     * 更新一条待办事项
     * @param todo 待办事项
     * @param onComplete 更新完成回调
     */
    fun updateTodo(todo: Todo, onComplete: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            onComplete(todoDao.updateTodo(todo))
        }
    }

    /**
     * 删除一条待办事项
     * @param ids 待办事项 ID 集合
     * @param onComplete 删除完成回调
     */
    fun deleteTodo(ids: List<Long>, onComplete: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            onComplete(todoDao.deleteTodo(ids))
        }
    }
}