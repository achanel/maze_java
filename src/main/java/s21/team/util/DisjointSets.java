package s21.team.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class DisjointSets {

  private final List<Map<Integer, Set<Integer>>> disjointSet;

  public DisjointSets() {
    disjointSet = new ArrayList<>();
  }

  public void setCreation(int element) {
    Map<Integer, Set<Integer>> map = new HashMap<>();
    Set<Integer> set = new HashSet<>();

    set.add(element);
    map.put(element, set);

    disjointSet.add(map);
  }

  public void union(int first, int second) {

    int firstRep = setFinding(first);
    int secondRep = setFinding(second);

    Set<Integer> firstSet = null;
    Set<Integer> secondSet = null;

    for (Map<Integer, Set<Integer>> map : disjointSet) {
      if (map.containsKey(firstRep)) {
        firstSet = map.get(firstRep);
      } else if (map.containsKey(secondRep)) {
        secondSet = map.get(secondRep);
      }
    }

    if (firstSet != null && secondSet != null)
      firstSet.addAll(secondSet);

    for (int index = 0; index < disjointSet.size(); index++) {

      Map<Integer, Set<Integer>> map = disjointSet.get(index);

      if (map.containsKey(firstRep)) {
        map.put(firstRep, firstSet);
      } else if (map.containsKey(secondRep)) {
        map.remove(secondRep);
        disjointSet.remove(index);
      }
    }
  }

  public int setFinding(int element) {
    for (Map<Integer, Set<Integer>> map : disjointSet) {
      Set<Integer> keySet = map.keySet();

      for (Integer key : keySet) {
        Set<Integer> set = map.get(key);
        if (set.contains(element)) {
          return key;
        }
      }
    }
    return -1;
  }
}