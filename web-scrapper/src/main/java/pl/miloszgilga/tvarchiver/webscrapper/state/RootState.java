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
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.miloszgilga.tvarchiver.webscrapper.db.DataSource;
import pl.miloszgilga.tvarchiver.webscrapper.soup.TvChannel;

import java.util.ArrayList;
import java.util.List;

@Setter
public class RootState extends AbstractDisposableProvider {
	private final BehaviorSubject<List<TvChannel>> tvChannels$;
	private final BehaviorSubject<TvChannel> selectedChannel$;

	private DataSource dataSource;

	public RootState() {
		tvChannels$ = BehaviorSubject.createDefault(new ArrayList<>());
		selectedChannel$ = BehaviorSubject.createDefault(new TvChannel());
	}

	public void updateTvChannels(List<TvChannel> channels) {
		this.tvChannels$.onNext(channels);
	}

	public void updateSelectedChannel(TvChannel channel) {
		this.selectedChannel$.onNext(channel);
	}

	public Observable<List<TvChannel>> getTvChannels$() {
		return this.tvChannels$.hide();
	}

	public Observable<TvChannel> getSelectedChannel$() {
		return this.selectedChannel$.hide();
	}

	public List<TvChannel> getTvChannels() {
		return this.tvChannels$.getValue();
	}

	public JdbcTemplate getJdbcTemplate() {
		return dataSource.getJdbcTemplate();
	}

	@Override
	public void onCleanup() {
		if (dataSource != null) {
			dataSource.closeConnection();
		}
	}
}
