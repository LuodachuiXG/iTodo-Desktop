package cc.loac.itodo.ui.screens.theme

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.loac.itodo.data.models.enums.KeyValueEnum
import cc.loac.itodo.data.sql.dao.KeyValueDao
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

/**
 * 主题 ViewModel
 */
class ThemeViewModel : ViewModel() {


    // 键值对表
    private val keyValueDao: KeyValueDao by inject(KeyValueDao::class.java)

    /**
     * 设置主题种子颜色
     * @param color 颜色
     */
    fun setThemeSeedColor(color: Color) {
        viewModelScope.launch {
            keyValueDao.set(KeyValueEnum.THEME_COLOR_SEED, color.value.toString())
        }
    }
}