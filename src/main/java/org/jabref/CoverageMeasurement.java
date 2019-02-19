package org.jabref;

/**
*To initialize the hashmap for each function we're testing use:
*CoverageMeasurement.coverageMap.put("Name of function", new Boolean[size of array]);
*To set a "flag" to check if the branch was visited use: CoverageMeasurement.coverageMap.get("Name of function")[place in the array, int] = true;
**/
public class CoverageMeasurement {
  // <BranchID, If branch visited mark as True>


  public static void PrintMap(String id, boolean[] map) {
    //Iterator iterator = map.keySet().iterator();
  //  boolean[] bib = coverageMap.get("BIB").get(key);

      int newInt = 0;
      for(int i = 0; i < map.length; i++) {
          if(map[i]) {
              newInt++;
              System.out.println(i);
          }
        }
      System.out.println("ID: " + id + " " +  newInt +  " out of " + map.length + " branches covered.");
    }

}
