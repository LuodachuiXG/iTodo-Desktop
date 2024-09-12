package cc.loac.itodo.data.sql.dao.impl

import cc.loac.itodo.data.models.enums.KeyValueEnum
import cc.loac.itodo.data.sql.DatabaseSingleton.dbQuery
import cc.loac.itodo.data.sql.dao.KeyValueDao
import cc.loac.itodo.data.sql.tables.KeyValues
import cc.loac.itodo.util.isBoolean
import cc.loac.itodo.util.isInt
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

/**
 * 键值对表操作接口实现类
 */
class KeyValueDaoImpl : KeyValueDao {
    /**
     * 设置字符串键
     * @param key 键
     * @param value 值
     */
    override suspend fun set(key: KeyValueEnum, value: String): Boolean = dbQuery {
        // 设置前先尝试删除键
        KeyValues.deleteWhere { KeyValues.key eq key.name }

        val result = KeyValues.insert {
            it[KeyValues.key] = key.name
            it[KeyValues.value] = value
        }
        result.insertedCount > 0
    }

    /**
     * 设置布尔值键
     * @param key 键
     * @param value 值
     */
    override suspend fun set(key: KeyValueEnum, value: Boolean): Boolean {
        return set(key, value.toString())
    }

    /**
     * 设置整数型键
     * @param key 键
     * @param value 值
     */
    override suspend fun set(key: KeyValueEnum, value: Int): Boolean {
        return set(key, value.toString())
    }

    /**
     * 获取字符串键
     * @param key 键
     * @param defaultValue 默认值
     * @return 值
     */
    override suspend fun get(key: KeyValueEnum, defaultValue: String?): String? = dbQuery {
        KeyValues
            .selectAll()
            .where { KeyValues.key eq key.name }
            .firstOrNull()?.get(KeyValues.value) ?: defaultValue
    }

    /**
     * 获取布尔值键
     * @param key 键
     * @param defaultValue 默认值
     * @return 值
     */
    override suspend fun getBoolean(key: KeyValueEnum, defaultValue: Boolean?): Boolean? {
        val result = get(key) ?: return defaultValue
        if (!result.isBoolean()) return defaultValue
        return result.toBoolean()
    }

    /**
     * 获取整数型键
     * @param key 键
     * @param defaultValue 默认值
     * @return 值
     */
    override suspend fun getInt(key: KeyValueEnum, defaultValue: Int?): Int? {
        val result = get(key) ?: return defaultValue
        if (!result.isInt()) return defaultValue
        return result.toInt()
    }
}