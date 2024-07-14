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
import { FormEvent } from 'react';
import { Dayjs } from 'dayjs';
import { useAxios } from '@/api';
import { useSearchFilterContext } from '@/context/SearchFilterContext';
import {
  Box,
  Button,
  FormControlLabel,
  Grid,
  Switch,
  TextField,
} from '@mui/material';
import { DateTimePicker } from '@mui/x-date-pickers';
import { useQuery } from '@tanstack/react-query';
import MultiselectCheckbox from '../MultiselectCheckbox';

const SearchContentForm: React.FC = (): JSX.Element => {
  const { searchFilter, updateFilterProp, refetch, setPage } =
    useSearchFilterContext();
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

  const handleSubmitSearchForm = (e: FormEvent<HTMLFormElement>): void => {
    e.preventDefault();
    refetch();
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
              onChange={e =>
                updateFilterProp('fullTextSearch', e.target.checked)
              }
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
        <Grid container spacing={2}>
          <Grid item sm={12} md={6} lg={4}>
            <Box display="flex" alignItems="center" height="100%">
              <FormControlLabel
                control={
                  <Switch
                    checked={searchFilter.tvShowsActive}
                    onChange={e =>
                      updateFilterProp('tvShowsActive', e.target.checked)
                    }
                  />
                }
                label="Hide/show TV series (with seasons and episodes)"
              />
            </Box>
          </Grid>
          <Grid item sm={12} md={6} lg={4} width="100%">
            <DateTimePicker
              label="Start date"
              sx={{ width: '100%' }}
              onChange={startDate =>
                updateFilterProp('startDate', formatDate(startDate))
              }
            />
          </Grid>
          <Grid item sm={12} md={6} lg={4} width="100%">
            <DateTimePicker
              label="End date"
              sx={{ width: '100%' }}
              onChange={endDate =>
                updateFilterProp('endDate', formatDate(endDate))
              }
            />
          </Grid>
          <Grid item sm={12} md={6} lg={4}>
            <FormControlLabel
              control={
                <Switch
                  checked={searchFilter.sortNowToPrev}
                  onChange={e =>
                    updateFilterProp('sortNowToPrev', e.target.checked)
                  }
                />
              }
              label="Sort prev to now/now to prev (date)"
            />
          </Grid>
          <Grid item sm={6} lg={2} width="100%">
            <TextField
              value={searchFilter.season ?? ''}
              onChange={e => updateFilterProp('season', e.target.value)}
              disabled={!searchFilter.tvShowsActive}
              size="small"
              label="Season"
              type="number"
              sx={{ width: '100%' }}
            />
          </Grid>
          <Grid item sm={6} lg={2} width="100%">
            <TextField
              value={searchFilter.episode ?? ''}
              onChange={e => updateFilterProp('episode', e.target.value)}
              disabled={!searchFilter.tvShowsActive}
              size="small"
              label="Episode"
              type="number"
              sx={{ width: '100%' }}
            />
          </Grid>
          <Grid item sm={12} lg={4} width="100%">
            <MultiselectCheckbox
              id="search-weekdays"
              label="Weekdays"
              limitTags={3}
              elements={weekdays}
              onChange={selectedWeekdays =>
                updateFilterProp('selectedWeekdays', selectedWeekdays)
              }
              refetch={refetchWeekdays}
            />
          </Grid>
        </Grid>
        <Button type="submit" variant="contained" sx={{ width: '100%' }}>
          Search content
        </Button>
      </Box>
    </form>
  );
};

export default SearchContentForm;
