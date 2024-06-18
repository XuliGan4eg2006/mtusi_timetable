package com.example.mtusi_timetable.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mtusi_timetable.R
import com.example.mtusi_timetable.ui.theme.backColor
import com.example.mtusi_timetable.ui.theme.leftStripColor
import com.example.mtusi_timetable.ui.theme.logoColor1
import com.example.mtusi_timetable.ui.theme.sourceCodePro
import com.example.mtusi_timetable.ui.theme.textColor

@Composable
fun ServerDown(navController: NavController){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            Image(
                painter = painterResource(id = R.drawable.internet),
                contentDescription = "no internet",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(150.dp),
                colorFilter = ColorFilter.tint(leftStripColor)
            )
            Text(
                text = "Не удалось подключиться к серверу. Пожалуйста, проверьте подключение к сети и повторите попытку.",
                fontFamily = sourceCodePro,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 10.dp),
                textAlign = TextAlign.Center
            )
            Button(
                onClick = { navController.navigate("MainScreen") },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(containerColor = logoColor1)
            ) {
                Text(text = "Повторить попытку", color = textColor)

            }
        }
    }
}