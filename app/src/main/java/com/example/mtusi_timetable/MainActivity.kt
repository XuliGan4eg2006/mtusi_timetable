@file:OptIn(ExperimentalFoundationApi::class)

package com.example.mtusi_timetable

import android.Manifest
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings.Global.getString
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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mtusi_timetable.ui.theme.Mtusi_timetableTheme
import com.example.mtusi_timetable.ui.theme.grayCard
import com.example.mtusi_timetable.ui.theme.sourceCodePro
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.absoluteValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
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
fun Greeting(context: Context) {
    //        checkNotificationPermission(context)
    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
        if (!task.isSuccessful) {
            Log.w(TAG, "Fetching FCM registration token failed", task.exception)
            return@OnCompleteListener
        }

        // Get new FCM registration token
        val token = task.result
        Log.w(TAG, token.toString())
    })

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Gray)){

    val pagerState = rememberPagerState(pageCount = {3})

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
                    Text(text = "Page 1", modifier = Modifier.align(Alignment.CenterHorizontally), textAlign = TextAlign.Center)
                    Image(painter = painterResource(id = R.drawable.ic_launcher_background), contentDescription = "", modifier = Modifier.align(Alignment.CenterHorizontally))
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
                    Text(text = "Page 2", modifier = Modifier.align(Alignment.CenterHorizontally), textAlign = TextAlign.Center)
                    Image(painter = painterResource(id = R.drawable.ic_launcher_background), contentDescription = "", modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                2 -> Column(modifier = Modifier
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
                    Text(text = "Page 3", modifier = Modifier.align(Alignment.CenterHorizontally), textAlign = TextAlign.Center)
                    Image(painter = painterResource(id = R.drawable.ic_launcher_background), contentDescription = "", modifier = Modifier.align(Alignment.CenterHorizontally))
                }

            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectGroup(context: Context, navController: NavController) {
    Box(modifier = Modifier
        .background(Color.Gray)
        .fillMaxSize()) {

        val groupList = remember { mutableListOf<String>()}
        var isReady by remember { mutableStateOf(false)}
        //getting data via makeRequest
        LaunchedEffect(true) {
            val result = JSONObject(withContext(Dispatchers.IO){makeRequest(context, "https://mpa2a30b2dbfc4722f6a.free.beeceptor.com/groups")})
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

            Text(text = "Select Group")

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
                    Text(text = "Submit")
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeTable(context: Context, navController: NavController) {
    val classes = remember { mutableStateListOf<String>() }
    var requestReturn by remember { mutableStateOf(JSONObject()) }

    val weekDays  = listOf("Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота")
    var selectedDay by remember { mutableStateOf(0)}

    LaunchedEffect(true){
        val result = JSONObject(withContext(Dispatchers.IO){makeRequest(context, "http://192.168.1.191:8000/timetable/lol")})
        requestReturn = result
        for (i in 0 until result.getJSONObject("ИСП9-123А").getJSONArray("0").length()) {
            classes.add(result.getJSONObject("ИСП9-123А").getJSONArray("0").getString(i))
        }
        println(result)
    }

//        if (classesList.isEmpty()) {
//            Text(text = "Loading...")
//        }
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)) {
//        Card(colors = CardDefaults.cardColors(containerColor = Color.DarkGray), modifier = Modifier.padding(top = 5.dp, start = 5.dp)) {
//            Text(text = "TimeTable")
//        }
        CenterAlignedTopAppBar(title = { Text(text = "TimeTable", color = Color.White) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black), actions = {IconButton(onClick = {

            }) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "settings menu"
                )
            }})
        Spacer(modifier = Modifier.height(5.dp))
        
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)){
            for (i in weekDays) {
                item {
                    if (i == weekDays[selectedDay]) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.Cyan),
                        ) {
                            Text(text = i, color = Color.White, fontSize = 40.sp, modifier = Modifier.clickable { selectedDay = weekDays.indexOf(i) })

                        }
                    } else {
                        Text(text = i, color = Color.White, fontSize = 40.sp, modifier = Modifier.clickable {
                            selectedDay = weekDays.indexOf(i)
                            println("selectedDay: $selectedDay")

                            classes.clear()

                            for (z in 0 until requestReturn.getJSONObject("ИСП9-123А").getJSONArray(selectedDay.toString()).length()) {
                                classes.add(requestReturn.getJSONObject("ИСП9-123А").getJSONArray(selectedDay.toString()).getString(z))
                            }
                        })
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(5.dp))
        LazyColumn(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {

            items(classes) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = grayCard),
                    modifier = Modifier.size(width = 385.dp, height = 150.dp)
                        .animateItemPlacement(),
                    border = BorderStroke(1.dp, Color.Transparent)
                ) {
                        Row {
                            Box(
                                modifier = Modifier
                                    .background(Color.Cyan)
                                    .size(width = 50.dp, height = 150.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.book),
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


@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Log.i("checkNetwork", checkNetwork(context = LocalContext.current).toString())

    NavHost(navController = navController, startDestination = "TimeTable") {
        composable("HelloScreen") { Greeting(context = LocalContext.current) }
        composable("SelectGroup") { SelectGroup(context = LocalContext.current, navController = navController) }
        composable("TimeTable") { TimeTable(context = LocalContext.current, navController = navController) }
    }
}
