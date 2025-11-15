package com.lazytravel.data.base

import com.lazytravel.data.remote.schema.CollectionType
import com.lazytravel.data.remote.schema.FieldsBuilder
import com.lazytravel.data.remote.schema.collectionSchema

fun baseCollection(
    name: String,
    fieldsInit: FieldsBuilder.() -> Unit
) = collectionSchema {
    this.name = name
    type = CollectionType.BASE
    fields {
        fieldsInit()
        bool("active")
        bool("deleted")
        date("createdAt")
        date("updatedAt")
    }
    listRule = null
    viewRule = null
    createRule = null
    updateRule = null
    deleteRule = null
}