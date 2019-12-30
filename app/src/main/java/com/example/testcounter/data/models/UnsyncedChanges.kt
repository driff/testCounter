package com.example.testcounter.data.models

import io.realm.RealmObject

open class UnsyncedChanges(
    open var localId: String? = null,
    open var serverId: String? = null,
    open var type: Int? = null, // 0 = create, 1 = increase, 2 = decrease, 3 = delete
    open var status: Int? = null // 0 = unsynced, 1 = synced

) : RealmObject()