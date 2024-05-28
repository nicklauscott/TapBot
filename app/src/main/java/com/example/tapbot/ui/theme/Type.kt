package com.example.tapbot.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.tapbot.R

// Set of Material typography styles to start with
val Typography = Typography(
    // Other default text styles to override
    titleLarge = TextStyle(                  // Done  -- Page title
        fontFamily = customFontFamily[2],
        fontSize = 30.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    bodySmall = TextStyle(                  // Done  -- Navigation text
        fontFamily = customFontFamily[2],
        fontSize = 18.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = customFontFamily[5],
        fontWeight = FontWeight.Black,
        fontSize = 20.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = customFontFamily[3],
        fontSize = 15.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )

)

val customFontFamily: List<FontFamily>
    get() = listOf(
        FontFamily(Font(R.font.nationale_bold, FontWeight.Bold)),
        FontFamily(Font(R.font.nationale_black, FontWeight.Black)),
        FontFamily(Font(R.font.nationale_demi_bold, FontWeight.SemiBold)),
        FontFamily(Font(R.font.nationale_italic, FontWeight.Thin)),
        FontFamily(Font(R.font.nationale_light, FontWeight.Light)),
        FontFamily(Font(R.font.nationale_medium, FontWeight.Medium)),
        FontFamily(Font(R.font.nationale_regular, FontWeight.Normal))
    )
