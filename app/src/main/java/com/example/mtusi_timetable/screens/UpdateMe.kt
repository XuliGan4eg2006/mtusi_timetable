package com.example.mtusi_timetable.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import com.example.mtusi_timetable.ui.theme.backColor
import com.example.mtusi_timetable.ui.theme.logoColor1
import com.example.mtusi_timetable.ui.theme.textColor

@Composable
fun UpdateMe(appVersion: Int){
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .background(backColor)
            .fillMaxSize()
    ) {
        Column(modifier = Modifier.align(Alignment.Center)) {

            Text(
                text = "Ваша версия приложения устарела. Пожалуйста, обновите её. Ваша версия: $appVersion",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = textColor,
                textAlign = TextAlign.Center
            )
            Button(
                onClick = {
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://www.rustore.ru/catalog/app/com.rm_rf.mtusi_timetable")
                        )
                    )
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(containerColor = logoColor1)
            ) {
                Text(text = "Перейти в RuStore")
            }
        }
    }
}