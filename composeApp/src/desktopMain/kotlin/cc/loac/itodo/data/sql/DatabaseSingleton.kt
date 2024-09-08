package cc.loac.itodo.data.sql

import cc.loac.itodo.data.sql.tables.KeyValues
import cc.loac.itodo.data.sql.tables.Todos
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * 数据库单例类
 */
object DatabaseSingleton {
    /**
     * 初始化数据库
     */
    fun init() {
        val driverClassName = "org.h2.Driver"
        val databaseName = "iTodo"
        val jdbcURL = "jdbc:h2:file:./.iTodo/db"
        val username = "iTodo"
        val password = "123456"
        var database = Database.connect(
            url = jdbcURL,
            driver = driverClassName,
            user = username,
            password = password
        )

        transaction(database) {
            if (!SchemaUtils.listDatabases().contains(databaseName)) {
                // 数据库不存在，创建数据库
                SchemaUtils.createDatabase(databaseName)
            }

            // 重新连接到连接到新创建的数据库
            database = Database.connect(
                url = jdbcURL,
                driver = driverClassName,
                user = username,
                password = password
            )

            // 开启事物，要么全部成功，要么回滚
            transaction(database) {
                // 所有表
                val tables = listOf(KeyValues, Todos)
                // 创建表
                SchemaUtils.create(*tables.toTypedArray())
            }
        }

    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}