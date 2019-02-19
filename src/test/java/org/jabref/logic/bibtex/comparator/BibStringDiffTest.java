package org.jabref.logic.bibtex.comparator;

import org.jabref.CoverageMeasurement;
import org.jabref.model.database.BibDatabase;
import org.jabref.model.entry.BibtexString;
import org.junit.Before;
import org.junit.BeforeClass;
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
        stringA = new BibtexString("TEST", "some string");
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

    /**
     * Tests that two databases with not entirely identical content will be identified
     * The two databases will have a string each that share the same name but with different content.
     * The function should identify this and return what it is that is differing
     * Returns a List<BibStringDiff> that should contain stringA
     * author: Love Stark
     */
    @Test
    public void testDifferingContent(){
        BibtexString stringB = new BibtexString("TEST", "another string");
        database1.addString(stringA);
        database2.addString(stringB);
        List<BibStringDiff> list = BibStringDiff.compare(database1, database2);
        assertNotNull(list);
        assertTrue(!list.isEmpty());
        assertEquals(stringA, list.get(0).getOriginalString());
    }

    /**
     * Tests that two databases with completely different content will be identified
     * The two databases will have a string each that will not share either name or content.
     * The function should identify this and return what it is that is differing
     * Returns a List<BibStringDiff> that should contain stringA
     * author: Love Stark
     */
    @Test
    public void testNonIdentical(){
        BibtexString stringB = new BibtexString("ANOTHER", "another string");
        database1.addString(stringA);
        database2.addString(stringB);
        List<BibStringDiff> list = BibStringDiff.compare(database1, database2);
        assertNotNull(list);
        assertTrue(!list.isEmpty());
        assertEquals(stringA, list.get(0).getOriginalString());
    }


}
