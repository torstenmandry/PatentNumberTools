package de.javandry.patentnumbertools.formatter

import de.javandry.patentnumbertools.PatentNumber

/**
 * Simple PatentNumberFormatter that formats PatentNumbers based on a format String (like "ccnnnnnkd").<br/>
 * <br/>
 * Example<br/>
 * <code>new SimplePatentNumberFormatter("ccyyyynnnnnnkd").format(new PatentNumber("US", "2010", "12345", "A1"))</code><br/>
 * <br/>
 * Use the following characters to define the pattern string:<br/>
 * <ul>
 *     <li>"cc" two character country code</li>
 *     <li>"yy" or "yyyy" two or four digit year</li>
 *     <li>"n" one digit of the numeric serial (shorter serials will be filled with leading zeros)</li>
 *     <li>"kd" one or two characters kind code</li>
 * </ul>
 * @author torsten.mandry@opitz-consulting.com
 */
class SimplePatentNumberFormatter {

    private String format
    int serialLength

    SimplePatentNumberFormatter(String format) {
        this.format = format
        def matcher = format =~ "n+"
        serialLength = ((String) matcher[0]).length()
    }

    String format(PatentNumber patentNumber) {
        format
          .replace("cc", patentNumber.countryCode)
          .replace("yyyy", patentNumber.year ?: "")
          .replace("yy", patentNumber.year?.substring(2) ?: "")
          .replace("n" * serialLength, patentNumber.serial.padLeft(serialLength, '0'))
          .replace("kd", patentNumber.kindCode ?: "")
    }
}
