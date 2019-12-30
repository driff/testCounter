package com.example.testcounter.utils

import io.realm.RealmObject

fun <T: RealmObject>T.unmanagedCopy(): T {
    return this.realm.copyFromRealm(this)
}