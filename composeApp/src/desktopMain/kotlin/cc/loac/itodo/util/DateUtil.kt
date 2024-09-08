package cc.loac.itodo.util

import java.text.SimpleDateFormat

/**
 * 时间格式化
 * @param pattern 时间格式
 */
fun Long.format(pattern: String = "yyyy 年 MM 月 dd 日 HH:mm:ss"): String {
    val sdf = SimpleDateFormat(pattern)
    return sdf.format(this)
}