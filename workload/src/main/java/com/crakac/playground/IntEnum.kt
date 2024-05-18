package com.crakac.playground

import com.crakac.enumannotation.EnumAccessor
import com.crakac.enumannotation.ParsableEnum


@ParsableEnum(fallback = "Unknown")
enum class Kind(val v: Int) {
    A(1),
    B(2),
    Unknown(Int.MIN_VALUE)
}

data class B(
    @EnumAccessor("kind", Kind::class)
    val intValue: Int,
    @EnumAccessor("kinds", Kind::class)
    var intValues: List<Int>
)

fun useB() {
    val b = B(1, listOf(1,2,3))
    b.kind
    b.kinds
}
