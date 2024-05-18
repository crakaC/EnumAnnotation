package com.crakac.playground

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class EnumAnnotationsTest {
    private lateinit var a: A

    @Before
    fun setup() {
        a = A(
            mutable = "invalid",
            readOnly = "a",
            mutableList = listOf("a", "b", "c"),
            readOnlyList = listOf("")
        )
    }

    @Test
    fun testMutableProperty() {
        assertEquals(Type.Unknown, a.mutableType)
        a.mutableType = Type.A
        assertEquals("a", a.mutable)
    }

    @Test
    fun testReadOnlyProperty() {
        assertEquals(Type.A, a.readOnlyType)
    }

    @Test
    fun testMutableListProperty() {
        assertEquals(listOf(Type.A, Type.B, Type.C), a.mutableTypes)
        a.mutableTypes = listOf()
        assertEquals(listOf<String>(), a.mutableList)
    }

    @Test
    fun testReadOnlyListProperty() {
        assertEquals(listOf(Type.Unknown), a.readOnlyTypes)
    }
}
