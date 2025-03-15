package pl.miloszgilga.archiver.backend.features.tvchannel.util;

import org.junit.jupiter.api.Test;
import pl.miloszgilga.archiver.backend.util.AppUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class AppUtilsTest {
  @Test
  public void testReduce2dList() {
    //given
    final List<List<Integer>> input = List.of(
      List.of(10, 20, 30),
      List.of(20, 10, 15)
    );

    //when
    final List<Integer> expected = List.of(30, 30, 45);

    //then
    final List<Integer> result = AppUtils.reduce2dList(expected.size(), input);
    assertArrayEquals(expected.toArray(), result.toArray());
  }
}
