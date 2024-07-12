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

package pl.miloszgilga.tvarchiver.dataserver.network.tvchannel.util;

import org.junit.jupiter.api.Test;
import pl.miloszgilga.tvarchiver.dataserver.util.AppUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class AppUtilsTest {

	@Test
	public void testReduce2dList() {
		//given
		final List<List<Integer>> input = List.of(
			List.of(10, 20, 30),
			List.of(20, 10, 15)
		);

		//when
		final List<Integer> expected = List.of(30, 30, 45);

		//then
		final List<Integer> result = AppUtils.reduce2dList(expected.size(), input);
		assertArrayEquals(expected.toArray(), result.toArray());
	}
}
