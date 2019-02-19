package org.jabref;

import java.util.HashMap;
import java.util.Iterator;
/**
*To initialize the hashmap for each function we're testing use:
*CoverageMeasurement.coverageMap.put("Name of function", new Boolean[size of array]);
*To set a "flag" to check if the branch was visited use: CoverageMeasurement.coverageMap.get("Name of function")[place in the array, int] = true;
**/
public class CoverageMeasurement {
  // <BranchID, If branch visited mark as True>
  public static final HashMap<String, Boolean[]> coverageMap = new HashMap<String, Boolean[]>();

  static {
    coverageMap.put("ID1", new Boolean[5]);
    coverageMap.put("ID2", new Boolean[5]);
    coverageMap.put("ID3", new Boolean[5]);

    coverageMap.get("ID1")[0] = true;
    coverageMap.get("ID1")[1] = true;
    coverageMap.get("ID1")[2] = false;
    coverageMap.get("ID2")[0] = true;
    coverageMap.get("ID2")[1] = true;
    coverageMap.get("ID2")[2] = true;
    coverageMap.get("ID2")[3] = true;
    coverageMap.get("ID2")[4] = true;
    coverageMap.get("ID3")[1] = false;
}

  public static void PrintMap() {
    Iterator iterator = coverageMap.keySet().iterator();
    int totalBranches = 0;

    while(iterator.hasNext()) {
      int branchNum = 0;
      String key = iterator.next().toString();
      for(Boolean branchVisit : coverageMap.get(key)) {
        if(branchVisit != null && branchVisit) {
          totalBranches++;
          branchNum++;
        }
      }
      System.out.println("BranchID: " + key + " " + "Visited: " + branchNum + "Total branches:" + totalBranches);
    }
  }
}
