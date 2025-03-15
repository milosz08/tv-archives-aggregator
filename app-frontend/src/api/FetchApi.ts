import { AxiosInstance } from 'axios';
import { ProgramDayDetails } from './types/archive-day-program';
import { SearchFilter, SearchResultResDto, SelectRecord } from './types/search-content';
import { TvChannelDetails, TvChannelsAlphabet } from './types/tv-channel';
import { MonthlyProgramsChartDto } from './types/tv-channel-chart';
import { CalendarMonth } from './types/tv-channel-years';
import { DatabaseCapacityDetails } from './types/util';

const fetchApi = (axios: AxiosInstance) => ({
  fetchTvChannels: async (channelName: string, onlyWithSomeData: boolean) => {
    const { data } = await axios.get<TvChannelsAlphabet>('/v1/tv-channel/all/search', {
      params: { phrase: channelName, onlyWithSomeData },
    });
    return data;
  },
  fetchTvChannelDetails: async (channelSlug: string | undefined) => {
    const { data } = await axios.get<TvChannelDetails>(`/v1/tv-channel/details/${channelSlug}`);
    return data;
  },
  fetchDatabaseCapacityDetails: async (channelSlug: string | null) => {
    const { data } = await axios.get<DatabaseCapacityDetails>('/v1/util/db/capacity', {
      params: { slug: channelSlug ?? '' },
    });
    return data;
  },
  fetchMonthlyChannelPrograms: async (channelSlug: string | undefined, year: number) => {
    const { data } = await axios.get<MonthlyProgramsChartDto>(
      `/v1/tv-channel/${channelSlug}/chart/year/${year}/program/types`
    );
    return data;
  },
  fetchTvChannelYearMonths: async (channelSlug: string | undefined, year: number) => {
    const { data } = await axios.get<CalendarMonth[]>(
      `/v1/calendar/struct/channel/${channelSlug}/year/${year}`
    );
    return data;
  },
  fetchPersistedChannelYears: async (channelSlug: string | undefined) => {
    const { data } = await axios.get<number[]>(`/v1/calendar/years/channel/${channelSlug}`);
    return data;
  },
  fetchArchiveProgramPerDay: async (channelSlug: string | undefined, date: string | undefined) => {
    const { data } = await axios.get<ProgramDayDetails>(
      `/v1/program/all/channel/${channelSlug}/date/${date}`
    );
    return data;
  },
  fetchSearchTvChannels: async () => {
    const { data } = await axios.get<SelectRecord[]>('/v1/search/channels');
    return data;
  },
  fetchSearchProgramTypes: async () => {
    const { data } = await axios.get<SelectRecord[]>('/v1/search/program/types');
    return data;
  },
  fetchSearchWeekdays: async () => {
    const { data } = await axios.get<SelectRecord[]>('/v1/search/weekdays');
    return data;
  },
  fetchSearchResults: async (reqDto: SearchFilter, page: number, pageSize: number) => {
    const { data } = await axios.post<SearchResultResDto>(
      '/v1/search',
      {
        ...reqDto,
        season: reqDto.season ? Number(reqDto.season) : null,
        episode: reqDto.episode ? Number(reqDto.episode) : null,
        selectedTvChannels: reqDto.selectedTvChannels.map(({ id }) => id),
        selectedProgramTypes: reqDto.selectedProgramTypes.map(({ id }) => id.replace('-', ' ')),
        selectedWeekdays: reqDto.selectedWeekdays.map(({ id }) => Number(id)),
      },
      { params: { page, pageSize } }
    );
    return data;
  },
});

export { fetchApi };
