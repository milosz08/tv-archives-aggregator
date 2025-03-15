import * as React from 'react';
import { FormEvent } from 'react';
import { Dayjs } from 'dayjs';
import { useAxios } from '@/api';
import { useSearchFilterContext } from '@/context/SearchFilterContext';
import { Box, Button, FormControlLabel, Grid2, Switch, TextField } from '@mui/material';
import { DateTimePicker } from '@mui/x-date-pickers';
import { useQuery } from '@tanstack/react-query';
import { MultiselectCheckbox } from '../MultiselectCheckbox';

const SearchContentForm: React.FC = (): React.ReactElement => {
  const { searchFilter, updateFilterProp, refetch, setPage } = useSearchFilterContext();
  const { api } = useAxios();

  const { data: tvChannels = [], refetch: refetchTvChannels } = useQuery({
    queryKey: ['searchTvChannels'],
    queryFn: async () => await api.fetchSearchTvChannels(),
    enabled: false,
  });

  const { data: programTypes = [], refetch: refetchProgramTypes } = useQuery({
    queryKey: ['searchProgramTypes'],
    queryFn: async () => await api.fetchSearchProgramTypes(),
    enabled: false,
  });

  const { data: weekdays = [], refetch: refetchWeekdays } = useQuery({
    queryKey: ['weekdays'],
    queryFn: async () => await api.fetchSearchWeekdays(),
    enabled: false,
  });

  const handleSubmitSearchForm = async (e: FormEvent<HTMLFormElement>): Promise<void> => {
    e.preventDefault();
    setPage(1);
    await refetch();
  };

  const formatDate = (date: Dayjs | null): string | null => {
    return date ? date.format('YYYY-MM-DD HH:mm:ss') : null;
  };

  return (
    <form onSubmit={handleSubmitSearchForm}>
      <Box display="flex" flexDirection="column" rowGap={2}>
        <TextField
          size="small"
          value={searchFilter.searchPhrase}
          label="Enter program title or description part"
          onChange={e => updateFilterProp('searchPhrase', e.target.value)}
        />
        <FormControlLabel
          control={
            <Switch
              checked={searchFilter.fullTextSearch}
              onChange={e => updateFilterProp('fullTextSearch', e.target.checked)}
            />
          }
          label="Disable/enable full text search"
        />
        <MultiselectCheckbox
          id="search-tv-channels"
          label="Tv channels"
          elements={tvChannels}
          onChange={selectedTvChannels =>
            updateFilterProp('selectedTvChannels', selectedTvChannels)
          }
          refetch={refetchTvChannels}
        />
        <MultiselectCheckbox
          id="search-program-type"
          label="Program types"
          elements={programTypes}
          onChange={selectedProgramTypes =>
            updateFilterProp('selectedProgramTypes', selectedProgramTypes)
          }
          refetch={refetchProgramTypes}
        />
        <Grid2 container spacing={2}>
          <Grid2 size={{ sm: 12, md: 6, lg: 4 }}>
            <Box display="flex" alignItems="center" height="100%">
              <FormControlLabel
                control={
                  <Switch
                    checked={searchFilter.tvShowsActive}
                    onChange={e => updateFilterProp('tvShowsActive', e.target.checked)}
                  />
                }
                label="Hide/show TV series (with seasons and episodes)"
              />
            </Box>
          </Grid2>
          <Grid2 size={{ sm: 12, md: 6, lg: 4 }} width="100%">
            <DateTimePicker
              label="Start date"
              sx={{ width: '100%' }}
              onChange={startDate => updateFilterProp('startDate', formatDate(startDate))}
            />
          </Grid2>
          <Grid2 size={{ sm: 12, md: 6, lg: 4 }} width="100%">
            <DateTimePicker
              label="End date"
              sx={{ width: '100%' }}
              onChange={endDate => updateFilterProp('endDate', formatDate(endDate))}
            />
          </Grid2>
          <Grid2 size={{ sm: 12, md: 6, lg: 4 }}>
            <FormControlLabel
              control={
                <Switch
                  checked={searchFilter.sortNowToPrev}
                  onChange={e => updateFilterProp('sortNowToPrev', e.target.checked)}
                />
              }
              label="Sort prev to now/now to prev (date)"
            />
          </Grid2>
          <Grid2 size={{ sm: 6, lg: 2 }} width="100%">
            <TextField
              value={searchFilter.season ?? ''}
              onChange={e => updateFilterProp('season', e.target.value)}
              disabled={!searchFilter.tvShowsActive}
              size="small"
              label="Season"
              type="number"
              sx={{ width: '100%' }}
            />
          </Grid2>
          <Grid2 size={{ sm: 6, lg: 2 }} width="100%">
            <TextField
              value={searchFilter.episode ?? ''}
              onChange={e => updateFilterProp('episode', e.target.value)}
              disabled={!searchFilter.tvShowsActive}
              size="small"
              label="Episode"
              type="number"
              sx={{ width: '100%' }}
            />
          </Grid2>
          <Grid2 size={{ sm: 12, lg: 4 }} width="100%">
            <MultiselectCheckbox
              id="search-weekdays"
              label="Weekdays"
              limitTags={3}
              elements={weekdays}
              onChange={selectedWeekdays => updateFilterProp('selectedWeekdays', selectedWeekdays)}
              refetch={refetchWeekdays}
            />
          </Grid2>
        </Grid2>
        <Button type="submit" variant="contained" sx={{ width: '100%' }}>
          Search content
        </Button>
      </Box>
    </form>
  );
};

export { SearchContentForm };
