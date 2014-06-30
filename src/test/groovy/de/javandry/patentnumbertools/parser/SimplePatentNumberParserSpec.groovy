package de.javandry.patentnumbertools.parser

import de.javandry.patentnumbertools.PatentNumber
import de.javandry.patentnumbertools.parser.SimplePatentNumberParser
import spock.lang.Specification
import spock.lang.Unroll

class SimplePatentNumberParserSpec extends Specification {

    @Unroll("parse '#input' with format string '#format'")
    def "parse"() {
        given:
        SimplePatentNumberParser parser = new SimplePatentNumberParser(format);

        when:
        PatentNumber patentNumber = parser.parse(input)

        then:
        patentNumber.countryCode == countryCode
        patentNumber.serial == serial
        patentNumber.kindCode == kindCode

        where:
        format           | input            | countryCode | year   | serial    | kindCode
        "ccnnnnnkd"      | "EP12345A1"      | "EP"        | null   | "12345"   | "A1"
        "ccnnnnnkd"      | "EP12345A"       | "EP"        | null   | "12345"   | "A"
        "ccnnnnnkd"      | "EP12345"        | "EP"        | null   | "12345"   | null
        "ccnnnnnnnkd"    | "EP0012345A1"    | "EP"        | null   | "0012345" | "A1"
        "ccyynnnnnkd"    | "WO9612345A1"    | "WO"        | "1996" | "12345"   | "A1"
        "ccyyyynnnnnnkd" | "WO2011123456A1" | "WO"        | "2011" | "123456"  | "A1"
    }

    @Unroll
    def "detect that '#input' does not match format '#format'"() {
        given:
        SimplePatentNumberParser parser = new SimplePatentNumberParser(format);

        when:
        parser.parse(input)

        then:
        thrown(IllegalArgumentException)

        where:
        format      | input
        "ccnnnnnkd" | "EP1234A1"
        "ccnnnnnkd" | "EP123456A1"
        "ccnnnnnkd" | "12345678A1"
    }
}
