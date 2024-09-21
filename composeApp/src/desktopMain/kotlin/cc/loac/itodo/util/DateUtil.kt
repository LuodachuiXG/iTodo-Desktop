package cc.loac.itodo.util

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import kotlin.math.ceil

/**
 * 时间格式化
 * @param pattern 时间格式
 */
fun Long.format(pattern: String = "yyyy 年 MM 月 dd 日 HH:mm:ss"): String {
    val sdf = SimpleDateFormat(pattern)
    return sdf.format(this)
}

/**
 * 根据时间戳计算天数，向上取整
 */
fun Long.calculateDays(): Int {
    return ceil(this / 86400000.0).toInt()
}

/**
 * 获取今天早上 8 点的时间戳
 */
fun currentEightHourTimeStamp(): Long {
    val now = LocalDate.now()
    return now.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() + 8 * 60 * 60 * 1000
}