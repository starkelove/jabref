package org.jabref.logic.importer.fileformat;

import org.jabref.logic.importer.fileformat.medline.Investigator;
import org.jabref.logic.importer.fileformat.medline.KeywordList;
import org.jabref.logic.util.StandardFileType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MedlineTest {

    /**
     * Tests both getValidYN and setValidYN through setting a value
     * and testing that the correct value is returned for
     * both possible returns from get.
     * Author: Victor
     */
    @Test
    public void testGetAndSetValidYN(){
        Investigator temp = new Investigator();
        temp.setValidYN(null);
        assertEquals("Y", temp.getValidYN());
        temp.setValidYN("hej");
        assertEquals("hej", temp.getValidYN());
    }

    /**
     * tests getOwner and setOwner by
     * setting and getting the same value
     * then setting and not getting the
     * same value
     * Author: Victor
     */
    @Test
    public void testGetAndSetOwner(){
        KeywordList temp = new KeywordList();
        temp.setOwner("apa");
        assertEquals("apa", temp.getOwner());
        assertNotEquals("banan", temp.getOwner());
        temp.setOwner(null);
        assertEquals("NLM", temp.getOwner());
    }

}
