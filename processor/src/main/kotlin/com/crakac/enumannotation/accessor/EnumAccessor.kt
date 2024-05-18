package com.crakac.enumannotation.accessor

import kotlin.reflect.KClass

annotation class EnumAccessor(val accessorName: String, val enumClass: KClass<*>)
