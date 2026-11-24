package edu.obu.coursecatalog.data

data class Course(
    val sequenceNumber: Int = 0,
    val department: String = "",
    val number: String = "", // Could be 1000.5
    val title: String = "",
    val numSeats: Int = 0,
    val numEnrolled: Int = 0,
//    val numWaiting: Int = 0,
//    val timeSlot: String = "", // We do not need this.
//    val semester: String = "", // We do not need this.
    val days: String = "",
    val time: String = "",
    val room: String = "",
    val instructorLast: String = "",
    val instructorFirst: String = "",
)