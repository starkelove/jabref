package org.jabref.logic.bibtex.comparator;

import java.util.*;

import org.jabref.model.database.BibDatabase;
import org.jabref.model.entry.BibtexString;
import org.jabref.CoverageMeasurement;

public class BibStringDiff {

    private final BibtexString originalString;
    private final BibtexString newString;
    public static final HashMap<String, Boolean[]> bibMap = new HashMap<String, Boolean[]>();
    //CoverageMeasurement.coverageMap.put("BIB", new Boolean[7]);
    //public CoverageMeasurement.coverageMap.put("BIB", new Boolean[7]);
    //HashMap<String, Boolean[]> coverageMap = new HashMap<>()

    private BibStringDiff(BibtexString originalString, BibtexString newString) {
        this.originalString = originalString;
        this.newString = newString;
    }

    public static List<BibStringDiff> compare(BibDatabase originalDatabase, BibDatabase newDatabase) {
       // CoverageMeasurement cover = new CoverageMeasurement();
       // CoverageMeasurement.coverageMap.put("BIB", new Boolean[5]);


        if (originalDatabase.hasNoStrings() && newDatabase.hasNoStrings()) {
            bibMap.get("BIB")[0] = true;
            System.out.println("print i impossible");
            return Collections.emptyList();
        }

        List<BibStringDiff> differences = new ArrayList<>();

        Set<BibtexString> used = new HashSet<>();
        Set<BibtexString> notMatched = new HashSet<>();

        // First try to match by string names.
        for (BibtexString original : originalDatabase.getStringValues()) {
            Optional<BibtexString> match = newDatabase.getStringValues().stream()
                    .filter(test -> test.getName().equals(original.getName()))
                    .findAny();
            if (match.isPresent()) {
                bibMap.get("BIB")[1] = true;
                // We have found a string with a matching name.
                if (!Objects.equals(original.getContent(), match.get().getContent())) {
                    // But they have non-matching contents, so we've found a change.
                    differences.add(new BibStringDiff(original, match.get()));
                    bibMap.get("BIB")[2] = true;

                }
                used.add(match.get());
            } else {
                // No match for this string.
                notMatched.add(original);
                bibMap.get("BIB")[3] = true;

            }
        }

        // See if we can detect a name change for those entries that we couldn't match, based on their content
        for (Iterator<BibtexString> iterator = notMatched.iterator(); iterator.hasNext(); ) {
            BibtexString original = iterator.next();
            Optional<BibtexString> match = newDatabase.getStringValues().stream()
                    .filter(test -> test.getName().equals(original.getName()))
                    .findAny();
            if (match.isPresent()) {
                bibMap.get("BIB")[4] = true;
                // We have found a string with the same content. It cannot have the same
                // name, or we would have found it above.
                differences.add(new BibStringDiff(original, match.get()));
                iterator.remove();
                used.add(match.get());
            }
        }

        // Strings that are still not found must have been removed.
        for (BibtexString original : notMatched) {
            differences.add(new BibStringDiff(original, null));
        }

        // Finally, see if there are remaining strings in the new database. They must have been added.
        newDatabase.getStringValues().stream()
                .filter(test -> !used.contains(test))
                .forEach(newString -> differences.add(new BibStringDiff(null, newString)));

        CoverageMeasurement.PrintMap(bibMap);
        return differences;
    }

    public BibtexString getOriginalString() {
        return originalString;
    }

    public BibtexString getNewString() {
        return newString;
    }
}
