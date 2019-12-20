package com.example.testcounter.data.models

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey

open class Counter(
    @Ignore @SerializedName("id") open var id: Long? = null,
    open var title: String? = null,
    open var count: Int? = null,
    @PrimaryKey open var ext_id: Long? = null
): RealmObject()