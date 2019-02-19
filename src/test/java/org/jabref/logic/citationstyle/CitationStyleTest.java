package org.jabref.logic.citationstyle;

//import org.jabref.logic.util.TestEntry;

import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

//import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CitationStyleTest {

    private CitationStyle citationStyle;


    @Test
    public void getDefault() throws Exception {
        assertNotNull(CitationStyle.getDefault());
    }

    /*
    @Test
    public void testDefaultCitation() {
        String citation = CitationStyleGenerator.generateCitation(TestEntry.getTestEntry(), CitationStyle.getDefault());

        // if the default citation style changes this has to be modified
        String expected = "  <div class=\"csl-entry\">\n" +
                "    <div class=\"csl-left-margin\">[1]</div><div class=\"csl-right-inline\">" +
                "B. Smith, B. Jones, and J. Williams, “Title of the test entry,” " +
                "<i>BibTeX Journal</i>, vol. 34, no. 3, pp. 45–67, Jul. 2016.</div>\n" +
                "  </div>\n";

        assertEquals(expected, citation);
    }
    */

    /**
     * Tests to see if an empty list is returned. Assert that we have actually received a list
     * but also that this list is empty
     * @author Love Stark & Anton Hedkrans
     * @throws Exception
     */
    @Test
    public void testDiscoverCitationStyles() throws Exception {
        List<CitationStyle> list =  CitationStyle.discoverCitationStyles();
        assertNotNull(list);
        assertTrue(list.isEmpty());

    }


}
