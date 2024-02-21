package com.example.mtusi_timetable.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mtusi_timetable.R
import com.example.mtusi_timetable.ui.theme.backColor
import com.example.mtusi_timetable.ui.theme.leftStripColor
import com.example.mtusi_timetable.ui.theme.logoColor1
import com.example.mtusi_timetable.ui.theme.logoColor2
import com.example.mtusi_timetable.ui.theme.sourceCodePro
import com.example.mtusi_timetable.ui.theme.telegramColor

@Composable
fun InfoScreen(navController: NavController) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backColor)
    ) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Image(
                painter = painterResource(id = R.drawable.rm_rf),
                contentDescription = "rm_rf logo",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            val gradientColors = listOf(logoColor1, logoColor2, logoColor2)

            Text(
                text = buildAnnotatedString {
                    append("Backend, Mobile app, Design \n by Иванов Дмитрий aka ")
                    withStyle(
                        SpanStyle(
                            brush = Brush.linearGradient(
                                colors = gradientColors
                            )
                        )
                    ) {
                        append("rm_rf")
                    }
                },
                fontFamily = sourceCodePro,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center
            )
            Button(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = telegramColor),
                onClick = {
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://t.me/circle_of_winds")
                        )
                    )
                }) {
                Text(
                    text = "Написать мне",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center
                )
            }

            Button(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = leftStripColor),
                onClick = { navController.navigate("Timetable") }) {
                Text(
                    text = "Вернуться к расписанию",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center
                )
            }
            Button(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = leftStripColor),
                onClick = { navController.navigate("SelectGroup") }) {
                Text(
                    text = "Выбрать группу",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}