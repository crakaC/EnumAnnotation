package com.crakac.enumannotation.accessor

import com.crakac.enumannotation.EnumAccessor
import com.crakac.enumannotation.ParsableEnum
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.ksp.toClassName

class EnumAccessorProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(EnumAccessor::class.qualifiedName!!)
        val enumParameterNames =
            resolver.getSymbolsWithAnnotation(ParsableEnum::class.qualifiedName!!)
                .mapNotNull {
                    it as? KSClassDeclaration
                }.map {
                    it.toClassName() to it.primaryConstructor!!.parameters[0].name!!
                }.toMap()
        val ret = symbols.filter { !it.validate() }.toList()
        symbols
            .filterIsInstance<KSPropertyDeclaration>()
            .filter { it.parentDeclaration is KSClassDeclaration && it.validate() }
            .groupBy { it.parentDeclaration!! as KSClassDeclaration }
            .forEach { (parent, properties) ->
                EnumAccessorGenerator(
                    codeGenerator, logger, parent, properties, enumParameterNames
                ).generate()
            }
        return ret
    }
}

class EnumAccessorProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return EnumAccessorProcessor(environment.codeGenerator, environment.logger)
    }
}