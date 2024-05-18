package com.crakac.enumannotation.accessor

import com.crakac.enumannotation.EnumAccessor
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSName
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo

class EnumAccessorGenerator(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val classDeclaration: KSClassDeclaration,
    private val propertyDeclarations: List<KSPropertyDeclaration>,
    private val enumParameterNames: Map<ClassName, KSName>
) {
    private val annotationName = EnumAccessor::class.simpleName
    fun generate() {
        val packageName = classDeclaration.containingFile!!.packageName.asString()
        val className = "${classDeclaration.simpleName.asString()}_Accessor"
        val fileSpecBuilder = FileSpec.builder(packageName, className)
        propertyDeclarations.forEach { propertyDeclaration ->
            val annotation =
                propertyDeclaration.annotations.find { it.shortName.asString() == annotationName }!!
            val enumClass =
                annotation.arguments.first { it.name?.asString() == "enumClass" }.value as KSType
            val enumClassName = enumClass.toClassName()
            val backingPropertyName = propertyDeclaration.simpleName.asString()
            val accessorName =
                annotation.arguments.first { it.name?.asString() == "accessorName" }.value as String
            val enumParameterName = enumParameterNames[enumClassName]!!.asString()

            if (propertyDeclaration.type.resolve().declaration.simpleName.asString() == "List") {
                val list = ClassName("kotlin.collections", "List")
                val enumList = list.parameterizedBy(enumClassName)
                fileSpecBuilder.addProperty(
                    PropertySpec.builder(
                        accessorName,
                        enumList
                    ).receiver(classDeclaration.toClassName())
                        .mutable(propertyDeclaration.isMutable)
                        .getter(
                            generateListGetter(backingPropertyName, enumClassName)
                        ).setter(
                            if (propertyDeclaration.isMutable) {
                                generateListSetter(
                                    enumClassName,
                                    enumParameterName,
                                    enumList,
                                    backingPropertyName
                                )
                            } else {
                                null
                            }
                        ).build()
                )
            } else {
                fileSpecBuilder.addProperty(
                    PropertySpec.builder(
                        accessorName,
                        enumClassName
                    ).receiver(classDeclaration.toClassName())
                        .mutable(propertyDeclaration.isMutable)
                        .getter(
                            generateGetter(enumClassName, backingPropertyName)
                        )
                        .setter(
                            if (propertyDeclaration.isMutable) {
                                generateSetter(
                                    enumClassName,
                                    enumParameterName,
                                    backingPropertyName
                                )
                            } else {
                                null
                            }
                        )
                        .build()
                )
            }
        }
        fileSpecBuilder.build()
            .writeTo(codeGenerator, Dependencies(true, classDeclaration.containingFile!!))
    }

    private fun generateSetter(
        enumClassName: ClassName,
        enumParameterName: String,
        backingPropertyName: String
    ): FunSpec {
        return FunSpec.setterBuilder()
            .addParameter("newValue", enumClassName)
            .addStatement(
                "%L = newValue.%L",
                backingPropertyName,
                enumParameterName
            )
            .build()
    }

    private fun generateGetter(
        enumClassName: ClassName,
        backingPropertyName: String
    ): FunSpec {
        return FunSpec.getterBuilder()
            .addStatement(
                "return %TParser.parse(%L)",
                enumClassName,
                backingPropertyName,
            )
            .build()
    }

    private fun generateListSetter(
        enumClassName: ClassName,
        enumParameterName: String,
        enumList: ParameterizedTypeName,
        backingPropertyName: String
    ): FunSpec {
        return FunSpec.setterBuilder()
            .addParameter("newValue", enumList)
            .addStatement(
                "%L = newValue.map(%L::%L)",
                backingPropertyName,
                enumClassName,
                enumParameterName
            ).build()
    }

    private fun generateListGetter(
        backingPropertyName: String,
        enumClassName: ClassName
    ): FunSpec {
        return FunSpec.getterBuilder()
            .addStatement(
                "return %L.map(%TParser::parse)",
                backingPropertyName,
                enumClassName
            )
            .build()
    }
}