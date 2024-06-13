/*
 * Copyright (c) 2024 by Miłosz Gilga <https://miloszgilga.pl>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     <http://www.apache.org/licenses/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.miloszgilga.tvarchiver.webscrapper.state;

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
