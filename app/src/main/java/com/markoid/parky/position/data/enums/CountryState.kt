package com.markoid.parky.position.data.enums

import java.text.Normalizer

enum class CountryState(val stateName: String, val stateCode: String, val country: Country) {
    // USA States
    AL("Alabama", "AL", Country.USA),
    AK("Alaska", "AK", Country.USA),
    AZ("Arizona", "AZ", Country.USA),
    AR("Arkansas", "AR", Country.USA),
    CA("California", "CA", Country.USA),
    CO("Colorado", "CO", Country.USA),
    CT("Connecticut", "CT", Country.USA),
    DE("Delaware", "DE", Country.USA),
    FL("Florida", "FL", Country.USA),
    GA("Georgia", "GA", Country.USA),
    HI("Hawaii", "HI", Country.USA),
    ID("Idaho", "ID", Country.USA),
    IL("Illinois", "IL", Country.USA),
    IN("Indiana", "IN", Country.USA),
    IA("Iowa", "IA", Country.USA),
    KS("Kansas", "KS", Country.USA),
    KY("Kentucky", "KY", Country.USA),
    LA("Louisiana", "LA", Country.USA),
    ME("Maine", "ME", Country.USA),
    MD("Maryland", "MD", Country.USA),
    MA("Massachusetts", "MA", Country.USA),
    MI("Michigan", "MI", Country.USA),
    MN("Minnesota", "MN", Country.USA),
    MS("Mississippi", "MS", Country.USA),
    MO("Missouri", "MO", Country.USA),
    MT("Montana", "MT", Country.USA),
    NE("Nebraska", "NE", Country.USA),
    NV("Nevada", "NV", Country.USA),
    NH("New Hampshire", "NH", Country.USA),
    NJ("New Jersey", "NJ", Country.USA),
    NM("New Mexico", "NM", Country.USA),
    NY("New York", "NY", Country.USA),
    NC("North Carolina", "NC", Country.USA),
    ND("North Dakota", "ND", Country.USA),
    OH("Ohio", "OH", Country.USA),
    OK("Oklahoma", "OK", Country.USA),
    OR("Oregon", "OR", Country.USA),
    PA("Pennsylvania", "PA", Country.USA),
    RI("Rhode Island", "RI", Country.USA),
    SC("South Carolina", "SC", Country.USA),
    SD("South Dakota", "SD", Country.USA),
    TN("Tennessee", "TN", Country.USA),
    TX("Texas", "TX", Country.USA),
    UT("Utah", "UT", Country.USA),
    VT("Vermont", "VT", Country.USA),
    VA("Virginia", "VA", Country.USA),
    WA("Washington", "WA", Country.USA),
    WV("West Virginia", "WV", Country.USA),
    WI("Wisconsin", "WI", Country.USA),
    WY("Wyoming", "WY", Country.USA),

    // Provinces and Territories
    AS("American Samoa", "AS", Country.USA),
    DC("District of Columbia", "DC", Country.USA),
    GU("Guam", "GU", Country.USA),
    MP("Commonwealth of the Northern Mariana Islands", "MP", Country.USA),
    PR("Puerto Rico", "PR", Country.USA),
    VI("United States Virgin Islands", "VI", Country.USA),

    // CANADA Provinces
    AB("Alberta", "AB", Country.Canada),
    BC("British Columbia", "BC", Country.Canada),
    MB("Manitoba", "MB", Country.Canada),
    NB("New Brunswick", "NB", Country.Canada),
    NL("Newfoundland and Labrador", "NL", Country.Canada),
    NS("Nova Scotia", "NS", Country.Canada),
    ON("Ontario", "ON", Country.Canada),
    PE("Prince Edward Island", "PE", Country.Canada),
    QC("Quebec", "QC", Country.Canada),
    SK("Saskatchewan", "SK", Country.Canada),

    // Canadian Territories
    CK("Canuck", "CK", Country.Canada),
    NU("Nunavut", "NU", Country.Canada),
    YT("Yukon", "YT", Country.Canada),
    NT("Northwest Territories", "NT", Country.Canada),

    // Mexican States
    MEX_AG("Aguascalientes", "AGU", Country.Mexico),
    MEX_BN("Baja California Norte", "BJBCN", Country.Mexico),
    MEX_BS("Baja California Sur", "BSBCS", Country.Mexico),
    MEX_CP("Campeche", "CPCMCAM", Country.Mexico),
    MEX_CS("Chiapas", "CHCSCHP", Country.Mexico),
    MEX_CI("Chihuahua", "CICHH", Country.Mexico),
    MEX_CH("Coahuila", "CUCOA", Country.Mexico),
    MEX_CL("Colima", "CLCOL", Country.Mexico),
    MEX_DF("Ciudad de México", "DFCMX", Country.Mexico),
    MEX_DG("Durango", "DGDUR", Country.Mexico),
    MEX_GJ("Guanajuato", "GJGUA", Country.Mexico),
    MEX_GE("Guerrero", "GRO", Country.Mexico),
    MEX_HD("Hidalgo", "HGHID", Country.Mexico),
    MEX_JA("Jalisco", "JAL", Country.Mexico),
    MEX_MX("Estado de Mexico", "EMMEX", Country.Mexico),
    MEX_MC("Michoacan", "MCMHMIC", Country.Mexico),
    MEX_MR("Morelos", "MRMOR", Country.Mexico),
    MEX_NA("Nayarit", "NAY", Country.Mexico),
    MEX_NL("Nuevo Leon", "NXNLE", Country.Mexico),
    MEX_OA("Oaxaca", "OAX", Country.Mexico),
    MEX_PU("Puebla", "PUE", Country.Mexico),
    MEX_QE("Queretaro", "QAQTQUE", Country.Mexico),
    MEX_QI("Quinatana Roo", "QRROO", Country.Mexico),
    MEX_SL("San Luis Potosi", "SLP", Country.Mexico),
    MEX_SI("Sinaloa", "SIN", Country.Mexico),
    MEX_SO("Sonora", "SON", Country.Mexico),
    MEX_TB("Tabasco", "TBTAB", Country.Mexico),
    MEX_TA("Tamaulipas", "TATMTAM", Country.Mexico),
    MEX_TL("Tlaxcala", "TLA", Country.Mexico),
    MEX_VC("Veracruz", "VZVLVER", Country.Mexico),
    MEX_YU("Yucatan", "YCYUC", Country.Mexico),
    MEX_ZA("Zacatecas", "ZTZAC", Country.Mexico);

    companion object {
        fun getStateFromName(name: String?): CountryState? {
            if (name.isNullOrBlank()) return null
            return values().firstOrNull {
                it.stateName.unaccent().equals(name.unaccent(), true) ||
                    it.stateName.unaccent().contains(name.unaccent())
            }
        }

        fun getStateFromCode(code: String?): CountryState? {
            if (code.isNullOrBlank()) return null
            return values().firstOrNull {
                it.stateCode == code || it.stateCode.contains(code, true)
            }
        }
    }
}

fun CharSequence.unaccent(): String {
    val unAccentRegex = "\\p{InCombiningDiacriticalMarks}+".toRegex()
    val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
    return unAccentRegex.replace(temp, "")
}
