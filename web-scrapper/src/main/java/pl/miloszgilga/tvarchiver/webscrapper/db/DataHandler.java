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

package pl.miloszgilga.tvarchiver.webscrapper.db;

import pl.miloszgilga.tvarchiver.webscrapper.soup.DayScheduleDetails;
import pl.miloszgilga.tvarchiver.webscrapper.soup.TvChannel;

import java.io.Closeable;
import java.net.InetSocketAddress;
import java.time.LocalDate;
import java.util.List;

public interface DataHandler extends Closeable {
	InetSocketAddress getDbHost();

	List<TvChannel> getTvChannels();

	void batchInsertTvChannels(List<TvChannel> tvChannels);

	void createChannelTableIfNotExists(String channelSlug);

	List<LocalDate> getAlreadySavedDates(String channelSlug, LocalDate start, LocalDate end);

	void batchInsertChannelData(String channelSlug, List<DayScheduleDetails> dayScheduleDetails, LocalDate scheduleDate);

	List<YearWithPersistedDto> getAlreadyPersistedPerYear(String channelSlug);

	Long getPersistedProgramsPerYear(String channelSlug);

	int deleteChannelDataByYear(String channelSlug, int year);

	Long getDatabaseSize();
}
