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

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.PreparedBatch;
import org.jdbi.v3.core.statement.Query;
import pl.miloszgilga.tvarchiver.webscrapper.soup.DayScheduleDetails;
import pl.miloszgilga.tvarchiver.webscrapper.soup.TvChannel;

import java.net.InetSocketAddress;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class JdbiDataHandler implements DataHandler {
	private final DataSource dataSource;
	private final Jdbi jdbi;

	public JdbiDataHandler(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbi = dataSource.getJdbi();
	}

	@Override
	public InetSocketAddress getDbHost() {
		return dataSource.getDbHost();
	}

	@Override
	public List<TvChannel> getTvChannels() {
		final String sql = "SELECT * FROM tv_channels";
		return jdbi.withHandle(handle -> handle.createQuery(sql)
			.map((rs, rowNum) -> new TvChannel(rs.getLong("id"), rs.getString("name"), rs.getString("slug")))
			.list()
		);
	}

	@Override
	public void batchInsertTvChannels(List<TvChannel> tvChannels) {
		final String sql = "INSERT INTO tv_channels (name,slug) VALUES (?,?)";
		jdbi.useHandle(handle -> {
			final PreparedBatch batch = handle.prepareBatch(sql);
			for (final TvChannel tvChannel : tvChannels) {
				batch
					.bind(0, tvChannel.name())
					.bind(1, tvChannel.slug())
					.add();
			}
			batch.execute();
		});
	}

	@Override
	public void createChannelTableIfNotExists(String channelSlug) {
		final String sql = String.format("""
			CREATE TABLE IF NOT EXISTS `%s` (
				id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
				name VARCHAR(255) NOT NULL,
			 	description VARCHAR(3000),
			 	program_type VARCHAR(255) NOT NULL,
			 	season INT UNSIGNED DEFAULT NULL,
			 	episode INT UNSIGNED DEFAULT NULL,
			 	badge VARCHAR(255) DEFAULT NULL,
			 	hour_start VARCHAR(5) NOT NULL,
			 	schedule_date DATE NOT NULL,
			 	weekday INT UNSIGNED NOT NULL,
			 	PRIMARY KEY (id)
			)
			ENGINE=InnoDB COLLATE=utf16_polish_ci;
			""", transformChannelSlug(channelSlug));
		jdbi.useHandle(handle -> handle.execute(sql));
	}

	@Override
	public List<LocalDate> getAlreadySavedDates(String channelSlug, LocalDate start, LocalDate end) {
		final String sql = String.format("""
			SELECT schedule_date FROM `%s`
			WHERE schedule_date BETWEEN :start AND :end
			""", transformChannelSlug(channelSlug));
		return jdbi.withHandle(handle -> {
			final Query query = handle.createQuery(sql);
			query.bind("start", start);
			query.bind("end", end);
			return query
				.map((rs, rowNum) -> rs.getDate("schedule_date").toLocalDate())
				.list();
		});
	}

	@Override
	public void batchInsertChannelData(
		String channelSlug,
		List<DayScheduleDetails> dayScheduleDetails,
		LocalDate scheduleDate
	) {
		final String sql = String.format("""
			INSERT INTO `%s`(
				name,
				description,
				program_type,
				season, episode,
				badge,
				hour_start,
				schedule_date,
			  weekday
			) VALUES (?,?,?,?,?,?,?,?,?)
			""", transformChannelSlug(channelSlug));
		jdbi.useHandle(handle -> {
			final PreparedBatch batch = handle.prepareBatch(sql);
			for (final DayScheduleDetails detail : dayScheduleDetails) {
				batch
					.bind(0, detail.name())
					.bind(1, detail.description())
					.bind(2, detail.programType())
					.bind(3, detail.season())
					.bind(4, detail.episode())
					.bind(5, detail.badge())
					.bind(6, detail.hourStart())
					.bind(7, Date.valueOf(scheduleDate))
					.bind(8, scheduleDate.getDayOfWeek().getValue())
					.add();
			}
			batch.execute();
		});
	}

	@Override
	public List<YearWithPersistedDto> getAlreadyPersistedPerYear(String channelSlug) {
		final String sql = String.format("""
			SELECT YEAR(schedule_date) as year, COUNT(DISTINCT DATE(schedule_date)) AS count
			FROM `%s`
			GROUP BY YEAR(schedule_date)
			""", transformChannelSlug(channelSlug));
		return jdbi.withHandle(handle -> handle.createQuery(sql)
			.map((rs, rowNum) -> new YearWithPersistedDto(rs.getInt("year"), rs.getInt("count")))
			.list());
	}

	@Override
	public Long getPersistedProgramsPerYear(String channelSlug) {
		final String sql = String
			.format("SELECT COUNT(DISTINCT DATE(schedule_date)) FROM `%s`", transformChannelSlug(channelSlug));
		return jdbi.withHandle(handle -> handle
			.createQuery(sql)
			.mapTo(Long.class)
			.findOne()
			.orElse(0L)
		);
	}

	@Override
	public int deleteChannelDataByYear(String channelSlug, int year) {
		final String sql = String
			.format("DELETE FROM `%s` WHERE YEAR(schedule_date) = :year", transformChannelSlug(channelSlug));
		return jdbi.withHandle(handle -> handle
			.createUpdate(sql)
			.bind("year", year)
			.execute()
		);
	}

	@Override
	public Long getDatabaseSize() {
		final String sql = """
			SELECT SUM(data_length + index_length) FROM information_schema.TABLES
			WHERE table_schema = :schema
			""";
		return jdbi.withHandle(handle -> {
			final Query query = handle.createQuery(sql);
			query.bind("schema", dataSource.getDbName());
			return query.mapTo(Long.class).findOne().orElse(null);
		});
	}

	@Override
	public void close() {
		dataSource.closeConnection();
	}

	private String transformChannelSlug(String channelSlug) {
		return "channel_" + channelSlug.replaceAll("-", "_");
	}
}
