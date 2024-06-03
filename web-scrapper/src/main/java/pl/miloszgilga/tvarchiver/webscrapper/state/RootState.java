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

import io.github.cdimascio.dotenv.Dotenv;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.miloszgilga.tvarchiver.webscrapper.db.DataSource;
import pl.miloszgilga.tvarchiver.webscrapper.soup.TvChannel;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

@Setter
public class RootState extends AbstractDisposableProvider {
	private final BehaviorSubject<List<TvChannel>> tvChannels$;
	private final BehaviorSubject<TvChannel> selectedChannel$;
	private final BehaviorSubject<AppState> appState$;
	private final BehaviorSubject<InetSocketAddress> dbHost$;
	private final BehaviorSubject<Long> dbSize$;
	private final BehaviorSubject<Integer> randomness$;
	private final BehaviorSubject<Integer> progressBar$;

	private Dotenv dotenv;
	private DataSource dataSource;

	public RootState() {
		dotenv = Dotenv.load();
		tvChannels$ = BehaviorSubject.createDefault(new ArrayList<>());
		selectedChannel$ = BehaviorSubject.createDefault(new TvChannel());
		appState$ = BehaviorSubject.createDefault(AppState.IDLE);
		dbHost$ = BehaviorSubject.createDefault(new InetSocketAddress("localhost", 3306));
		dbSize$ = BehaviorSubject.createDefault(0L);
		randomness$ = BehaviorSubject.createDefault(0);
		progressBar$ = BehaviorSubject.createDefault(0);
	}

	public void updateTvChannels(List<TvChannel> channels) {
		this.tvChannels$.onNext(channels);
	}

	public void updateSelectedChannel(TvChannel channel) {
		this.selectedChannel$.onNext(channel);
	}

	public void updateAppState(AppState state) {
		this.appState$.onNext(state);
	}

	public void updateDbHost(InetSocketAddress host) {
		this.dbHost$.onNext(host);
	}

	public void updateDbSize(long size) {
		this.dbSize$.onNext(size);
	}

	public void updateRandomness(int randomness) {
		this.randomness$.onNext(randomness);
	}

	public void updateProgressBar(int percentage) {
		this.progressBar$.onNext(percentage);
	}

	public Observable<List<TvChannel>> getTvChannels$() {
		return this.tvChannels$.hide();
	}

	public Observable<TvChannel> getSelectedChannel$() {
		return this.selectedChannel$.hide();
	}

	public Observable<AppState> getAppState$() {
		return this.appState$.hide();
	}

	public Observable<InetSocketAddress> getDbHost$() {
		return this.dbHost$.hide();
	}

	public Observable<Long> getDbSize$() {
		return this.dbSize$.hide();
	}

	public Observable<Integer> getProgressBar$() {
		return this.progressBar$.hide();
	}

	public List<TvChannel> getTvChannels() {
		return this.tvChannels$.getValue();
	}

	public Integer getRandomness() {
		return this.randomness$.getValue();
	}

	public JdbcTemplate getJdbcTemplate() {
		return dataSource.getJdbcTemplate();
	}

	public String getEnvValue(EnvKey key) {
		return dotenv.get(key.name(), key.getDefaultValue());
	}

	@Override
	public void onCleanup() {
		if (dataSource != null) {
			dataSource.closeConnection();
		}
	}
}
