package pl.miloszgilga.archiver.backend.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppUtils {
  public static List<Integer> reduce2dList(int size, List<List<Integer>> list2d) {
    final Integer[] reducedArray = new Integer[size];
    Arrays.fill(reducedArray, 0);
    for (int j = 0; j < reducedArray.length; j++) {
      for (final List<Integer> restOfDatum : list2d) {
        reducedArray[j] += restOfDatum.get(j);
      }
    }
    return List.of(reducedArray);
  }
}
