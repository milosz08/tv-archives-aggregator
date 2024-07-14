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

package pl.miloszgilga.tvarchiver.dataserver.features.search;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import pl.miloszgilga.tvarchiver.dataserver.features.search.dto.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
	private final JdbcTemplate jdbcTemplate;
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public List<SelectRecordDto> getTvChannels() {
		final String sql = """
			SELECT DISTINCT c.slug AS id, c.name AS value FROM tv_channels AS c
			INNER JOIN tv_programs_data AS d ON c.id = d.channel_id
			""";
		return jdbcTemplate.query(sql, new DataClassRowMapper<>(SelectRecordDto.class));
	}

	@Override
	public List<SelectRecordDto> getProgramTypes() {
		final String sql = """
			SELECT DISTINCT
			REPLACE(LOWER(program_type), ' ', '-') AS id,
			CONCAT(program_type, ' (', COUNT(program_type), ')') AS value
			FROM tv_programs_data
			GROUP BY program_type ORDER BY COUNT(program_type) DESC
			""";
		return jdbcTemplate.query(sql, new DataClassRowMapper<>(SelectRecordDto.class));
	}

	@Override
	public SearchResultDto performSearch(SearchFilterReqDto reqDto, int page, int pageSize) {
		final String baseSql = """
			FROM tv_programs_data AS d
			INNER JOIN tv_channels AS c ON d.channel_id = c.id
			""";

		final List<String> filter = new ArrayList<>();
		final Map<String, Object> params = new HashMap<>();

		params.put("searchPhrase", "%" + reqDto.getSearchPhrase() + "%");
		params.put("limit", pageSize);
		params.put("offset", (page - 1) * pageSize);

		String textSearch = "WHERE LOWER(d.name) LIKE LOWER(:searchPhrase)";
		if (reqDto.isFullTextSearch()) {
			textSearch = "WHERE (LOWER(d.name) LIKE LOWER(:searchPhrase) OR LOWER(description) LIKE LOWER(:searchPhrase))";
		}
		filter.add(textSearch);

		if (!reqDto.isTvShowsActive()) {
			filter.add("(episode IS NULL AND season IS NULL)");
		}
		if (!reqDto.getSelectedTvChannels().isEmpty()) {
			filter.add("c.slug IN (:selectedTvChannels)");
			params.put("selectedTvChannels", reqDto.getSelectedTvChannels());
		}
		if (!reqDto.getSelectedProgramTypes().isEmpty()) {
			filter.add("program_type IN (:selectedProgramTypes)");
			params.put("selectedProgramTypes", reqDto.getSelectedProgramTypes());
		}
		if (reqDto.isTvShowsActive() && reqDto.getSeason() != null) {
			filter.add("(season = :season OR season IS NULL)");
			params.put("season", reqDto.getSeason());
		}
		if (reqDto.isTvShowsActive() && reqDto.getEpisode() != null) {
			filter.add("(episode = :episode OR episode IS NULL)");
			params.put("episode", reqDto.getEpisode());
		}
		if (reqDto.getStartDate() != null) {
			filter.add("CAST(CONCAT(schedule_date, ' ', hour_start, ':00') AS DATETIME) > :startDate");
			params.put("startDate", reqDto.getStartDate());
		}
		if (reqDto.getEndDate() != null) {
			filter.add("CAST(CONCAT(schedule_date, ' ', hour_start, ':00') AS DATETIME) < :endDate");
			params.put("endDate", reqDto.getEndDate());
		}
		final String filterSql = String.join(" AND ", filter);

		final String countSql = String.format("""
			SELECT COUNT(d.id) AS count,
			CEIL(COUNT(d.id) / :limit) AS total_pages
			%s %s
			""", baseSql, filterSql);

		final PaginationMetadataDto metadata = namedParameterJdbcTemplate.queryForObject(countSql, params,
			new DataClassRowMapper<>(PaginationMetadataDto.class));
		if (metadata == null) {
			throw new RuntimeException("Unable to find pagination metadata.");
		}

		final String sortDirection = reqDto.isSortNowToPrev() ? "DESC" : "ASC";
		final String searchSql = String.format("""
			SELECT d.name, description, program_type, episode, season, badge, hour_start, schedule_date,
			DAYNAME(schedule_date) AS weekday, c.name AS tv_channel_name %s %s
			ORDER BY schedule_date %s LIMIT :limit OFFSET :offset
			""", baseSql, filterSql, sortDirection);

		final List<SearchResultElement> elements = namedParameterJdbcTemplate
			.query(searchSql, params, new DataClassRowMapper<>(SearchResultElement.class));

		return new SearchResultDto(elements, page, metadata.totalPages(), metadata.count(), pageSize);
	}
}
