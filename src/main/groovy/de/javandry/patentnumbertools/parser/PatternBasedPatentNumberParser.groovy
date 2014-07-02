package de.javandry.patentnumbertools.parser

import de.javandry.patentnumbertools.PatentNumber

import java.util.regex.Pattern

/**
 * PatentNumberParser that parses patent numbers based on a pattern string like "ccnnnnnkd".<br/>
 * <br/>
 * Example<br/>
 * <code>new PatternBasedPatentNumberParser("ccnnnnnkd").parse("EP12345A1")</code><br/>
 * <br/>
 * Use the following characters to define the pattern string:<br/>
 * <ul>
 *     <li>"cc" two character country code (mandatory)</li>
 *     <li>"yy" or "yyyy" two or four digit year (mandatory)</li>
 *     <li>"n" one digit of the numeric serial (mandatory)</li>
 *     <li>"kd" one or two characters kind code (optional)</li>
 * </ul>
 * @author torsten.mandry@opitz-consulting.com
 */
class PatternBasedPatentNumberParser {

    private static final String PATTERN_COUNTRY_CODE = "cc"
    private static final String PATTERN_SERIAL = "n+"
    private static final String PATTERN_KIND_CODE = "kd"
    private static final String PATTERN_YEAR_2_DIGITS = "yy"
    private static final String PATTERN_YEAR_4_DIGITS = "yyyy"

    private static final String REGEXP_COUNTRY_CODE = "(?<cc>[a-zA-Z]{2})"
    private static final String REGEXP_SERIAL = "(?<serial>\\\\d{#})"
    private static final String REGEXP_KIND_CODE = "(?<kd>[a-zA-Z][a-zA-Z0-9]?)?"
    private static final String REGEXP_YEAR_4_DIGITS = "(?<year>\\\\d{4})"
    private static final String REGEXP_YEAR_2_DIGITS = "(?<year>\\\\d{2})"

    private String patternString
    private String regExp
    private Pattern pattern

    PatternBasedPatentNumberParser(String patternString) {
        this.patternString = patternString
        regExp = "^${patternString}\$"
                .replaceAll(PATTERN_COUNTRY_CODE, REGEXP_COUNTRY_CODE)
                .replaceAll(PATTERN_YEAR_4_DIGITS, REGEXP_YEAR_4_DIGITS)
                .replaceAll(PATTERN_YEAR_2_DIGITS, REGEXP_YEAR_2_DIGITS)
                .replaceAll(PATTERN_SERIAL, REGEXP_SERIAL.replace("#", patternString.count("n").toString()))
                .replaceAll(PATTERN_KIND_CODE, REGEXP_KIND_CODE)
        pattern = Pattern.compile(regExp)
    }

    PatentNumber parse(String numberString) {
        def matcher = pattern.matcher(numberString)
        if (!matcher.matches())
            throw new IllegalArgumentException("'" + numberString + "' does not match pattern '" + patternString + "'")

        String countryCode = regExp.contains("<cc>") ? matcher.group("cc") : null
        String year = regExp.contains("<year>") ? matcher.group("year") : null
        String serial = regExp.contains("<serial>") ? matcher.group("serial") : null
        String kindCode = regExp.contains("<kd>") ? matcher.group("kd") : null

        new PatentNumber(countryCode, year, serial, kindCode)
    }

}
