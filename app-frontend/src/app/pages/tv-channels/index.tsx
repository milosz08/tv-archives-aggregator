import * as React from 'react';
import { FormEvent, useState } from 'react';
import { useAxios } from '@/api';
import { DatabaseCapacityDetails, RefreshSectionHeader } from '@/components';
import { TvChannelsList } from '@/components/tv-channels';
import SearchIcon from '@mui/icons-material/Search';
import { Box, Button, FormControlLabel, Switch, TextField } from '@mui/material';
import { useQuery } from '@tanstack/react-query';

const TvChannelsPage: React.FC = (): React.ReactElement => {
  const [searchPhrase, setSearchPhrase] = useState('');
  const { api } = useAxios();

  const [showOnlyFetched, setShowOnlyFetched] = useState(true);

  const { data, isFetching, refetch } = useQuery({
    queryKey: ['tvChannels', showOnlyFetched],
    queryFn: async () => await api.fetchTvChannels(searchPhrase, showOnlyFetched),
  });

  const handleSubmitSearch = async (e: FormEvent<HTMLFormElement>): Promise<void> => {
    e.preventDefault();
    await refetch();
  };

  return (
    <Box display="flex" flexDirection="column" rowGap={2}>
      <RefreshSectionHeader onRefresh={() => refetch()}>TV Channels</RefreshSectionHeader>
      <form onSubmit={handleSubmitSearch}>
        <Box display="flex" columnGap={2}>
          <TextField
            size="small"
            fullWidth
            value={searchPhrase}
            label="Enter channel name"
            onChange={e => setSearchPhrase(e.target.value)}
          />
          <Button type="submit" variant="contained">
            <SearchIcon />
          </Button>
        </Box>
      </form>
      <FormControlLabel
        control={
          <Switch checked={showOnlyFetched} onChange={e => setShowOnlyFetched(e.target.checked)} />
        }
        label="Show only TV channels with some data"
      />
      <DatabaseCapacityDetails
        header="Database capacity"
        queryKey={['databaseCapacityGlobalDetails']}
      />
      <TvChannelsList data={data} isFetching={isFetching} />
    </Box>
  );
};

export default TvChannelsPage;
