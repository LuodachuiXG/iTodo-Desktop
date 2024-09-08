package cc.loac.itodo.ui

/**
 * 所有页面枚举类
 * @param route 路由名
 * @param screenName 页面名
 */
enum class Screens(
    val route: String,
    val screenName: String
) {
    LOGIN("login", "登录"),
    ME("ME", "我"),
    HOME("HOME", "首页")
}