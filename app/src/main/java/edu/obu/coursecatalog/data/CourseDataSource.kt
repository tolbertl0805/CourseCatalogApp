package edu.obu.coursecatalog.data

import android.content.Context
import edu.obu.coursecatalog.data.Course

class CourseDataSource {
    fun getSemesters(context: Context): List<String> {
        val semesters = mutableListOf<String>()
        val inputStream = context.assets.open("semesters.txt")
        val reader = inputStream.bufferedReader()

        reader.forEachLine {
            semesters.add(it)
        }

        return semesters
    }

    fun getDepartments(context: Context): List<String> {
        val departments = mutableListOf<String>()
        val inputStream = context.assets.open("departments.txt")
        val reader = inputStream.bufferedReader()

        reader.forEachLine {
            departments.add(it)
        }

        return departments
    }

    fun getCourses(semester: String, department: String, context: Context): List<Course> {
        val courseList = mutableListOf<Course>()
        val inputStream = context.assets.open("$semester$department.txt")
        val reader = inputStream.bufferedReader()

        // Append each line and newline character to stringBuilder
        reader.forEachLine {
            courseList.add(courseFromString(it))
        }

        return courseList
    }

    fun getCourse(
        semester: String,
        department: String,
        sequenceNumber: Int,
        context: Context
    ): Course {
        val inputStream = context.assets.open("$semester$department.txt")
        val reader = inputStream.bufferedReader()
        var result = Course()
        val x = reader.forEachLine {
            val fields = it.split(',')
            if (fields.get(0).toInt() == sequenceNumber) {
                result = courseFromString(it)
                return@forEachLine // Break the forEachLine function.
            }
        }

        return result
    }

    // Private helper function.
    private fun courseFromString(line: String): Course {
        val fields = line.split(',')
        val sequenceNumber = fields[0].toInt()
        val department = fields[1]
        val number = fields[2]
        val title = fields[3]
        val numSeats = fields[4].toInt()
        val numEnrolled = fields[5].toInt()
//            val numWaiting = fields[6].toInt()
        val days = fields[8]
        val time = fields[9]
        val room = fields[10]
        val instructorLast = fields[11]
        val instructorFirst = fields[12]

        return Course(
            sequenceNumber,
            department,
            number,
            title,
            numSeats,
            numEnrolled,
            days,
            time,
            room,
            instructorLast,
            instructorFirst
        )
    }

    companion object
}