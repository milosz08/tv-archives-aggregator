import * as React from 'react';
import { ResultElementsList, SearchContentForm } from '@/components/search-content';
import { SearchFilterProvider } from '@/context/SearchFilterContext';
import { Box } from '@mui/material';

const SearchContent: React.FC = (): React.ReactElement => (
  <SearchFilterProvider>
    <Box display="flex" flexDirection="column" rowGap={4}>
      <SearchContentForm />
      <ResultElementsList />
    </Box>
  </SearchFilterProvider>
);

export default SearchContent;
