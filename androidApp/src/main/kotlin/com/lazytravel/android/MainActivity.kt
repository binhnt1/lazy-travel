package com.lazytravel.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lazytravel.ui.components.layout.HeaderBar
import com.lazytravel.ui.components.layout.HeaderType

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFFAFAFA)
                ) {
                    TestScreen()
                }
            }
        }
    }
}

@Composable
fun TestScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Test HeaderBar - Greeting variant
        HeaderBar(
            type = HeaderType.Greeting(
                userName = "Minh",
                subtitle = "Sẵn sàng cho chuyến phiêu lưu tiếp theo?"
            )
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "🎉 Compose is working!",
                style = MaterialTheme.typography.headlineMedium
            )

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "HeaderBar Component Test",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "The HeaderBar component from shared module is rendering correctly!",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Text(
                text = "✅ Kotlin 2.2.21",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "✅ Compose Multiplatform 1.9.3",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "✅ Shared module components working",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
