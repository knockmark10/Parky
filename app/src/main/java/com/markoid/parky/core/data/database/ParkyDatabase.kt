package com.markoid.parky.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.markoid.parky.core.data.entities.TestEntity

@Database(
    entities = [TestEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ParkyDatabase : RoomDatabase()
