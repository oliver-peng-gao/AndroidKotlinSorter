package com.olivergao.androidsorter.action

import org.jetbrains.kotlin.psi.KtDeclaration

data class SortedData(var startOffsetOverrideMethods: Int = 0,
                      var startOffsetDeclaredMethods: Int = 0,
                      val sortedList: List<KtDeclaration>) {
}