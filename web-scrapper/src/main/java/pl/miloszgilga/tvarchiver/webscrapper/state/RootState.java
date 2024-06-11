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
import lombok.Getter;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.miloszgilga.tvarchiver.webscrapper.db.DataSource;
import pl.miloszgilga.tvarchiver.webscrapper.soup.TvChannel;
import pl.miloszgilga.tvarchiver.webscrapper.soup.TvChannelDetails;

import java.util.ArrayList;
import java.util.List;

@Setter
public class RootState extends AbstractDisposableProvider {
	private final BehaviorSubject<List<TvChannel>> tvChannels$;
	private final BehaviorSubject<TvChannel> selectedChannel$;
	private final BehaviorSubject<AppState> appState$;
	private final BehaviorSubject<Integer> randomness$;
	private final BehaviorSubject<Long> totalFetchedCount$;
	private final BehaviorSubject<TvChannelDetails> channelDetails$;
	private final BehaviorSubject<Integer> selectedYear$;

	private Dotenv dotenv;
	@Getter
	private DataSource dataSource;

	public RootState() {
		dotenv = Dotenv.load();
		tvChannels$ = BehaviorSubject.createDefault(new ArrayList<>());
		selectedChannel$ = BehaviorSubject.createDefault(new TvChannel());
		appState$ = BehaviorSubject.createDefault(AppState.IDLE);
		randomness$ = BehaviorSubject.createDefault(1);
		totalFetchedCount$ = BehaviorSubject.createDefault(0L);
		channelDetails$ = BehaviorSubject.create();
		selectedYear$ = BehaviorSubject.createDefault(-1);
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

	public void updateRandomness(int randomness) {
		this.randomness$.onNext(randomness);
	}

	public void updateTotalFetchedCount(long totalFetchedCount) {
		this.totalFetchedCount$.onNext(totalFetchedCount);
	}

	public void updateChannelDetails(TvChannelDetails details) {
		this.channelDetails$.onNext(details);
	}

	public void updateSelectedYear(int year) {
		this.selectedYear$.onNext(year);
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

	public Observable<Long> getTotalFetchedCount$() {
		return this.totalFetchedCount$.hide();
	}

	public Observable<TvChannelDetails> getChannelDetails$() {
		return this.channelDetails$.hide();
	}

	public Observable<Integer> getSelectedYear$() {
		return this.selectedYear$.hide();
	}

	public List<TvChannel> getTvChannels() {
		return this.tvChannels$.getValue();
	}

	public TvChannel getSelectedChannel() {
		return this.selectedChannel$.getValue();
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

	public Integer getSelectedYear() {
		return this.selectedYear$.getValue();
	}

	public TvChannelDetails getTvChannelDetails() {
		return this.channelDetails$.getValue();
	}

	public Long getTotalFetchedCount() {
		return this.totalFetchedCount$.getValue();
	}

	@Override
	public void onCleanup() {
		if (dataSource != null) {
			dataSource.closeConnection();
		}
	}
}
