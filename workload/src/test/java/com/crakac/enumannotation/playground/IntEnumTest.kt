package com.crakac.enumannotation.playground

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class IntEnumTest {
    private lateinit var b: B

    @Before
    fun setUp() {
        b = B(
            intValue = 1,
            intValues = emptyList()
        )
    }

    @Test
    fun testIntValue() {
        assertEquals(IntEnum.One, b.kind)
    }

    @Test
    fun testIntValues() {
        assertEquals(emptyList<Int>(), b.kinds)
        b.kinds = listOf(IntEnum.One, IntEnum.Two)
        assertEquals(listOf(1, 2), b.intValues)
    }
}