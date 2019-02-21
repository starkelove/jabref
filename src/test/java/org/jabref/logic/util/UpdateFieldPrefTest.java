package org.jabref.logic.util;

import org.junit.jupiter.api.Test;

import org.jabref.model.FieldChange;
import org.jabref.model.entry.BibEntry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateFieldPrefTest {

    @Test
    public void testUPF(){
        UpdateFieldPreferences temp = new UpdateFieldPreferences(true, true, "apa", true, true, "banan", "träd");
        assertEquals(true, temp.isUseOwner());
        assertEquals(true, temp.isOverwriteOwner());
        assertEquals("apa", temp.getDefaultOwner());
        assertEquals(true, temp.isUseTimeStamp());
        assertEquals(true, temp.isOverwriteTimeStamp());
        assertEquals("banan", temp.getTimeStampField());
        assertEquals("träd", temp.getTimeStampFormat());

    }

    @Test
    public void testUPF2(){
        UpdateFieldPreferences temp = new UpdateFieldPreferences(true, true, "apa", true, true, "banan", "träd");
        assertNotEquals(false, temp.isUseOwner());
        assertNotEquals(false, temp.isOverwriteOwner());
        assertNotEquals("aaaaapa", temp.getDefaultOwner());
        assertNotEquals(false, temp.isUseTimeStamp());
        assertNotEquals(false, temp.isOverwriteTimeStamp());
        assertNotEquals("banaaaaan", temp.getTimeStampField());
        assertNotEquals("trääääd", temp.getTimeStampFormat());
    }
}
