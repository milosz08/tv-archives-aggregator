package pl.miloszgilga.archiver.backend.db;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class BlockingThreadsExecutor<I, O> {
  private final Function<I, O> futureCallback;

  private final ExecutorService executor;
  private final List<Future<O>> futures = new ArrayList<>();

  public BlockingThreadsExecutor(int poolSize, Function<I, O> futureCallback) {
    this.futureCallback = futureCallback;
    executor = Executors.newFixedThreadPool(poolSize);
  }

  public static <T, D> Map<T, D> flatListMapToMap(
    List<Map<T, D>> listMap,
    BinaryOperator<D> mergeFunction
  ) {
    return listMap.stream()
      .flatMap(map -> map.entrySet().stream())
      .collect(Collectors.toMap(
        Map.Entry::getKey,
        Map.Entry::getValue,
        mergeFunction)
      );
  }

  public void initThreads(List<I> inputData) {
    for (final I i : inputData) {
      futures.add(executor.submit(() -> futureCallback.apply(i)));
    }
  }

  public List<O> waitAndGet() {
    final List<O> calculatedData = new ArrayList<>();
    for (Future<O> future : futures) {
      try {
        final O calculatedNullableElement = future.get();
        if (calculatedNullableElement != null) {
          calculatedData.add(calculatedNullableElement);
        }
      } catch (Exception ex) {
        log.error(ex.getMessage());
      }
    }
    return calculatedData;
  }
}
