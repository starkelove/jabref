package org.jabref;

import java.util.HashMap;
import java.util.Iterator;

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
