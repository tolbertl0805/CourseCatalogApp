package edu.obu.coursecatalog.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import edu.obu.coursecatalog.data.CourseDataSource

class DepartmentViewModel(application: Application): AndroidViewModel(application) {
    val departments = CourseDataSource().getDepartments(application)
}