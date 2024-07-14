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
import { AxiosInstance } from 'axios';
import { ProgramDayDetails } from './types/archive-day-program';
import {
  SearchFilter,
  SearchResultResDto,
  SelectRecord,
} from './types/search-content';
import {
  TvChannelDetails,
  TvChannelPersistenceDetails,
  TvChannelsAlphabet,
} from './types/tv-channel';
import { MonthlyProgramsChartDto } from './types/tv-channel-chart';
import { CalendarMonth } from './types/tv-channel-years';

const fetchApi = (axios: AxiosInstance) => ({
  fetchTvChannels: async (channelName: string, onlyWithSomeData: boolean) => {
    const { data } = await axios.get<TvChannelsAlphabet>(
      '/api/v1/tv-channel/all/search',
      { params: { phrase: channelName, onlyWithSomeData } }
    );
    return data;
  },
  fetchTvChannelDetails: async (channelSlug: string | undefined) => {
    const { data } = await axios.get<TvChannelDetails>(
      `/api/v1/tv-channel/details/${channelSlug}`
    );
    return data;
  },
  fetchTvChannelPersistenceDetails: async (channelSlug: string | undefined) => {
    const { data } = await axios.get<TvChannelPersistenceDetails>(
      `/api/v1/tv-channel/details/${channelSlug}/persistence`
    );
    return data;
  },
  fetchMonthlyChannelPrograms: async (
    channelSlug: string | undefined,
    year: number
  ) => {
    const { data } = await axios.get<MonthlyProgramsChartDto>(
      `/api/v1/tv-channel/${channelSlug}/chart/year/${year}/program/types`
    );
    return data;
  },
  fetchTvChannelYearMonths: async (
    channelSlug: string | undefined,
    year: number
  ) => {
    const { data } = await axios.get<CalendarMonth[]>(
      `/api/v1/calendar/struct/channel/${channelSlug}/year/${year}`
    );
    return data;
  },
  fetchPersistedChannelYears: async (channelSlug: string | undefined) => {
    const { data } = await axios.get<number[]>(
      `/api/v1/calendar/years/channel/${channelSlug}`
    );
    return data;
  },
  fetchArchiveProgramPerDay: async (
    channelSlug: string | undefined,
    date: string | undefined
  ) => {
    const { data } = await axios.get<ProgramDayDetails>(
      `/api/v1/program/all/channel/${channelSlug}/date/${date}`
    );
    return data;
  },
  fetchSearchTvChannels: async () => {
    const { data } = await axios.get<SelectRecord[]>('/api/v1/search/channels');
    return data;
  },
  fetchSearchProgramTypes: async () => {
    const { data } = await axios.get<SelectRecord[]>(
      '/api/v1/search/program/types'
    );
    return data;
  },
  fetchSearchResults: async (
    reqDto: SearchFilter,
    page: number,
    pageSize: number
  ) => {
    const { data } = await axios.post<SearchResultResDto>(
      '/api/v1/search',
      {
        ...reqDto,
        season: reqDto.season ? Number(reqDto.season) : null,
        episode: reqDto.episode ? Number(reqDto.episode) : null,
        selectedTvChannels: reqDto.selectedTvChannels.map(({ id }) => id),
        selectedProgramTypes: reqDto.selectedProgramTypes.map(({ id }) =>
          id.replace('-', ' ')
        ),
      },
      { params: { page, pageSize } }
    );
    return data;
  },
});

export default fetchApi;
