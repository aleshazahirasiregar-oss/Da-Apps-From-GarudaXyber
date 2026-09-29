package com.note.styles.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.note.styles.data.model.PatternTypes

fun Modifier.patternBackground(
    patternType: String,
    patternColor: Color,
    isDark: Boolean = false
): Modifier = this.drawBehind {
    val alpha = if (isDark) 0.18f else 0.12f
    val strokeColor = patternColor.copy(alpha = alpha)

    when (patternType) {
        PatternTypes.RULED -> {
            val lineSpacing = 28.dp.toPx()
            val startY = 40.dp.toPx()
            var y = startY
            while (y < size.height) {
                drawLine(
                    color = strokeColor,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1.dp.toPx()
                )
                y += lineSpacing
            }
            // Vertical margin line
            drawLine(
                color = patternColor.copy(alpha = if (isDark) 0.25f else 0.18f),
                start = Offset(32.dp.toPx(), 0f),
                end = Offset(32.dp.toPx(), size.height),
                strokeWidth = 1.2.dp.toPx()
            )
        }

        PatternTypes.DOTS -> {
            val dotSpacing = 20.dp.toPx()
            val radius = 1.3.dp.toPx()
            var x = dotSpacing
            while (x < size.width) {
                var y = dotSpacing
                while (y < size.height) {
                    drawCircle(
                        color = strokeColor,
                        radius = radius,
                        center = Offset(x, y)
                    )
                    y += dotSpacing
                }
                x += dotSpacing
            }
        }

        PatternTypes.GRAPH -> {
            val step = 18.dp.toPx()
            var x = step
            while (x < size.width) {
                drawLine(
                    color = strokeColor,
                    start = Offset(x, 0f),
                    end = Offset(x, size.height),
                    strokeWidth = 0.8.dp.toPx()
                )
                x += step
            }
            var y = step
            while (y < size.height) {
                drawLine(
                    color = strokeColor,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 0.8.dp.toPx()
                )
                y += step
            }
        }

        PatternTypes.CIRCUIT -> {
            val stroke = Stroke(width = 1.2.dp.toPx())
            val circuitColor = patternColor.copy(alpha = if (isDark) 0.28f else 0.18f)
            
            // Draw schematic paths
            val path1 = Path().apply {
                moveTo(20f, 40f)
                lineTo(80f, 40f)
                lineTo(120f, 80f)
                lineTo(size.width - 40f, 80f)
            }
            drawPath(path1, circuitColor, style = stroke)
            drawCircle(circuitColor, radius = 3.dp.toPx(), center = Offset(20f, 40f))
            drawCircle(circuitColor, radius = 3.dp.toPx(), center = Offset(size.width - 40f, 80f))

            if (size.height > 140f) {
                val path2 = Path().apply {
                    moveTo(size.width - 30f, 120f)
                    lineTo(size.width - 90f, 120f)
                    lineTo(size.width - 130f, 160f)
                    lineTo(40f, 160f)
                }
                drawPath(path2, circuitColor, style = stroke)
                drawCircle(circuitColor, radius = 3.dp.toPx(), center = Offset(40f, 160f))
            }
        }

        PatternTypes.TERRAZZO -> {
            val confColor1 = patternColor.copy(alpha = alpha * 1.5f)
            val confColor2 = patternColor.copy(alpha = alpha)
            
            // Fixed pleasant pattern spots
            val spots = listOf(
                Offset(0.15f, 0.2f), Offset(0.45f, 0.15f), Offset(0.85f, 0.25f),
                Offset(0.25f, 0.55f), Offset(0.70f, 0.60f), Offset(0.90f, 0.85f),
                Offset(0.10f, 0.88f), Offset(0.50f, 0.82f)
            )
            spots.forEachIndexed { index, normPos ->
                val center = Offset(normPos.x * size.width, normPos.y * size.height)
                if (index % 2 == 0) {
                    drawCircle(confColor1, radius = 4.dp.toPx(), center = center)
                } else {
                    drawRect(
                        confColor2,
                        topLeft = Offset(center.x - 4f, center.y - 4f),
                        size = androidx.compose.ui.geometry.Size(8.dp.toPx(), 8.dp.toPx())
                    )
                }
            }
        }

        PatternTypes.WAVES -> {
            val waveSpacing = 36.dp.toPx()
            var y = 20.dp.toPx()
            val stroke = Stroke(width = 1.dp.toPx())
            while (y < size.height) {
                val path = Path().apply {
                    moveTo(0f, y)
                    var x = 0f
                    val waveW = 60.dp.toPx()
                    while (x < size.width + waveW) {
                        quadraticTo(x + waveW / 4, y - 6.dp.toPx(), x + waveW / 2, y)
                        quadraticTo(x + 3 * waveW / 4, y + 6.dp.toPx(), x + waveW, y)
                        x += waveW
                    }
                }
                drawPath(path, strokeColor, style = stroke)
                y += waveSpacing
            }
        }

        PatternTypes.HATCH -> {
            val spacing = 20.dp.toPx()
            var p = -size.height
            while (p < size.width) {
                drawLine(
                    color = strokeColor,
                    start = Offset(p, 0f),
                    end = Offset(p + size.height, size.height),
                    strokeWidth = 0.8.dp.toPx()
                )
                p += spacing
            }
        }

        PatternTypes.PARCHMENT -> {
            // Delicate parchment fibers
            val fiberColor = patternColor.copy(alpha = alpha * 1.2f)
            val fibers = listOf(
                Offset(25f, 50f) to Offset(65f, 55f),
                Offset(size.width - 80f, 90f) to Offset(size.width - 30f, 85f),
                Offset(40f, size.height - 60f) to Offset(90f, size.height - 50f),
                Offset(size.width / 2, size.height / 2) to Offset(size.width / 2 + 40f, size.height / 2 + 8f)
            )
            fibers.forEach { (start, end) ->
                drawLine(
                    color = fiberColor,
                    start = start,
                    end = end,
                    strokeWidth = 1.dp.toPx()
                )
            }
        }

        PatternTypes.BLANK -> {
            // Smooth solid clean
        }
    }
}
