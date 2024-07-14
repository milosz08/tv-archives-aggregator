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
import ResultElementsList from '@/components/search-content/ResultElementsList';
import SearchContentForm from '@/components/search-content/SearchContentForm';
import SearchFilterProvider from '@/context/SearchFilterContext';
import { Box } from '@mui/material';

const SearchContent: React.FC = (): JSX.Element => (
  <SearchFilterProvider>
    <Box display="flex" flexDirection="column" rowGap={4}>
      <SearchContentForm />
      <ResultElementsList />
    </Box>
  </SearchFilterProvider>
);

export default SearchContent;
