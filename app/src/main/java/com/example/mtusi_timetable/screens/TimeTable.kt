@file:OptIn(ExperimentalFoundationApi::class)

package com.example.mtusi_timetable.screens

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mtusi_timetable.R
import com.example.mtusi_timetable.makeRequest
import com.example.mtusi_timetable.serverUrl
import com.example.mtusi_timetable.ui.theme.backColor
import com.example.mtusi_timetable.ui.theme.cardGreen
import com.example.mtusi_timetable.ui.theme.grayCard
import com.example.mtusi_timetable.ui.theme.leftStripColor
import com.example.mtusi_timetable.ui.theme.sourceCodePro
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.util.Calendar

fun GetIndexDayOfWeek(): Int {
    val calendar = Calendar.getInstance()
    return calendar.get(Calendar.DAY_OF_WEEK)
}

fun getDayOfWeek(dayOffset: Int): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, dayOffset)

    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH) + 1 // Месяцы начинаются с 0, поэтому добавляем 1

    return "$day.$month"
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeTable(navController: NavController) {
    val context = LocalContext.current

    val weekDays = listOf("Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота")

    var resultTimetable by remember { mutableStateOf(String()) }
    var decodedMap by remember { mutableStateOf<Map<String, List<String>>?>(null) }
    var selectedDay by remember { mutableIntStateOf(0) }
    var isFailed by remember { mutableStateOf(false) }
    //getting group from sharedPreferences
    val sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE)
    val group = sharedPreferences.getString("group", "ИСП9-123А").toString()

    val pagerState = rememberPagerState(pageCount = {
        weekDays.size
    })
    LaunchedEffect(true) {
        resultTimetable = withContext(Dispatchers.IO) {
            try {
            makeRequest(
                "$serverUrl/timetableV1/${group}/"
            )}
            catch (e: Exception) {
                isFailed = true
                String()
            }
        }
        val jsonHandler = Json { this.ignoreUnknownKeys = true }
        decodedMap = jsonHandler.decodeFromString<Map<String, List<String>>>(
            resultTimetable
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backColor)
    ) {

        TopAppBar(title = { Text(text = "КТ МТУСИ РАСПИСАНИЕ", color = Color.White) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = backColor), actions = {
                IconButton(onClick = {
                    navController.navigate("InfoScreen")
                }) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "settings menu"
                    )
                }
            })
        Spacer(modifier = Modifier
            .height(5.dp)
            .fillMaxWidth())
        Box(modifier = Modifier.fillMaxWidth()) {
            AnimatedContent(
                modifier = Modifier.align(Alignment.CenterStart),
                targetState = selectedDay,
                transitionSpec = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.End,
                        animationSpec = tween(durationMillis = 1000)
                    ) togetherWith fadeOut()
                },
                label = "", contentAlignment = Alignment.Center
            ) { targetIndex ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = cardGreen),
                    modifier = Modifier.padding(start = 10.dp)
                ) {
                    Text(
                        text = weekDays[targetIndex],
                        color = Color.White,
                        fontSize = 40.sp,
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                    )
                }
            }

            Card(modifier = Modifier
                .padding(end = 10.dp)
                .align(Alignment.CenterEnd)) {
                Text(
                    text = getDayOfWeek(selectedDay),
                    color = Color.White,
                    fontSize = 40.sp,
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        AnimatedVisibility(visible = isFailed) {
            Text(text = "Ошибка при получении расписания",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontFamily = sourceCodePro,
                color = Color.White,
                fontSize = 15.sp)
        }
        AnimatedVisibility(visible = decodedMap != null) {
            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->

                LazyColumn(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(decodedMap?.get(page.toString())!!) {

                        selectedDay = pagerState.currentPage

                        //getting text between ~ and #
                        val regex = Regex("(?<=~)(.*?)(?=#)")
                        val matchResult = regex.find(it)
                        val classRoom =
                            matchResult?.value?.replace(" ", "")?.replace("\n", "") ?: ""

                        when {
                            "Конец" in it -> {}
                            "Перемена" in it -> {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = cardGreen),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(33.dp),
                                    border = BorderStroke(1.dp, Color.Transparent)
                                ) {
                                    Text(
                                        text = it,
                                        fontFamily = sourceCodePro,
                                        color = Color.Black,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .padding(top = 5.dp)
                                    )
                                }
                            }

                            else -> {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = grayCard),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(150.dp)
                                        .padding(start = 5.dp, end = 5.dp),
                                    border = BorderStroke(1.dp, Color.Transparent)
                                ) {
                                    Row {
                                        Box(
                                            modifier = Modifier
                                                .background(leftStripColor)
                                                .size(width = 50.dp, height = 150.dp)
                                        ) {
                                            Icon(
                                                painter = painterResource(
                                                    id = (if ("Физическая культура" in it) {
                                                        R.drawable.basketball
                                                    } else if ("Нет урока" in it) {
                                                        R.drawable.disabled
                                                    } else {
                                                        R.drawable.book
                                                    })
                                                ),
                                                contentDescription = "book",
                                                modifier = Modifier
                                                    .size(65.dp)
                                                    .align(Alignment.TopCenter)
                                                    .padding(top = 20.dp),
                                                tint = Color.Gray
                                            )
                                            Text( // class room
                                                text = classRoom.replace("\n", "")
                                                    .replace("/", "\n")
                                                    .replace("None", ""),
                                                fontFamily = sourceCodePro,
                                                color = Color.Black,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                textAlign = TextAlign.Justify,
                                                modifier = Modifier
                                                    .align(Alignment.BottomCenter)
                                                    .padding(bottom = 15.dp)
                                            )

                                        }
                                        Box(modifier = Modifier.fillMaxSize()) {
                                            Text( //class name
                                                text = it.split("~")[0],
                                                maxLines = 4,
                                                fontFamily = sourceCodePro,
                                                color = Color.White,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier
                                                    .align(Alignment.TopStart)
                                                    .padding(start = 10.dp, top = 5.dp)
                                            )
                                            Text(//class time
                                                text = it.split("#")[1].replace(" ", ""),
                                                fontFamily = sourceCodePro,
                                                color = Color.White,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier
                                                    .align(Alignment.BottomStart)
                                                    .padding(start = 10.dp, bottom = 5.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}