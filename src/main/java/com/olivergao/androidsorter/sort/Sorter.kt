package com.olivergao.androidsorter.sort

import com.olivergao.androidsorter.sortstrategy.CommonSortStrategy
import org.jetbrains.kotlin.psi.KtClassOrObject


/**
 * Created by XQ Yang on 9/21/2018  5:06 PM.
 * Description :
 */

class Sorter(private val mPsiClass: KtClassOrObject) {

    fun sort() {
        realSort(mPsiClass)
    }

    private fun realSort(classOrObject: KtClassOrObject) {
        val declarations = classOrObject.declarations
        val before = declarations.hashCode()
        val sortedList = CommonSortStrategy(declarations).sort()
        val after = sortedList.hashCode()

        sortedList.forEach {
            if (it is KtClassOrObject) {
                realSort(it)
            }
        }
        if (before != after) {
            sortedList.forEach {
                classOrObject.addDeclaration(it)
            }
            declarations.forEach {
                it.delete()
            }
        }
    }
}
