package cc.loac.itodo.data.sql.dao

import cc.loac.itodo.data.models.enums.KeyValueEnum

/**
 * 键值对表操作接口
 */
interface KeyValueDao {

    /**
     * 设置字符串键
     * @param key 键
     * @param value 值
     */
    suspend fun set(key: KeyValueEnum, value: String): Boolean

    /**
     * 设置布尔值键
     * @param key 键
     * @param value 值
     */
    suspend fun set(key: KeyValueEnum, value: Boolean): Boolean

    /**
     * 设置整数型键
     * @param key 键
     * @param value 值
     */
    suspend fun set(key: KeyValueEnum, value: Int): Boolean

    /**
     * 获取字符串键
     * @param key 键
     * @param defaultValue 默认值
     * @return 值
     */
    suspend fun get(key: KeyValueEnum, defaultValue: String? = null): String?

    /**
     * 获取布尔值键
     * @param key 键
     * @param defaultValue 默认值
     * @return 值
     */
    suspend fun getBoolean(key: KeyValueEnum, defaultValue: Boolean? = null): Boolean?

    /**
     * 获取整数型键
     * @param key 键
     * @param defaultValue 默认值
     * @return 值
     */
    suspend fun getInt(key: KeyValueEnum, defaultValue: Int? = null): Int?
}