@file:OptIn(ExperimentalFoundationApi::class)

package com.example.mtusi_timetable

import android.Manifest
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mtusi_timetable.ui.theme.Mtusi_timetableTheme
import com.example.mtusi_timetable.ui.theme.backColor
import com.example.mtusi_timetable.ui.theme.cardGreen
import com.example.mtusi_timetable.ui.theme.grayCard
import com.example.mtusi_timetable.ui.theme.leftStripColor
import com.example.mtusi_timetable.ui.theme.logoColor1
import com.example.mtusi_timetable.ui.theme.logoColor2
import com.example.mtusi_timetable.ui.theme.sourceCodePro
import com.example.mtusi_timetable.ui.theme.telegramColor
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlin.math.absoluteValue

const val serverUrl = "http://192.168.1.191:8000"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Mtusi_timetableTheme {
                MainScreen()
            }
        }
    }
}

fun checkNotificationPermission(context: Context){
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 12)
        }
    }
    else{
        Log.i("NotificationStatus","Have Permission")
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Greeting(navController: NavController) {
    val context = LocalContext.current

    Box(modifier = Modifier
        .fillMaxSize()
        .background(backColor)){

    val pagerState = rememberPagerState(pageCount = {2})

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
                        color = Color.White,
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
                        colors = ButtonDefaults.buttonColors(containerColor = leftStripColor),
                        onClick = {     coroutineScope.launch {
                            pagerState.animateScrollToPage(1)
                        } }) {

                        Text(text = "Поехали!",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.CenterVertically),
                            textAlign = TextAlign.Center)
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
                            Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                            return@OnCompleteListener
                        }

                        // Get new FCM registration token
                        val token = task.result
                        Log.w(TAG, token.toString())
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
                        Text(text = "Начать пользоваться",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.CenterVertically),
                            textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectGroup(navController: NavController) {
    val context = LocalContext.current

    Box(modifier = Modifier
        .background(backColor)
        .fillMaxSize()) {

        val groupList = remember { mutableListOf<String>()}
        var isReady by remember { mutableStateOf(false)}
        //getting data via makeRequest
        LaunchedEffect(true) {
            val jsonHandler = Json{this.ignoreUnknownKeys = true}

            val result = jsonHandler.decodeFromString<Map<String, List<String>>>(withContext(Dispatchers.IO){makeRequest("${serverUrl}/groups")})

            for (i in 0 until result["groups"]!!.size) {
                groupList.add(result["groups"]!![i])
            }
            isReady = true
        }

        Column(
            modifier = Modifier.align(Alignment.Center)
        ) {
            var expanded by remember { mutableStateOf(false) }
            var selectedIndex by remember { mutableIntStateOf(0) }

            Text(text = "Выберите группу", color = Color.White)

            AnimatedVisibility(visible = isReady) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    modifier = Modifier.padding(top = 10.dp),
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {
                    TextField(
                        value = groupList[selectedIndex],
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor()
                    )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }) {
                            groupList.forEachIndexed { index, s ->
                                DropdownMenuItem(
                                    onClick = { selectedIndex = index; expanded = false },
                                    text = { Text(text = s) })
                            }
                        }
                    }

                Button(modifier = Modifier.padding(top = 70.dp),onClick = {
                    val sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("group", groupList[selectedIndex])
                    editor.apply()
                    Log.i("groupUpdated", groupList[selectedIndex])
                    Toast.makeText(context, "Group Updated", Toast.LENGTH_SHORT).show()
                    navController.navigate("TimeTable")
                }) {
                    Text(text = "Продолжить")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeTable(navController: NavController) {
    val context = LocalContext.current

    var resultTimetable by remember { mutableStateOf(String()) }

    val weekDays = listOf("Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота")
    var selectedDay by remember { mutableIntStateOf(0) }

    //getting group from sharedPreferences
    val sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE)
    val group = sharedPreferences.getString("group", "ИСП9-123А").toString()
    var decodedMap by remember { mutableStateOf<Map<String, List<String>>?>(null) }

    val pagerState = rememberPagerState(pageCount = {
        weekDays.size
    })
    LaunchedEffect(true) {
        resultTimetable = withContext(Dispatchers.IO) {
            makeRequest(
                "${serverUrl}/timetableV1/${group}/"
            )
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
        Spacer(modifier = Modifier.height(5.dp))
        AnimatedContent(
            targetState = selectedDay, transitionSpec = {
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

        Spacer(modifier = Modifier.height(8.dp))

        if (decodedMap != null) {

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
                        val classRoom = matchResult?.value?.replace(" ", "")?.replace("\n", "") ?: ""

                        when {
                            "Конец" in it -> {}
                            "Перемена" in it -> {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = cardGreen),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(33.dp)

                                        .animateItemPlacement(),
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
                                        .animateItemPlacement(),
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
                                            Text(
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
                                            Text(
                                                text = it.split("~")[0],
                                                fontFamily = sourceCodePro,
                                                color = Color.White,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.align(Alignment.TopStart).padding(start = 10.dp, top = 5.dp)
                                            )
                                            Text(
                                                text = it.split("#")[1].replace(" ", ""),
                                                fontFamily = sourceCodePro,
                                                color = Color.White,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.align(Alignment.BottomStart).padding(start = 10.dp, bottom = 5.dp)
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

@Composable
fun InfoScreen(navController: NavController) {
    val context = LocalContext.current

    Box(modifier = Modifier
        .fillMaxSize()
        .background(backColor)) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Image(
                painter = painterResource(id = R.drawable.rm_rf),
                contentDescription = "rm_rf logo",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            val gradientColors = listOf(logoColor1, logoColor2, logoColor2)

            Text(
                text = buildAnnotatedString {append("Backend, Mobile app, Design \n by Иванов Дмитрий aka ")
                    withStyle(
                        SpanStyle(
                            brush = Brush.linearGradient(
                                colors = gradientColors
                            )
                        )
                    ) {
                        append("rm_rf")
                    }},
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
                onClick = { context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://t.me/circle_of_winds")
                    )
                ) }) {
                Text(text = "Написать мне",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center)
            }

            Button(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = leftStripColor),
                onClick = { navController.navigate("Timetable") }) {
                Text(text = "Вернуться к расписанию",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center)
            }
            Button(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = leftStripColor),
                onClick = { navController.navigate("SelectGroup") }) {
                Text(text = "Выбрать группу",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center)
            }
        }
    }
}
@Composable
fun CheckConditions(navController: NavController) {
    val context = LocalContext.current
    Box(modifier = Modifier
        .fillMaxSize()
        .background(backColor)) {
     if (!checkNetwork(context = context)) {
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
                 text = "Для работы приложения необходимо подключение к сети \nПожалуйста, подключитесь к сети и повторите попытку.",
                 fontFamily = sourceCodePro,
                 color = Color.White,
                 fontSize = 15.sp,
                 fontWeight = FontWeight.Bold,
                 modifier = Modifier
                     .align(Alignment.CenterHorizontally)
                     .padding(top = 10.dp),
                 textAlign = TextAlign.Center
             )
         }
     }
     else {
         val sharedPreferences =
             LocalContext.current.getSharedPreferences("userInfo", Context.MODE_PRIVATE)
         val group = sharedPreferences.getString("group", "no").toString()
    
         if (group == "no") {
             navController.navigate("HelloScreen")
         } else {
             navController.navigate("TimeTable")
         }
     }
    }
}

@Composable
fun MainScreen() {
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(backColor)) {
        val navController = rememberNavController()
        Log.i("checkNetwork", checkNetwork(context = LocalContext.current).toString())


        NavHost(navController = navController, startDestination = "CheckConditions") {
            composable("HelloScreen") { Greeting(navController = navController) }
            composable("SelectGroup") { SelectGroup(navController = navController) }
            composable("TimeTable") { TimeTable(navController = navController) }
            composable("CheckConditions") { CheckConditions(navController = navController) }
            composable("InfoScreen") { InfoScreen(navController = navController) }
        }
    }
}
