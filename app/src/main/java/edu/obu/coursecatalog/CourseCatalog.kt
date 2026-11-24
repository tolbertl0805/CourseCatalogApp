package edu.obu.coursecatalog

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import edu.obu.coursecatalog.data.Course
import edu.obu.coursecatalog.data.CourseDataSource
import edu.obu.coursecatalog.ui.CourseViewModel
import edu.obu.coursecatalog.ui.CourseViewModelFactory
import edu.obu.coursecatalog.ui.DepartmentViewModel
import edu.obu.coursecatalog.ui.SemesterViewModel
import kotlinx.serialization.Serializable

@Serializable
sealed class Routes{
    @Serializable
    data object Semesters

    @Serializable
    data class Departments(
        val semester: String
    )

    @Serializable
    data class Courses(
        val semester: String,
        val department: String
    )

    @Serializable
    data class Details(
        val semester: String,
        val department: String,
        val seqNumber: Int
    )
}

@Composable
fun CourseCatalog(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Semesters
    ) {
        composable<Routes.Semesters> {
            SemestersScreen(
                onClick = { semester ->
                    navController.navigate(Routes.Departments(semester))
                }
            )
        }
        composable<Routes.Departments> { backStackEntry ->
            val semester: Routes.Departments = backStackEntry.toRoute()

            DepartmentsScreen(
                onClick = { department ->
                    navController.navigate(Routes.Courses(semester.semester, department))
                },
                onUpClick = {
                    navController.navigate(Routes.Semesters)
                }
            )
        }
        composable<Routes.Courses> { backStackEntry ->
            val details: Routes.Courses = backStackEntry.toRoute()

            CoursesScreen(
                semester = details.semester,
                department = details.department,
                onClick = { seqNumber ->
                    navController.navigate(Routes.Details(details.semester, details.department, seqNumber))
                },
                onUpClick = {
                    navController.navigate(Routes.Departments(details.semester))
                }
            )
        }
        composable<Routes.Details> { backStackEntry ->
            val details: Routes.Details = backStackEntry.toRoute()
            val course = CourseDataSource().getCourse(details.semester, details.department, details.seqNumber, LocalContext.current)

            DetailsScreen(
                course,
                {
                    navController.navigate(Routes.Courses(details.semester, details.department))
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogBar(
    message: String,
    topScreen: Boolean,
    onUpClick: () -> Unit = {}
){
    Box(
        modifier = Modifier
            .background(Color(0xFF441880))
            .fillMaxWidth()
            .height(120.dp)
    ){
        Text(
            text=message,
            modifier = Modifier
                .padding(18.dp)
                .align(Alignment.BottomCenter),
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        if (!topScreen) {
            IconButton(
                onClick = onUpClick,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp)
            ) {
                Icon(
                    painterResource(R.drawable.previous_screen),
                    "return"
                )
            }
        }
    }
}

@Composable
fun SemestersScreen(
    onClick: (String) -> Unit,
    viewModel: SemesterViewModel = viewModel()
){
    Scaffold(
        topBar = {
            CatalogBar("Select a semester", true)
        }
    ){innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            items(viewModel.semesters){ semester ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clickable(
                            onClick = { onClick(semester) },
                            onClickLabel = stringResource(R.string.select_message, semester)
                        )
                        .background(Color(0xFFE8E8E8))
                        .drawBehind {
                            val strokeWidth = 2.dp.toPx()
                            drawLine(
                                color = Color.Gray,
                                start = Offset(0f, size.height),
                                end = Offset(size.width, size.height),
                                strokeWidth = strokeWidth
                            )
                        }
                        .padding(start=20.dp, end=20.dp)
                ){
                    Text(
                        text = semester,
                        modifier = Modifier.align(Alignment.CenterStart),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray
                    )
                    Icon(painter = painterResource(R.drawable.forward), contentDescription = "To Next Page", modifier = Modifier.align(Alignment.CenterEnd).size(30.dp))
                }
            }
        }
    }
}

@Composable
fun DepartmentsScreen(
    onClick: (String) -> Unit,
    onUpClick: () -> Unit,
    viewModel: DepartmentViewModel = viewModel()
){
    Scaffold(
        topBar = {
            CatalogBar("Select a department", false, onUpClick = onUpClick)
        }
    ){innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            items(viewModel.departments){ department ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clickable(
                            onClick = { onClick(department) },
                            onClickLabel = stringResource(R.string.select_message, department)
                        )
                        .background(Color(0xFFE8E8E8))
                        .drawBehind {
                            val strokeWidth = 2.dp.toPx()
                            drawLine(
                                color = Color.Gray,
                                start = Offset(0f, size.height),
                                end = Offset(size.width, size.height),
                                strokeWidth = strokeWidth
                            )
                        }
                        .padding(start=20.dp, end=20.dp)
                ){
                    Text(
                        text = department,
                        modifier = Modifier.align(Alignment.CenterStart),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray
                    )
                    Icon(painter = painterResource(R.drawable.forward), contentDescription = "To Next Page", modifier = Modifier.align(Alignment.CenterEnd).size(30.dp))
                }
            }
        }
    }
}

@Composable
fun CoursesScreen(
    semester: String,
    department: String,
    onClick: (Int) -> Unit,
    onUpClick: () -> Unit,
    viewModel: CourseViewModel = viewModel(
        factory = CourseViewModelFactory(
            LocalContext.current.applicationContext as Application,
            semester,
            department
        )
    )
){
    Scaffold(
        topBar = {
            CatalogBar("Select a course", false, onUpClick = onUpClick)
        }
    ){innerPadding ->
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding),
            columns = GridCells.Fixed(2)
        ) {
            items(viewModel.courses){ course ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clickable(
                            onClick = { onClick(course.sequenceNumber) },
                            onClickLabel = stringResource(R.string.select_message, department)
                        )
                        .background(Color(0xFFE8E8E8))
                        .drawBehind {
                            val strokeWidth = 1.dp.toPx()
                            val color = Color.Gray
                            drawLine(color, Offset(0f, size.height), Offset(size.width, size.height), strokeWidth)
                            drawLine(color, Offset(size.width, 0f), Offset(size.width, size.height), strokeWidth)
                        }
                        .padding(start=20.dp, end=20.dp)
                ){
                    Text(
                        text = stringResource(R.string.time_slot, course.title, course.days, course.time),
                        modifier = Modifier.align(Alignment.CenterStart),
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}

@Composable
fun DetailsScreen(
    course: Course,
    onUpClick: () -> Unit
){
    Scaffold(
        topBar = {
            CatalogBar("Course details", false, onUpClick = onUpClick)
        }
    ){ innerPadding ->
        Row(
            modifier = Modifier
                .drawBehind {
                    val strokeWidth = 2.dp.toPx()
                    drawLine(
                        color = Color.Gray,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = strokeWidth
                    )
                }
                .background(Color(0xFFE8E8E8))
                .padding(innerPadding)
        ){
            Text(
                stringResource(
                    R.string.course_info,
                    course.title,
                    course.department,
                    course.number,
                    course.sequenceNumber,
                    course.days,
                    course.time,
                    course.instructorFirst,
                    course.instructorLast,
                    course.room,
                    course.numEnrolled,
                    course.numSeats
                ),
                textAlign = TextAlign.Center,
                fontSize = 22.sp,
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            )
        }
    }
}
