package com.markoid.parky.position.data.enums

enum class Country {
    USA,
    Canada,
    Mexico
}

val Country.isUSA: Boolean
    get() = this == Country.USA

val Country.isCanada: Boolean
    get() = this == Country.Canada

val Country.isMexico: Boolean
    get() = this == Country.Mexico
