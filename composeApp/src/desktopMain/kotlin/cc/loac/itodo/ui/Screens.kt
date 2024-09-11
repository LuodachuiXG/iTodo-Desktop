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
    ME("me", "我"),
    HOME("home", "首页"),
    THEME("theme", "主题")
}