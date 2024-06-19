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
import { useEffect } from 'react';
import { useSnackbar } from 'notistack';
import { useDebounceValue } from 'usehooks-ts';
import { fetchTvChannels } from '@/api';
import { Alert, Box, CircularProgress, TextField } from '@mui/material';
import { useQuery } from '@tanstack/react-query';
import TvChannelsWithLetter from './components/TvChannelsWithLetter';

const TvChannelsPage: React.FC = (): JSX.Element => {
  const [searchPhrase, setSearchPhrase] = useDebounceValue('', 500);
  const { enqueueSnackbar } = useSnackbar();

  const { data, isError, isFetching } = useQuery({
    queryKey: ['tvChannels', searchPhrase],
    queryFn: async () => await fetchTvChannels(searchPhrase),
  });

  useEffect(() => {
    if (isError) {
      enqueueSnackbar('Unable to fetch data!', { variant: 'error' });
    }
  }, [isError]);

  return (
    <Box display="flex" flexDirection="column" rowGap={2}>
      <TextField
        label="Enter channel name"
        variant="standard"
        onChange={e => setSearchPhrase(e.target.value)}
      />
      {isFetching && (
        <Box display="flex" justifyContent="center" marginTop={4}>
          <CircularProgress />
        </Box>
      )}
      {data && Object.keys(data).length === 0 && (
        <Alert severity="warning">Not found any channels.</Alert>
      )}
      {data && !isFetching && (
        <Box display="flex" flexDirection="column" rowGap={2} marginTop={5}>
          {Object.keys(data).map(key => (
            <TvChannelsWithLetter
              key={key}
              letter={key}
              tvChannels={data[key]}
            />
          ))}
        </Box>
      )}
    </Box>
  );
};

export default TvChannelsPage;
