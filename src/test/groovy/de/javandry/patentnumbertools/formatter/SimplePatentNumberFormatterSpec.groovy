package de.javandry.patentnumbertools.formatter

import de.javandry.patentnumbertools.PatentNumber
import spock.lang.Specification
import spock.lang.Unroll

class SimplePatentNumberFormatterSpec extends Specification {

    @Unroll("format [#countryCode #serial] with format '#formatString'")
    def "format patent number without kind code"() {
        given:
        SimplePatentNumberFormatter formatter = new SimplePatentNumberFormatter(formatString)

        when:
        def formattedNumber = formatter.format(new PatentNumber(countryCode, serial))

        then:
        formattedNumber == expectedNumber

        where:
        formatString  | countryCode | serial  | expectedNumber
        "ccnnnnnnnkd" | "EP"        | "12345" | "EP0012345"
    }

    @Unroll("format [#countryCode #serial #kindCode] with format '#formatString'")
    def "format patent number with kind code"() {
        given:
        SimplePatentNumberFormatter formatter = new SimplePatentNumberFormatter(formatString)

        when:
        def formattedNumber = formatter.format(new PatentNumber(countryCode, serial, kindCode))

        then:
        formattedNumber == expectedNumber

        where:
        formatString  | countryCode | serial    | kindCode | expectedNumber
        "ccnnnnnnnkd" | "EP"        | "12345"   | "A1"     | "EP0012345A1"
        "ccnnnnnnnkd" | "EP"        | "12345"   | "A"      | "EP0012345A"
        "ccnnnnnkd"   | "EP"        | "12345"   | "A1"     | "EP12345A1"
        "ccnnnnnkd"   | "EP"        | "1234567" | "A1"     | "EP1234567A1"
    }

    @Unroll("format [#countryCode #year #serial #kindCode] with format '#formatString'")
    def "format patent number with year and kind code"() {
        given:
        SimplePatentNumberFormatter formatter = new SimplePatentNumberFormatter(formatString)

        when:
        def formattedNumber = formatter.format(new PatentNumber(countryCode, year, serial, kindCode))

        then:
        formattedNumber == expectedNumber

        where:
        formatString      | countryCode | year   | serial    | kindCode | expectedNumber
        "ccyyyynnnnnnnkd" | "US"        | "2010" | "12345"   | "A1"     | "US20100012345A1"
        "ccyyyynnnnnkd"   | "US"        | "2010" | "12345"   | "A1"     | "US201012345A1"
        "ccyyyynnnnnkd"   | "US"        | "2010" | "1234567" | "A1"     | "US20101234567A1"
        "ccyynnnnnnnkd"   | "US"        | "2010" | "12345"   | "A1"     | "US100012345A1"
        "ccyynnnnnkd"     | "US"        | "2010" | "12345"   | "A1"     | "US1012345A1"
    }
}
