package org.jabref.logic.bibtex.comparator;

import org.jabref.model.database.BibDatabase;
import org.jabref.model.entry.BibtexString;
import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BibStringDiffTest {

    private BibDatabase database1;
    private BibDatabase database2;

    /**
     * Tests that two databases with identical content will be identified
     * Returns a List<BibStringDiff> that should be empty since there are no differences
     * author: Love Stark
     */
    @Test
    public void testIdenticalDatabases(){
        database1 = new BibDatabase();
        database2 = new BibDatabase();
        BibtexString a = new BibtexString("TEST", "SOME STRING");
        database1.addString(a);
        database2.addString(a);

        List<BibStringDiff> list = BibStringDiff.compare(database1, database1);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

}
