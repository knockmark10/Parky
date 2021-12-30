package com.markoid.parky.home.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.markoid.parky.core.data.dao.AbstractDao
import com.markoid.parky.home.data.entities.ExclusionZoneEntity

@Dao
interface ExclusionZoneDao : AbstractDao<ExclusionZoneEntity> {

    @Query("DELETE FROM exclusion_zones WHERE id=:id")
    fun delete(id: Long)

    @Query("SELECT * FROM exclusion_zones")
    fun getExclusionZones(): List<ExclusionZoneEntity>
}
