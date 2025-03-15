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
