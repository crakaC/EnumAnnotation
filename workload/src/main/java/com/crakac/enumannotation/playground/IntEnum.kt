package com.crakac.enumannotation.playground

import com.crakac.enumannotation.EnumAccessor
import com.crakac.enumannotation.ParsableEnum


@ParsableEnum(fallback = "Unknown")
enum class IntEnum(val v: Int) {
    One(1),
    Two(2),
    Unknown(Int.MIN_VALUE)
}

data class B(
    @EnumAccessor("kind", IntEnum::class)
    val intValue: Int,
    @EnumAccessor("kinds", IntEnum::class)
    var intValues: List<Int>
)