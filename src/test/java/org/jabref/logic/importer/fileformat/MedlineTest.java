package org.jabref.logic.importer.fileformat;

import org.jabref.logic.importer.fileformat.medline.Investigator;
import org.jabref.logic.util.StandardFileType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MedlineTest {

    @Test
    public void testGetAndSetValidYN(){
        Investigator temp = new Investigator();
        temp.setValidYN(null);
        assertEquals("Y", temp.getValidYN());
        temp.setValidYN("hej");
        assertEquals("hej", temp.getValidYN());
    }
}
