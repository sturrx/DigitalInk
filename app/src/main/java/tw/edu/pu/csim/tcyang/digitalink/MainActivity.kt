package tw.edu.pu.csim.tcyang.digitalink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import tw.edu.pu.csim.tcyang.digitalink.ui.theme.DigitalInkTheme
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.material3.Button
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DigitalInkTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TestCanvas(
                        modifier = Modifier.padding(innerPadding)
                    )
                    /*DigitalInk(
                        modifier = Modifier.padding(innerPadding)
                    )*/
                }
            }
        }
    }
}

@Composable
fun TestCanvas(modifier: Modifier = Modifier) {
    val minionsImage: ImageBitmap = ImageBitmap.imageResource(id = R.drawable.minions)

    var isPlaying by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0f) }

    var minionsOffset by remember { mutableStateOf(Offset(-1f, -1f)) }

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            while (isPlaying) {
                withFrameNanos { _ ->
                    progress += 0.005f
                    if (progress > 2.5f) {
                        progress = 0f
                    }
                }
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            drawCircle(
                Color.Magenta,
                100f,
                Offset(100f, 100f)
            )

            drawRect(
                Color.Blue,
                Offset(300f, 200f),
                Size(200f, 200f),
                style = Stroke(width = 5f)
            )

            val startPoint = Offset(200f, 800f)
            val endPoint = Offset(size.width, size.height)

            drawLine(
                Color.Red,
                startPoint,
                endPoint,
                strokeWidth = 10f
            )

            if (minionsOffset.x < 0f) {
                minionsOffset = Offset((size.width - minionsImage.width) / 2f, (size.height - minionsImage.height) / 2f)
            }

            var drawX = minionsOffset.x
            var drawY = minionsOffset.y

            if (isPlaying) {
                val direction = (endPoint - startPoint).let {
                    val length = kotlin.math.sqrt(it.x * it.x + it.y * it.y)
                    if (length > 0f) Offset(it.x / length, it.y / length) else Offset(0f, 0f)
                }

                val initialPos = Offset((size.width - minionsImage.width) / 2f, (size.height - minionsImage.height) / 2f)
                val distance = progress * 900f
                val currentPos = initialPos + direction * distance

                drawX = currentPos.x
                drawY = currentPos.y

                if (drawX > size.width || drawY > size.height || drawX < -150f || drawY < -150f) {
                    drawX = 0f
                    drawY = 0f
                    isPlaying = false
                    progress = 0f
                }

                minionsOffset = Offset(drawX, drawY)
            }

            translate(drawX, drawY) {
                drawImage(minionsImage)
            }
        }

        Button(
            onClick = {
                isPlaying = !isPlaying
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(if (isPlaying) "暫停遊戲" else "繼續遊戲")
        }
    }
}

/*@Composable
fun DigitalInk(modifier: Modifier = Modifier) {
    var path by remember { mutableStateOf(Path() )}
    var recognizedText by remember {
        mutableStateOf("請在畫布區手寫，再按「開始辨識」按鈕")
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(
            modifier = Modifier
                .size(300.dp)
                .border(2.dp, Color.Gray)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            val x = offset.x
                            val y = offset.y
                            path.moveTo(x, y)
                        },

                        onDrag = { change, _ ->
                            change.consume()
                            val x = change.position.x
                            val y = change.position.y
                            path.lineTo(x, y)
                            val tempPath = Path()
                            tempPath.addPath(path)
                            path = tempPath
                        },

                        onDragEnd = {}
                    )
                }
        ) {
        drawPath(path, Color.Black, 1f, Stroke(width = 10f))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(onClick = {
            }) {
                Text("開始辨識")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {
                path = Path()
            }){
                Text("清除")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(recognizedText)
    }
}*/