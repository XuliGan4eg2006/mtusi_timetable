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
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.modifier.modifierLocalConsumer
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

import org.json.JSONObject
import kotlin.math.absoluteValue

val serverUrl = "http://87.251.77.69:8000"

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
        println("Have Permission")
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Greeting(context: Context, navController: NavController) {

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
                                (pagerState.currentPage - page) + pagerState
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
                        modifier = Modifier.align(Alignment.CenterHorizontally).size(150.dp)
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
fun SelectGroup(context: Context, navController: NavController) {
    Box(modifier = Modifier
        .background(backColor)
        .fillMaxSize()) {

        val groupList = remember { mutableListOf<String>()}
        var isReady by remember { mutableStateOf(false)}
        //getting data via makeRequest
        LaunchedEffect(true) {
            val result = JSONObject(withContext(Dispatchers.IO){makeRequest(context, "${serverUrl}/groups")})
            val groups = result.getJSONArray("groups")

            Log.i("groups", groups.toString())

            for (i in 0 until groups.length()) {
                groupList.add(groups.getString(i))
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

fun MutableList<String>.fillClassesAndBreaks(resultTimetable: JSONObject, resultBreaks: JSONObject, dayNum: String, group: String) {
    for (i in 0 until resultTimetable.getJSONObject(group).getJSONArray(dayNum).length()) {
        this.add(resultTimetable.getJSONObject(group).getJSONArray(dayNum).getString(i))
        this.add(resultBreaks.getJSONArray("breaks").getString(i))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeTable(context: Context, navController: NavController) {
    val classes = remember { mutableStateListOf<String>() }
    var resultTimetable by remember { mutableStateOf(JSONObject()) }

    var resultBreaks by remember { mutableStateOf(JSONObject()) }

    val weekDays  = listOf("Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота")
    var selectedDay by remember { mutableStateOf(0) }

    //getting group from sharedPreferences
    val sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE)
    val group = sharedPreferences.getString("group", "ИСП9-123А").toString()

    LaunchedEffect(true){
        resultTimetable = JSONObject(withContext(Dispatchers.IO){makeRequest(context, "${serverUrl}/timetable/${group}/")})
        resultBreaks = JSONObject(withContext(Dispatchers.IO){makeRequest(context, "${serverUrl}/breaks")})


        classes.fillClassesAndBreaks(resultTimetable, resultBreaks, "0", group)
        println(resultTimetable)
    }

//        if (classesList.isEmpty()) {
//            Text(text = "Loading...")
//        }
    Column(modifier = Modifier
        .fillMaxSize()
        .background(backColor)) {

        TopAppBar(title = { Text(text = "КТ МТУСИ РАСПИСАНИЕ", color = Color.White) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = backColor), actions = {IconButton(onClick = {
                navController.navigate("InfoScreen")
            }) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "settings menu"
                )
            }})
        Spacer(modifier = Modifier.height(5.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier
            .padding(start = 8.dp)) {
            for (i in weekDays) {
                item {
                    if (i == weekDays[selectedDay]) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = cardGreen),
                        ) {
                            Text(text = i, color = Color.White, fontSize = 40.sp, modifier = Modifier.clickable { selectedDay = weekDays.indexOf(i) })

                        }
                    } else {
                        Text(text = i, color = Color.White, fontSize = 40.sp, modifier = Modifier.clickable {
                            selectedDay = weekDays.indexOf(i)
                            println("selectedDay: $selectedDay")

                            classes.clear()

                            classes.fillClassesAndBreaks(resultTimetable, resultBreaks, selectedDay.toString(), group)
                        })
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {

            items(classes) {
                when {
                    "Конец" in it -> { }
                    "Перемена" in it -> {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = cardGreen),
                            modifier = Modifier
                                .size(width = 385.dp, height = 33.dp)
                                .animateItemPlacement(),
                            border = BorderStroke(1.dp, Color.Transparent)) {
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
                                .size(width = 385.dp, height = 150.dp)
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
                                            .align(Alignment.Center)
                                            .size(40.dp),
                                        tint = Color.Gray
                                    )
                                }

                                Text(
                                    text = it,
                                    fontFamily = sourceCodePro,
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(top = 5.dp, start = 10.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun InfoScreen(context: Context, navController: NavController) {
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
fun CheckConditions(context: Context, navController: NavController) {
 Box(modifier = Modifier
     .fillMaxSize()
     .background(backColor)) {
     if (!checkNetwork(context = LocalContext.current)) {
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
    val navController = rememberNavController()
    Log.i("checkNetwork", checkNetwork(context = LocalContext.current).toString())


    NavHost(navController = navController, startDestination = "CheckConditions") {
        composable("HelloScreen") { Greeting(context = LocalContext.current, navController = navController) }
        composable("SelectGroup") { SelectGroup(context = LocalContext.current, navController = navController) }
        composable("TimeTable") { TimeTable(context = LocalContext.current, navController = navController) }
        composable("CheckConditions") { CheckConditions(context = LocalContext.current, navController = navController) }
        composable("InfoScreen") { InfoScreen(context = LocalContext.current ,navController = navController) }
    }
}
