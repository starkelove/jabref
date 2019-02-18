package org.jabref.logic.bibtex.comparator;

import org.jabref.model.database.BibDatabase;
import org.jabref.model.entry.BibtexString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BibStringDiffTest {

    private BibDatabase database1;
    private BibDatabase database2;
    private BibtexString stringA;

    @BeforeEach
    public void setUp() {
        database1 = new BibDatabase();
        database2 = new BibDatabase();
        stringA = new BibtexString("TEST", "SOME STRING");
    }

    /**
     * Tests that two databases with identical content will be identified
     * Returns a List<BibStringDiff> that should be empty since there are no differences
     * author: Love Stark
     */
    @Test
    public void testIdenticalDatabases(){
        database1.addString(stringA);
        database2.addString(stringA);
        List<BibStringDiff> list = BibStringDiff.compare(database1, database2);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }


}
