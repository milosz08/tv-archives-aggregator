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

package pl.miloszgilga.tvarchiver.dataserver.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppUtils {
	public static List<Integer> reduce2dList(int size, List<List<Integer>> list2d) {
		final Integer[] reducedArray = new Integer[size];
		Arrays.fill(reducedArray, 0);
		for (int j = 0; j < reducedArray.length; j++) {
			for (final List<Integer> restOfDatum : list2d) {
				reducedArray[j] += restOfDatum.get(j);
			}
		}
		return List.of(reducedArray);
	}
}
