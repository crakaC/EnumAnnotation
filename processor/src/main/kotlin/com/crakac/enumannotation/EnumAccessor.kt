package com.crakac.enumannotation

import kotlin.reflect.KClass

annotation class EnumAccessor(val accessorName: String, val enumClass: KClass<*>)
