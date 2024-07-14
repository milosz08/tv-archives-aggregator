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
import { useState } from 'react';
import { useDebounceValue } from 'usehooks-ts';
import { useAxios } from '@/api';
import RefreshSectionHeader from '@/components/RefreshSectionHeader';
import TvChannelsList from '@/components/tv-channels/TvChannelsList';
import { Box, FormControlLabel, Switch, TextField } from '@mui/material';
import { useQuery } from '@tanstack/react-query';

const TvChannelsPage: React.FC = (): JSX.Element => {
  const [searchPhrase, setSearchPhrase] = useDebounceValue('', 500);
  const { api } = useAxios();

  const [showOnlyFetched, setShowOnlyFetched] = useState(true);

  const { data, isFetching, refetch } = useQuery({
    queryKey: ['tvChannels', searchPhrase, showOnlyFetched],
    queryFn: async () =>
      await api.fetchTvChannels(searchPhrase, showOnlyFetched),
  });

  return (
    <Box display="flex" flexDirection="column" rowGap={2}>
      <RefreshSectionHeader onRefresh={() => refetch()}>
        TV Channels
      </RefreshSectionHeader>
      <TextField
        size="small"
        value={searchPhrase}
        label="Enter channel name"
        onChange={e => setSearchPhrase(e.target.value)}
      />
      <FormControlLabel
        control={
          <Switch
            checked={showOnlyFetched}
            onChange={e => setShowOnlyFetched(e.target.checked)}
          />
        }
        label="Show only TV channels with some data"
      />
      <TvChannelsList data={data} isFetching={isFetching} />
    </Box>
  );
};

export default TvChannelsPage;
