package com.olivergao.androidsorter.sortstrategy

import com.olivergao.androidsorter.action.SortedData
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.*
import org.w3c.dom.ranges.Range

abstract class BaseSortStrategy(private val mAllDeclarations: List<KtDeclaration>) {
    protected var mOrdering: ArrayList<String>? = null

    fun sort(): ArrayList<KtDeclaration> {
        //group declaration
        val allNewDeclaration = ArrayList<KtDeclaration>()
        val group = mAllDeclarations.groupBy { it.javaClass.name }

        //sort properties
        var variableList = group[KtProperty::class.java.name]
        variableList = variableList?.sortedWith(compareBy({ calcPropertyWeight(it) }, { it.name }))
        variableList?.let { allNewDeclaration.addAll(it) }

        //initializer
        group[KtClassInitializer::class.java.name]?.let { allNewDeclaration.addAll(it) }

        //secondaryConstructor
        group[KtSecondaryConstructor::class.java.name]?.let { allNewDeclaration.addAll(it) }

        //sort method
        if (group[KtNamedFunction::class.java.name] != null) {
            val methods = group[KtNamedFunction::class.java.name] as ArrayList
            val overrideMethods = methods.filter { it.text.startsWith("override") }.sortedBy { it.name }
            methods.removeAll(overrideMethods)
            methods.sortWith(compareBy({ calcFunctionWeight(it) }, { it.name }))
            allNewDeclaration.addAll(methods)
            allNewDeclaration.addAll(overrideMethods)// add override methods to last
        }

        group.forEach { (key, subList) ->
            if (key !in listOf(
                    KtProperty::class.java.name,
                    KtClassInitializer::class.java.name,
                    KtSecondaryConstructor::class.java.name,
                    KtNamedFunction::class.java.name
                )
            ) {
                allNewDeclaration.addAll(subList)
            }
        }

        return allNewDeclaration
    }

    private fun calcFunctionWeight(func: KtDeclaration): Int {
        var result = 0
        if (func.hasModifier(KtTokens.PRIVATE_KEYWORD)) result += 16
        if (func.hasModifier(KtTokens.OPEN_KEYWORD)) result += 8
        if (func.hasModifier(KtTokens.INTERNAL_KEYWORD)) result += 4
        if (func.hasModifier(KtTokens.PUBLIC_KEYWORD)) result += 2
        if (func.hasModifier(KtTokens.ABSTRACT_KEYWORD)) result += 1
        return result
    }

    private fun calcPropertyWeight(property: KtDeclaration): Int {
        var result = 0
        if (property.hasModifier(KtTokens.LATEINIT_KEYWORD)) result += 32
        if (property.hasModifier(KtTokens.CONST_KEYWORD)) result += 16
        if (property.hasModifier(KtTokens.PUBLIC_KEYWORD)) result += 8
        if (property.hasModifier(KtTokens.INTERNAL_KEYWORD)) result += 4
        if (property.hasModifier(KtTokens.FINAL_KEYWORD)) result += 2
        if (property.hasModifier(KtTokens.PRIVATE_KEYWORD)) result += 1
        return result
    }
}
