package org.jabref;

import java.security.Key;
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
  public static int visits = 0;
  public static boolean[] BIB = new boolean[7];
  public static boolean[] LIB = new boolean[3];

    public static void init(String key){
      coverageMap.put(key, new Boolean[7]);
  }

  public static void PrintMap(HashMap map) {
    Iterator iterator = map.keySet().iterator();
  //  boolean[] bib = coverageMap.get("BIB").get(key);
    while(iterator.hasNext()) {
      int branchNum = 0;
      int temp = 0;
      String key = iterator.next().toString();
      for(Boolean branchVisit : map.get(key)) {
        if(branchVisit != null && branchVisit) {
            visits++;
            BIB[temp] = true;
          branchNum++;

        }
        temp++;
      }
      int newInt = 0;

        for(int i = 0; i < BIB.length; i++) {
            if(BIB[i]) {
                newInt++;

            }
        }
      System.out.println("BranchID: " + key + " " + "Visited: " + branchNum + " Visits: " +  newInt);
    }
  }
/*
  public void setMap(HashMap<String, Boolean[]> coverageMap){
      this.coverageMap = coverageMap;
  }

  public HashMap<String, Boolean[]> getMap(){
      return coverageMap;
  }
  */
}
