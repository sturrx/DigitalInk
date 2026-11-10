package tw.edu.pu.csim.tcyang.digitalink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import tw.edu.pu.csim.tcyang.digitalink.ui.theme.DigitalInkTheme
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DigitalInkTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    /*TestCanvas(
                        modifier = Modifier.padding(innerPadding)
                    )*/
                    DigitalInk(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

/*@Composable
fun TestCanvas(modifier: Modifier = Modifier) {
    val minionsImage: ImageBitmap =
        ImageBitmap.imageResource(id = R.drawable.minions)
    var path by remember { mutableStateOf(Path() )}

    Canvas(
        modifier = modifier.fillMaxSize()
        .pointerInput(Unit) {
            detectDragGestures(
                onDragStart = { offset ->
                    val x = offset.x
                    val y = offset.y

                    path.reset()
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

                onDragEnd = { /* 手指離開 */ }
            )
        }
    )
    {
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

        drawLine(
            Color.Red,
            Offset(200f, 800f),
            Offset(size.width, size.height),
            strokeWidth = 10f
        )

        val minionsWidth = minionsImage.width
        val minionsHeight = minionsImage.height
        val offsetX = (size.width - minionsWidth) / 2f
        val offsetY = (size.height - minionsHeight) / 2f
        translate(offsetX, offsetY) {
            drawImage(minionsImage)
        }

        drawPath(path, Color.Green, 1f, Stroke(width = 10f))
    }
}*/

@Composable
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

                        onDragEnd = { /* 手指離開 */ }
                    )
                }
        ) {
        drawPath(path, Color.Black, 1f, Stroke(width = 10f))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            // 開始辨識
            Button(onClick = {
            }) {
                Text("開始辨識")
            }
            Spacer(modifier = Modifier.width(16.dp))
            // 清除畫布
            Button(onClick = {
                path = Path()  // 指派新物件位址，立刻觸發 Canvas 重繪並清空畫面
            }){
                Text("清除")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(recognizedText)
    }
}