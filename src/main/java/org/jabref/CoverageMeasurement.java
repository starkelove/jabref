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

  public static void PrintMap() {
    Iterator iterator = coverageMap.keySet().iterator();

    while(iterator.hasNext()) {
      int branchNum = 0;
      String key = iterator.next().toString();
      for(Boolean branchVisit : coverageMap.get(key)) {
        if(branchVisit != null && branchVisit) {
          branchNum++;
        }
      }
      System.out.println("BranchID: " + key + " " + "Visited: " + branchNum);
    }
  }
}
