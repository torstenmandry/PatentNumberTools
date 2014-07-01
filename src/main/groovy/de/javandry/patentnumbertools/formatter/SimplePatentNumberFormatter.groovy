package de.javandry.patentnumbertools.formatter

import de.javandry.patentnumbertools.PatentNumber

class SimplePatentNumberFormatter {

    private String formatString
    int serialLength

    SimplePatentNumberFormatter(String formatString) {
        this.formatString = formatString
        def matcher = formatString =~ "n+"
        serialLength = ((String) matcher[0]).length()
    }

    String format(PatentNumber patentNumber) {
        formatString
          .replace("cc", patentNumber.countryCode)
          .replace("yyyy", patentNumber.year ?: "")
          .replace("yy", patentNumber.year?.substring(2) ?: "")
          .replace("n" * serialLength, patentNumber.serial.padLeft(serialLength, '0'))
          .replace("kd", patentNumber.kindCode ?: "")
    }
}
