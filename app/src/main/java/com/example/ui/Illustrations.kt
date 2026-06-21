package com.example.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.theme.Outline
import com.example.ui.theme.Primary
import com.example.ui.theme.Secondary
import com.example.ui.theme.OnPrimaryContainer

@Composable
fun WaterDropIcon(modifier: Modifier = Modifier, color: Color = Secondary) {
    Canvas(modifier = modifier.size(24.dp)) {
        val width = size.width
        val height = size.height
        val path = Path().apply {
            moveTo(width / 2f, height * 0.15f)
            cubicTo(
                width * 0.15f, height * 0.60f,
                width * 0.10f, height * 0.90f,
                width / 2f, height * 0.95f
            )
            cubicTo(
                width * 0.90f, height * 0.90f,
                width * 0.85f, height * 0.60f,
                width / 2f, height * 0.15f
            )
            close()
        }
        drawPath(path = path, color = color)
    }
}

@Composable
fun SnowflakeIcon(modifier: Modifier = Modifier, color: Color = Primary) {
    Canvas(modifier = modifier.size(24.dp)) {
        val width = size.width
        val height = size.height
        val center = Offset(width / 2, height / 2)
        val armLength = width * 0.35f

        // Draw 6 arms
        for (i in 0 until 6) {
            val angle = (i * 60) * (Math.PI / 180).toFloat()
            val endX = center.x + armLength * kotlin.math.cos(angle).toFloat()
            val endY = center.y + armLength * kotlin.math.sin(angle).toFloat()
            drawLine(
                color = color,
                start = center,
                end = Offset(endX, endY),
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )

            // Draw V branch for each arm
            val branchAngle1 = angle + (30 * Math.PI / 180).toFloat()
            val branchAngle2 = angle - (30 * Math.PI / 180).toFloat()
            val branchLen = armLength * 0.4f
            val midX = center.x + (armLength * 0.6f) * kotlin.math.cos(angle).toFloat()
            val midY = center.y + (armLength * 0.6f) * kotlin.math.sin(angle).toFloat()

            drawLine(
                color = color,
                start = Offset(midX, midY),
                end = Offset(
                    midX + branchLen * kotlin.math.cos(branchAngle1).toFloat(),
                    midY + branchLen * kotlin.math.sin(branchAngle1).toFloat()
                ),
                strokeWidth = 2.5.dp.toPx(),
                cap = StrokeCap.Round
            )
            drawLine(
                color = color,
                start = Offset(midX, midY),
                end = Offset(
                    midX + branchLen * kotlin.math.cos(branchAngle2).toFloat(),
                    midY + branchLen * kotlin.math.sin(branchAngle2).toFloat()
                ),
                strokeWidth = 2.5.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun BugIcon(modifier: Modifier = Modifier, color: Color = Outline) {
    Canvas(modifier = modifier.size(24.dp)) {
        val width = size.width
        val height = size.height
        val cx = width / 2f
        val cy = height / 2f

        // Body
        drawOval(
            color = color,
            topLeft = Offset(cx - width * 0.2f, cy - height * 0.3f),
            size = Size(width * 0.4f, height * 0.6f)
        )

        // Head
        drawCircle(
            color = color,
            radius = width * 0.15f,
            center = Offset(cx, cy - height * 0.35f)
        )

        // Legs (3 pairs)
        val legsY = listOf(cy - height * 0.15f, cy, cy + height * 0.15f)
        for (y in legsY) {
            drawLine(
                color = color,
                start = Offset(cx - width * 0.15f, y),
                end = Offset(cx - width * 0.45f, y - height * 0.05f),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
            drawLine(
                color = color,
                start = Offset(cx + width * 0.15f, y),
                end = Offset(cx + width * 0.45f, y - height * 0.05f),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
        }

        // Antennae
        drawLine(
            color = color,
            start = Offset(cx - width * 0.05f, cy - height * 0.45f),
            end = Offset(cx - width * 0.25f, cy - height * 0.58f),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(cx + width * 0.05f, cy - height * 0.45f),
            end = Offset(cx + width * 0.25f, cy - height * 0.58f),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun StylizedHouseIllustration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Ground pedestal (isometric ellipse)
        val groundY = h * 0.72f
        drawOval(
            color = Color(0xFFDCEAFE),
            topLeft = Offset(w * 0.1f, groundY - h * 0.1f),
            size = Size(w * 0.8f, h * 0.25f)
        )

        // Pedestal layers
        drawOval(
            color = Color(0xFFC0DBFF),
            topLeft = Offset(w * 0.1f, groundY - h * 0.08f),
            size = Size(w * 0.8f, h * 0.23f)
        )

        // House building block (3D styled prism)
        val roofPeak = Offset(w * 0.5f, h * 0.25f)
        val roofLeft = Offset(w * 0.28f, h * 0.45f)
        val roofRight = Offset(w * 0.72f, h * 0.45f)
        val frontBottomLeft = Offset(w * 0.32f, groundY - h * 0.05f)
        val frontBottomRight = Offset(w * 0.68f, groundY - h * 0.05f)

        // Left Face (Front-Left in isometric projection)
        val leftFacePath = Path().apply {
            moveTo(roofPeak.x, roofPeak.y)
            lineTo(roofLeft.x, roofLeft.y)
            lineTo(frontBottomLeft.x, frontBottomLeft.y)
            lineTo(w * 0.5f, groundY)
            close()
        }
        drawPath(leftFacePath, Color(0xFF93C5FD))

        // Right Face
        val rightFacePath = Path().apply {
            moveTo(roofPeak.x, roofPeak.y)
            lineTo(roofRight.x, roofRight.y)
            lineTo(frontBottomRight.x, frontBottomRight.y)
            lineTo(w * 0.5f, groundY)
            close()
        }
        drawPath(rightFacePath, Color(0xFF3B82F6))

        // Roof Left
        val roofLeftPath = Path().apply {
            moveTo(roofPeak.x, roofPeak.y)
            lineTo(roofLeft.x, roofLeft.y)
            lineTo(w * 0.25f, h * 0.42f)
            lineTo(w * 0.5f, h * 0.20f)
            close()
        }
        drawPath(roofLeftPath, Color(0xFFDBEAFE))

        // Roof Right
        val roofRightPath = Path().apply {
            moveTo(roofPeak.x, roofPeak.y)
            lineTo(roofRight.x, roofRight.y)
            lineTo(w * 0.75f, h * 0.42f)
            lineTo(w * 0.5f, h * 0.20f)
            close()
        }
        drawPath(roofRightPath, Color(0xFF1D4ED8))

        // Windows (2D/3D blocks)
        drawRect(
            color = Color.White,
            topLeft = Offset(w * 0.4f, h * 0.52f),
            size = Size(w * 0.07f, h * 0.08f)
        )
        drawRect(
            color = Color.White,
            topLeft = Offset(w * 0.53f, h * 0.52f),
            size = Size(w * 0.07f, h * 0.08f)
        )

        // Toy Car
        val carX = w * 0.55f
        val carY = groundY + h * 0.02f
        drawRoundRect(
            color = Primary,
            topLeft = Offset(carX, carY),
            size = Size(w * 0.25f, h * 0.08f),
            cornerRadius = CornerRadius(10f, 10f)
        )
        drawRoundRect(
            color = OnPrimaryContainer,
            topLeft = Offset(carX + w * 0.05f, carY - h * 0.04f),
            size = Size(w * 0.15f, h * 0.05f),
            cornerRadius = CornerRadius(8f, 8f)
        )
        drawCircle(
            color = Color.DarkGray,
            radius = 12f,
            center = Offset(carX + w * 0.06f, carY + h * 0.08f)
        )
        drawCircle(
            color = Color.DarkGray,
            radius = 12f,
            center = Offset(carX + w * 0.19f, carY + h * 0.08f)
        )
    }
}
