package org.jabref;

import org.jabref.logic.bibtex.comparator.BibStringDiff;
import org.jabref.logic.citationstyle.CitationStyle;
import org.jabref.logic.importer.fileformat.PdfContentImporter;
import org.jabref.migrations.PreferencesMigrations;
import org.junit.jupiter.api.Test;

public class CoverageMeasurementTest {

    @Test
    public void testAll(){
        boolean[] cit = CitationStyle.CIT;
        boolean[] bib = BibStringDiff.BIB;
        boolean[] fac = PreferencesMigrations.FacultyEncodingStrings;
        boolean[] sortOrder = PreferencesMigrations.UpgradeSortOrder;
        boolean[] pdf = PdfContentImporter.SLN;
      //  boolean[] cit = CitationStyle.CIT;
        CoverageMeasurement.PrintMap("CIT", cit);
        CoverageMeasurement.PrintMap("BIB", bib);
        CoverageMeasurement.PrintMap("FAC", fac);
        CoverageMeasurement.PrintMap("Sort order", sortOrder);
        CoverageMeasurement.PrintMap("PDF", pdf);
    }
}
