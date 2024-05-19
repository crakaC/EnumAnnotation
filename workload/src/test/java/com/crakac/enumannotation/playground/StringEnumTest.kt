package com.crakac.enumannotation.playground

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class StringEnumTest {
    private lateinit var a: A

    @Before
    fun setup() {
        a = A(
            stringValue = "a",
            stringValues = listOf("a", "b", "c"),
        )
    }

    @Test
    fun testReadOnlyProperty() {
        assertEquals(StringEnum.A, a.type)
    }

    @Test
    fun testMutableListProperty() {
        assertEquals(listOf(StringEnum.A, StringEnum.B, StringEnum.C), a.types)
        a.types = listOf()
        assertEquals(listOf<String>(), a.stringValues)
    }
}
