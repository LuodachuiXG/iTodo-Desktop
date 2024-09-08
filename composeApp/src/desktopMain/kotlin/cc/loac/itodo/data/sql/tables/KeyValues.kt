package cc.loac.itodo.data.sql.tables

import org.jetbrains.exposed.sql.Table

/**
 * 键值对表
 */
object KeyValues : Table("key_values") {
    /** 键 **/
    val key = varchar("key", 128).uniqueIndex()

    /** 值 **/
    val value = text("value")

    override val primaryKey = PrimaryKey(key)
}