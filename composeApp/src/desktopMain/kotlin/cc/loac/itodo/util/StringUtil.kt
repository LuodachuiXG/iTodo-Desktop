package cc.loac.itodo.util

import java.util.regex.Pattern


/**
 * String 扩展函数
 * 验证正则表达式
 * @param regex 正则表达式
 * @param find 是否是查找模式
 */
fun String.matches(regex: String, find: Boolean = false): Boolean {
    if (this.isEmpty()) return false
    val pattern = Pattern.compile(regex)
    val matcher = pattern.matcher(this)
    return if (find) {
        matcher.find()
    } else {
        matcher.matches()
    }
}

/**
 * String 扩展函数
 * 判断文本是否是合法的 URL
 */
fun String.isUrl(): Boolean {
    // 判断标准为 http:// 或 https:// 开头，并且最少存在一个 .
    return this.matches("^(http|https)://[^.]+\\.[^/]*\$")
}

/**
 * String 扩展函数
 * 验证是否是整数
 */
fun String.isInt(): Boolean {
    return this.matches("^\\d+")
}

/**
 * String 扩展函数
 * 验证是否是布尔值
 */
fun String.isBoolean(): Boolean {
    return this.matches("^(true|false)$")
}

/**
 * String 扩展函数
 * 验证是否是 Float
 */
fun String.isFloat(): Boolean {
    return this.toFloatOrNull() != null
}