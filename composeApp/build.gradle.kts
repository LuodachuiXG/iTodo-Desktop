import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    jvm("desktop")
    
    sourceSets {
        val desktopMain by getting
        
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)

            // Exposed
            implementation(libs.exposed.core)
            implementation(libs.exposed.dao)
            implementation(libs.exposed.jdbc)

            // H2
            implementation(libs.sql.h2)

            // 根据 Seed Color 生成 Material3 主题颜色
            implementation(libs.material.kolor)

            // Compose Navigation
            implementation(libs.compose.navigation)

            // SLF4J
            implementation(libs.slf4j)

            // Koin 注入
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel.navigation)
            
            // Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.okhttp)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}


compose.desktop {
    application {
        mainClass = "cc.loac.itodo.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "cc.loac.itodo"
            packageVersion = "1.0.0"
        }
    }
}
