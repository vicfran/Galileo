package com.josedlpozo.galileo.realm.realmbrowser.models.model

sealed class Sort

object Asc : Sort()
object Desc : Sort()

fun Sort.inverse(): Sort = when (this) {
    Asc -> Desc
    Desc -> Asc
}