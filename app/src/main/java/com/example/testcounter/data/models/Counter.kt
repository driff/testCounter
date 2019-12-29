package com.example.testcounter.data.models

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey

open class Counter(
    @PrimaryKey var localId: Long?,
    open var title: String? = null,
    open var count: Int? = null,
    @SerializedName("id") open var serverId: Long? = null
): RealmObject() {
    constructor() : this(null, null, null, null)
}