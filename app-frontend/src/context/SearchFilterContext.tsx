import * as React from 'react';
import { Dispatch, SetStateAction, createContext, useContext, useState } from 'react';
import { useAxios } from '@/api';
import { SearchFilter, SearchResultResDto } from '@/api/types/search-content';
import { QueryObserverResult, useQuery } from '@tanstack/react-query';

type SearchFilterContextType = {
  searchPhrase: string;
  setSearchPhrase: Dispatch<SetStateAction<string>>;
  searchResult?: SearchResultResDto;
  searchFilter: SearchFilter;
  isFetching: boolean;
  page: number;
  pageSize: number;
  updateFilterProp: (name: keyof SearchFilter, value: unknown) => void;
  setPage: Dispatch<SetStateAction<number>>;
  setPageSize: Dispatch<SetStateAction<number>>;
  refetch: () => Promise<QueryObserverResult<SearchResultResDto, Error>>;
};

const SearchFilterContext = createContext<SearchFilterContextType>({} as SearchFilterContextType);

type Props = {
  children: React.ReactNode;
};

const SearchFilterProvider: React.FC<Props> = ({ children }): React.ReactElement => {
  const { api } = useAxios();

  const [searchPhrase, setSearchPhrase] = useState('');

  const [searchFilter, setSearchFilter] = useState<SearchFilter>({
    searchPhrase: '',
    fullTextSearch: false,
    selectedTvChannels: [],
    selectedProgramTypes: [],
    selectedWeekdays: [],
    tvShowsActive: true,
    startDate: null,
    endDate: null,
    season: null,
    episode: null,
    sortNowToPrev: true,
  });

  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(20);

  const updateFilterProp = (name: keyof SearchFilter, value: unknown): void => {
    setSearchFilter(prevState => ({ ...prevState, [name]: value }));
  };

  const {
    data: searchResult,
    isFetching,
    refetch,
  } = useQuery({
    queryKey: ['searchPrograms', page, pageSize],
    queryFn: async () => await api.fetchSearchResults(searchFilter, page, pageSize),
    enabled: false,
  });

  return (
    <SearchFilterContext.Provider
      value={{
        searchPhrase,
        setSearchPhrase,
        searchResult,
        searchFilter,
        isFetching,
        page,
        pageSize,
        updateFilterProp,
        setPage,
        setPageSize,
        refetch,
      }}>
      {children}
    </SearchFilterContext.Provider>
  );
};

const useSearchFilterContext = () => useContext(SearchFilterContext);

export { SearchFilterProvider, useSearchFilterContext };
