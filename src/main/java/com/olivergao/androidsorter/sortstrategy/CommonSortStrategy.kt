package com.olivergao.androidsorter.sortstrategy

import org.jetbrains.kotlin.psi.*

/**
 * Created by XQ Yang on 9/21/2018  5:06 PM.
 * Description :
 */

class CommonSortStrategy(mAllDeclarations: List<KtDeclaration>) : BaseSortStrategy(mAllDeclarations) {
    init {
        mOrdering = ArrayList()
        mOrdering?.let {
            it.add(KtProperty::class.java.name)
            it.add(KtClassInitializer::class.java.name)
            it.add(KtSecondaryConstructor::class.java.name)
            it.add(KtNamedFunction::class.java.name)
            it.add(KtClass::class.java.name)
            it.add(KtObjectDeclaration::class.java.name)
        }
    }
}