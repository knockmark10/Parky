package com.markoid.parky.core.data.dao

import androidx.room.* // ktlint-disable no-wildcard-imports

@Dao
interface AbstractDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: T)

    @Update
    suspend fun update(item: T)

    @Delete
    suspend fun delete(item: T)
}
