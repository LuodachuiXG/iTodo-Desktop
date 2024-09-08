package cc.loac.itodo.data.koin

import cc.loac.itodo.data.sql.dao.KeyValueDao
import cc.loac.itodo.data.sql.dao.TodoDao
import cc.loac.itodo.data.sql.dao.impl.KeyValueDaoImpl
import cc.loac.itodo.data.sql.dao.impl.TodoDaoImpl
import cc.loac.itodo.ui.screens.home.HomeViewModel
import cc.loac.itodo.ui.screens.login.LoginViewModel
import cc.loac.itodo.ui.screens.me.MeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.koinApplication
import org.koin.dsl.module

/**
 * 配置 Koin 注入
 */
fun koinConfiguration() = koinApplication {
    modules(module {
        // 键值对表操作接口
        single<KeyValueDao> { KeyValueDaoImpl() }

        // 代办事项表操作接口
        single<TodoDao> { TodoDaoImpl() }

        // 首页 ViewModel
        viewModelOf(::HomeViewModel)

        // 我 ViewModel
        viewModelOf(::MeViewModel)

        // 登录 ViewModel
        viewModelOf(::LoginViewModel)
    })
}