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

package pl.miloszgilga.tvarchiver.dataserver.network.tvchannel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import pl.miloszgilga.tvarchiver.dataserver.network.tvchannel.dto.TvChannelResponseDto;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TvChannelServiceImpl implements TvChannelService {
	private final JdbcTemplate jdbcTemplate;

	@Override
	public Map<Character, List<TvChannelResponseDto>> getTvChannelsBySearch(String phrase) {
		final String sql = "SELECT name, slug FROM tv_channels WHERE LOWER(name) LIKE LOWER(?)";
		final List<TvChannelResponseDto> tvChannels = jdbcTemplate
			.query(sql, new DataClassRowMapper<>(TvChannelResponseDto.class), "%" + phrase + "%");

		final TreeMap<Character, List<TvChannelResponseDto>> mappedByFirstLetter = new TreeMap<>(tvChannels.stream()
			.collect(Collectors.groupingBy(s -> s.name().toLowerCase().charAt(0))));

		final List<TvChannelResponseDto> nonLettersNames = new ArrayList<>();
		final List<Character> elementsToRemoved = new ArrayList<>();

		// filter for non-letter characters
		for (final Map.Entry<Character, List<TvChannelResponseDto>> entry : mappedByFirstLetter.entrySet()) {
			if (!Character.isLetter(entry.getKey())) {
				nonLettersNames.addAll(entry.getValue());
				elementsToRemoved.add(entry.getKey());
			}
		}
		// removing moved letters
		for (final Character letter : elementsToRemoved) {
			mappedByFirstLetter.remove(letter);
		}
		mappedByFirstLetter.put('#', nonLettersNames); // add saved non-letters tv channels

		// sort all channels in selected latter category
		for (final List<TvChannelResponseDto> channels : mappedByFirstLetter.values()) {
			Collections.sort(channels);
		}
		log.info("Found: {} records with phrase: {}. Letters: {}", tvChannels.size(), phrase,
			mappedByFirstLetter.size());
		return mappedByFirstLetter;
	}
}
