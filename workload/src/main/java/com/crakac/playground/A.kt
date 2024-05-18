package com.crakac.playground

import com.crakac.enumannotation.EnumAccessor
import com.crakac.enumannotation.ParsableEnum

data class A(
    @EnumAccessor("mutableType", Type::class)
    var mutable: String,
    @EnumAccessor("readOnlyType", Type::class)
    val readOnly: String,
    @EnumAccessor("mutableTypes", Type::class)
    var mutableList: List<String>,
    @EnumAccessor("readOnlyTypes", Type::class)
    val readOnlyList: List<String>
)

@ParsableEnum(fallback = "Unknown")
enum class Type(val stringValue: String = "") {
    A("a"),
    B("b"),
    C("c"),
    Unknown;
}