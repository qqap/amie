package com.example.amie.calendar

import androidx.compose.ui.graphics.Color
import java.time.LocalDateTime

data class Event(
    val name: String,
    val color: Color,
    var start: LocalDateTime,
    var end: LocalDateTime,
    var checkboxItem: Boolean = false,
    var checked: Boolean = false,
    var onCalendar: Boolean = false,
    var todoTask: Boolean = false,
)

val sampleEvents = listOf(
    Event(
        name = "Google I/O Keynote",
        color = Color(0xFF0e5782),
        start = LocalDateTime.parse("2024-01-16T02:00:00"),
        end = LocalDateTime.parse("2024-01-16T04:00:00"),
    ),
    Event(
        name = "Developer Keynote",
        color = Color(0xFFAFBBF2),
        start = LocalDateTime.parse("2024-01-14T09:00:00"),
        end = LocalDateTime.parse("2024-01-14T09:45:00"),
    ),
    Event(
        name = "What's new in Dogo",
        color = Color(0xFF1B998B),
        start = LocalDateTime.parse("2024-01-15T10:00:00"),
        end = LocalDateTime.parse("2024-01-15T11:00:00"),
    ),
    Event(
        name = "What's new in ",
        color = Color(0xFF6DD3CE),
        start = LocalDateTime.parse("2024-01-14T11:00:00"),
        end = LocalDateTime.parse("2024-01-14T11:45:00"),
    ),
    Event(
        name = "What's new in Dog",
        color = Color(0xFF990F4A),
        start = LocalDateTime.parse("2024-01-14T10:00:00"),
        end = LocalDateTime.parse("2024-01-14T11:00:00"),
    ),
    Event(
        name = "What's new in Cat",
        color = Color(0xFF990F4A),
        start = LocalDateTime.parse("2024-01-12T16:30:00"),
        end = LocalDateTime.parse("2024-01-12T18:15:00"),
    ),
    Event(
        name = "Buy groceries",
        color = Color(0xFF5F5A5C),
        start = LocalDateTime.parse("2024-01-12T16:30:00"),
        end = LocalDateTime.parse("2024-01-12T17:15:00"),
        checkboxItem = true,
    ),
    Event(
        name = "Try coffee from Dak ☕️",
        color = Color(0xFF835467),
        start = LocalDateTime.parse("2024-01-12T16:30:00"),
        end = LocalDateTime.parse("2024-01-12T17:15:00"),
        checkboxItem = true
    ),
    Event(
        name = "🌱 Lunch Break",
        color = Color(0xFFA39E72),
        start = LocalDateTime.parse("2024-01-12T16:30:00"),
        end = LocalDateTime.parse("2024-01-12T17:15:00"),
        checkboxItem = true,
    ),
    Event(
        name = "todo",
        color = Color(0xFFA39E72),
        start = LocalDateTime.parse("2024-01-12T16:30:00"),
        end = LocalDateTime.parse("2024-01-12T17:15:00"),
        todoTask = true,
    ),
)