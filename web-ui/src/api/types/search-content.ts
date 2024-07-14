/*
 * Copyright (c) 2024 by Miłosz Gilga <https://miloszgilga.pl>
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

export type SearchRecord = {
  name: string;
  description?: string;
  programType: string;
  episode?: number;
  season?: number;
  badge?: string;
  hourStart: string;
  scheduleDate: string;
  weekday: string;
  tvChannelName: string;
};

export type SearchResultResDto = {
  elements: SearchRecord[];
  viewTvShowColumn: boolean;
  page: number;
  totalPages: number;
  totalElements: number;
  perPage: number;
};

export type SearchTvChannelRecord = {
  name: string;
  slug: string;
};

export type SelectRecord = {
  id: string;
  value: string;
};

export type SearchFilter = {
  searchPhrase: string;
  fullTextSearch: boolean;
  selectedTvChannels: SelectRecord[];
  selectedProgramTypes: SelectRecord[];
  selectedWeekdays: SelectRecord[];
  tvShowsActive: boolean;
  startDate: string | null;
  endDate: string | null;
  season: number | null;
  episode: number | null;
  sortNowToPrev: boolean;
};
