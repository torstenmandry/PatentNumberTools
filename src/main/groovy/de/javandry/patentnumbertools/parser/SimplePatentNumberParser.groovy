package de.javandry.patentnumbertools.parser

import de.javandry.patentnumbertools.PatentNumber

/**
 * Simple PatentNumberParser that parses patent numbers based on a simple format string like "ccnnnnnkd".<br/>
 * Use the following characters to define the format string:<br/>
 * <ul>
 *     <li>"cc" two character country code (mandatory)</li>
 *     <li>"yy" or "yyyy" two or four digit year (mandatory)</li>
 *     <li>"n" one digit of the numeric serial (mandatory)</li>
 *     <li>"kd" one or two characters kind code (optional)</li>
 * </ul>
 * @author torsten.mandry@opitz-consulting.com
 */
class SimplePatentNumberParser {

    private static final String FORMAT_COUNTRY_CODE = "cc"
    private static final String FORMAT_NUMBER_DIGIT = "n"
    private static final String FORMAT_KIND_CODE = "kd"
    private static final String FORMAT_YEAR_DIGIT = "y"
    private static final String FORMAT_YEAR_2_DIGITS = "yy"
    private static final String FORMAT_YEAR_4_DIGITS = "yyyy"

    private static final String REGEXP_DIGIT = "\\\\d"
    public static final String REGEXP_ALPHA = "[a-zA-Z]"
    public static final String REGEXP_ALNUM = "[a-zA-Z0-9]"
    public static final String REGEXP_COUNTRY_CODE = REGEXP_ALPHA + "{2}"
    public static final String REGEXP_KIND_CODE = "(" + REGEXP_ALPHA + REGEXP_ALNUM + "?)?"
    public static final String REGEXP_YEAR_4_DIGITS = REGEXP_DIGIT + "{4}"
    public static final String REGEXP_YEAR_2_DIGITS = REGEXP_DIGIT + "{2}"

    private String formatString
    private String formatRegExp

    SimplePatentNumberParser(String formatString) {
        this.formatString = formatString
        this.formatRegExp = "^${formatString}\$"
                .replaceAll(FORMAT_COUNTRY_CODE, REGEXP_COUNTRY_CODE)
                .replaceAll(FORMAT_YEAR_4_DIGITS, REGEXP_YEAR_4_DIGITS)
                .replaceAll(FORMAT_YEAR_2_DIGITS, REGEXP_YEAR_2_DIGITS)
                .replaceAll(FORMAT_NUMBER_DIGIT, REGEXP_DIGIT)
                .replaceAll(FORMAT_KIND_CODE, REGEXP_KIND_CODE)
    }

    PatentNumber parse(String numberString) {
        validateFormat(numberString)

        String countryCode = extractCountryCode(numberString)
        String year = extractYear(numberString)
        String serial = extractSerial(numberString)
        String kindCode = extractKindCode(numberString)

        new PatentNumber(countryCode, year, serial, kindCode)
    }

    private void validateFormat(String numberString) {
        if (numberString.matches(formatRegExp))
            return

        System.err.println("input '" +  numberString + "' does not match format '" + formatString + "' (regular expression '" + formatRegExp + "'")
        throw new IllegalArgumentException("'" + numberString + "' does not match format '" + formatString + "'")
    }

    private String extractYear(String numberString) {
        int yearStartIndex = formatString.indexOf(FORMAT_YEAR_2_DIGITS)

        if (yearStartIndex < 0) {
            return null
        }

        int yearEndIndex = formatString.lastIndexOf(FORMAT_YEAR_DIGIT)
        String year = numberString.substring(yearStartIndex, yearEndIndex + 1)

        if (year.length() == 4)
            return year

        if (year.toInteger() > 60) {
            return "19" + year
        } else {
            return "20" + year
        }
    }

    private String extractCountryCode(String numberString) {
        int countryCodeStartIndex = formatString.indexOf(FORMAT_COUNTRY_CODE)
        def countryCode = numberString.substring(countryCodeStartIndex, countryCodeStartIndex + 2)
        countryCode
    }

    private String extractSerial(String numberString) {
        int numberPartStartIndex = formatString.indexOf(FORMAT_NUMBER_DIGIT);
        int numberPartEndIndex = formatString.lastIndexOf(FORMAT_NUMBER_DIGIT)
        numberString.substring(numberPartStartIndex, numberPartEndIndex + 1)
    }

    private String extractKindCode(String numberString) {
        int kindCodeStartIndex = formatString.indexOf(FORMAT_KIND_CODE)
        if (numberString.length() < kindCodeStartIndex + 1)
            return null
        numberString.substring(kindCodeStartIndex)
    }

}
