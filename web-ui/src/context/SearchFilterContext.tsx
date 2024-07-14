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
import {
  Dispatch,
  SetStateAction,
  createContext,
  useContext,
  useState,
} from 'react';
import { useAxios } from '@/api';
import { SearchFilter, SearchResultResDto } from '@/api/types/search-content';
import { QueryObserverResult, useQuery } from '@tanstack/react-query';

type SearchFilterContextType = {
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

const SearchFilterContext = createContext<SearchFilterContextType>(
  {} as SearchFilterContextType
);

type Props = {
  children: React.ReactNode;
};

const SearchFilterProvider: React.FC<Props> = ({ children }): JSX.Element => {
  const { api } = useAxios();

  const [searchFilter, setSearchFilter] = useState<SearchFilter>({
    searchPhrase: '',
    fullTextSearch: false,
    selectedTvChannels: [],
    selectedProgramTypes: [],
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
    queryFn: async () =>
      await api.fetchSearchResults(searchFilter, page, pageSize),
  });

  return (
    <SearchFilterContext.Provider
      value={{
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

export const useSearchFilterContext = () => useContext(SearchFilterContext);

export default SearchFilterProvider;
