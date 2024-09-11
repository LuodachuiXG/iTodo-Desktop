package cc.loac.itodo.data.models.enums

import androidx.compose.ui.unit.Dp
import cc.loac.itodo.ui.theme.SCREEN_EXTRA_WIDE
import cc.loac.itodo.ui.theme.SCREEN_ULTRA_WIDE
import cc.loac.itodo.ui.theme.SCREEN_WIDE

/**
 * 屏幕宽度枚举
 */
enum class ScreenWidth {
    // 正常
    NORMAL,
    // 宽屏
    WIDE,
    // 超宽屏
    EXTRA_WIDE,
    // 巨宽屏
    ULTRA_WIDE;


    companion object {
        /**
         * 获取屏幕宽度枚举
         * @param width 屏幕宽度
         */
        fun of(width: Dp): ScreenWidth {
            return if (width < SCREEN_WIDE) {
                NORMAL
            } else if (width < SCREEN_EXTRA_WIDE) {
                WIDE
            } else if (width < SCREEN_ULTRA_WIDE) {
                EXTRA_WIDE
            } else {
                ULTRA_WIDE
            }
        }
    }
}