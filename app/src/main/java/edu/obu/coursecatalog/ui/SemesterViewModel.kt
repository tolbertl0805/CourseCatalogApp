package edu.obu.coursecatalog.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import edu.obu.coursecatalog.data.CourseDataSource

class SemesterViewModel(application: Application): AndroidViewModel(application) {
    val semesters = CourseDataSource().getSemesters(getApplication())
}