package com.example.mtusi_timetable.screens

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavController
import com.example.mtusi_timetable.R
import com.example.mtusi_timetable.checkNotificationPermission
import com.example.mtusi_timetable.ui.theme.backColor
import com.example.mtusi_timetable.ui.theme.backColorTEst
import com.example.mtusi_timetable.ui.theme.leftStripColor
import com.example.mtusi_timetable.ui.theme.primaryTest
import com.example.mtusi_timetable.ui.theme.sourceCodePro
import com.example.mtusi_timetable.ui.theme.telegramColor
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Greeting(navController: NavController) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backColorTEst)
    ) {

        val pagerState = rememberPagerState(pageCount = { 2 })

        HorizontalPager(state = pagerState, modifier = Modifier.align(Alignment.Center)) { page ->
            when (page) {
                0 -> Column(modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        val pageOffset = (
                                (pagerState.currentPage) + pagerState
                                    .currentPageOffsetFraction
                                ).absoluteValue

                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }) {
                    Image(
                        painter = painterResource(id = R.drawable.hello),
                        contentDescription = "",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = "Добро пожаловать в открытый бета тест первого приложения для рассписания КТ МТУСИ\n\nПожалуйста, разрешите уведомления на следующем экране, чтобы получать уведомления о заменах и событиях колледжа",
                        fontFamily = sourceCodePro,
                        color = primaryTest,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 10.dp),
                        textAlign = TextAlign.Center
                    )

                    val coroutineScope = rememberCoroutineScope()
                    Button(modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryTest),
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(1)
                            }
                        }) {

                        Text(
                            text = "Поехали!",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.CenterVertically),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                1 -> Column(modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        val pageOffset = (
                                (pagerState.currentPage - page) + pagerState
                                    .currentPageOffsetFraction
                                ).absoluteValue

                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }) {

                    checkNotificationPermission(context)
                    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            Log.w(
                                ContentValues.TAG,
                                "Fetching FCM registration token failed",
                                task.exception
                            )
                            return@OnCompleteListener
                        }

                        // Get new FCM registration token
                        val token = task.result
                        Log.w(ContentValues.TAG, token.toString())
                    })

                    Image(
                        painter = painterResource(id = R.drawable.bug),
                        colorFilter = ColorFilter.tint(leftStripColor),
                        contentDescription = "",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .size(150.dp)
                    )

                    Text(
                        text = "Отлично, на следующем экране выберите вашу группу (позже вы сможете изменить её в настройках) \n\nЕсли вы нашли баг/недочёт или столкнулись с другой иной проблемой, пожалуйста, свяжитесь со мной через",
                        fontFamily = sourceCodePro,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Telegram",
                        fontFamily = sourceCodePro,
                        color = telegramColor,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable {
                                context.startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://t.me/circle_of_winds")
                                    )
                                )
                            },
                        textAlign = TextAlign.Center
                    )

                    Button(modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = leftStripColor),
                        onClick = { navController.navigate("SelectGroup") }) {
                        Text(
                            text = "Начать пользоваться",
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
    }
}