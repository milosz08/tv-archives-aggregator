package pl.miloszgilga.archiver.scrapper.state;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

@Slf4j
public abstract class AbstractDisposableProvider {
  private final ConcurrentMap<UUID, Disposable> subscriptionsPool;

  public AbstractDisposableProvider() {
    subscriptionsPool = new ConcurrentHashMap<>();
  }

  public <T> void asDisposable(Observable<T> subject, Consumer<T> consumer) {
    final Disposable disposable = subject.subscribe(consumer::accept);
    subscriptionsPool.put(UUID.randomUUID(), disposable);
  }

  public void cleanupAndDisposableSubscription() {
    for (final Map.Entry<UUID, Disposable> entry : subscriptionsPool.entrySet()) {
      entry.getValue().dispose();
      log.debug("Dispose subscription {}", entry.getKey().toString());
    }
    log.info("Dispose all subscriptions: {}", subscriptionsPool.size());
    onCleanup();
  }

  public abstract void onCleanup();
}
