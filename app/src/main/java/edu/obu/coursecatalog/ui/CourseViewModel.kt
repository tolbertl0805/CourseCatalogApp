package edu.obu.coursecatalog.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.obu.coursecatalog.data.CourseDataSource

class CourseViewModel(
    application: Application,
    private val semester: String,
    private val department: String
): AndroidViewModel(application) {
    val courses = CourseDataSource().getCourses(semester, department, getApplication())
}

class CourseViewModelFactory(
    private val application: Application,
    private val semester: String,
    private val department: String
): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return CourseViewModel(application, semester, department) as T
    }
}