package de.javandry.patentnumbertools

class PatentNumber {
    String countryCode
    String year
    String serial
    String kindCode

    PatentNumber(String countryCode, String year, String serial, String kindCode) {
        this.countryCode = countryCode
        this.year = year
        this.serial = serial
        this.kindCode = kindCode
    }

    PatentNumber(String countryCode, String serial, String kindCode) {
        this.countryCode = countryCode
        this.serial = serial
        this.kindCode = kindCode
    }
}
