package com.crakac.enumannotation.playground

import com.crakac.enumannotation.EnumAccessor
import com.crakac.enumannotation.ParsableEnum

@ParsableEnum(fallback = "Unknown")
enum class StringEnum(val stringValue: String = "") {
    A("a"),
    B("b"),
    C("c"),
    Unknown;
}

data class A(
    @EnumAccessor("type", StringEnum::class)
    val stringValue: String,
    @EnumAccessor("types", StringEnum::class)
    var stringValues: List<String>,
)
