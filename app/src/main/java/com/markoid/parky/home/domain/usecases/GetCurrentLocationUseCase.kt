package com.markoid.parky.home.domain.usecases

import com.markoid.parky.core.domain.usecases.UseCase
import com.markoid.parky.position.data.entities.PositionEntity
import javax.inject.Inject

class GetCurrentLocationUseCase
@Inject constructor() : UseCase<PositionEntity, Unit>() {

    override suspend fun onExecute(request: Unit): PositionEntity {
        TODO()
    }
}
