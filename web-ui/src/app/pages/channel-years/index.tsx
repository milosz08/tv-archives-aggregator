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
import { useEffect, useState } from 'react';
import { useSnackbar } from 'notistack';
import { useNavigate, useParams } from 'react-router';
import { Link, useSearchParams } from 'react-router-dom';
import {
  fetchPersistedChannelYears,
  fetchTvChannelYearMonths,
} from '@/api/fetch';
import EmptyBlocks from '@/components/channel-years/EmptyBlocks';
import RefreshIcon from '@mui/icons-material/Refresh';
import {
  Alert,
  Box,
  Button,
  CircularProgress,
  FormControl,
  Grid,
  InputLabel,
  MenuItem,
  Paper,
  Select,
  Typography,
} from '@mui/material';
import { useQuery } from '@tanstack/react-query';

const dayOfWeeks = ['MO', 'TU', 'WE', 'TH', 'FR', 'SA', 'SU'];

const ChannelYearsPage: React.FC = (): JSX.Element => {
  const { slug } = useParams();
  const navigate = useNavigate();
  const { enqueueSnackbar } = useSnackbar();
  const [searchParams, setSearchParams] = useSearchParams();
  const [year, setYear] = useState('');

  const {
    data: yearsData,
    isError: yearsError,
    isFetching: yearsFetching,
    refetch: yearsRefetch,
  } = useQuery({
    queryKey: ['channelPersistedYears', slug],
    queryFn: async () => await fetchPersistedChannelYears(slug),
    enabled: !!slug,
  });

  const { data, isFetching, isError, refetch } = useQuery({
    queryKey: ['tvChannelYears', slug, year],
    queryFn: async () => await fetchTvChannelYearMonths(slug, year),
    enabled: !!slug && !!year,
  });

  const generatedDayOfWeeks: JSX.Element[] = dayOfWeeks.map(dayOfWeek => (
    <Grid key={dayOfWeek} item xs={1} padding={1} textAlign="center">
      {dayOfWeek}
    </Grid>
  ));

  const onRefetchData = (): void => {
    if (year) {
      yearsRefetch();
      refetch();
      navigate(`/channel/${slug}/years?year=${year}`);
    }
  };

  useEffect(() => {
    if (yearsError || isError) {
      navigate('/');
      enqueueSnackbar('Unable to fetch data!', { variant: 'error' });
    }
  }, [yearsError, isError]);

  useEffect(() => {
    const year = searchParams.get('year');
    if (year) {
      setYear(year);
    }
  }, []);

  if (yearsFetching || isFetching) {
    return (
      <Box display="flex" justifyContent="center" marginTop={4}>
        <CircularProgress />
      </Box>
    );
  }

  return (
    <>
      <Box display="flex">
        <FormControl sx={{ m: 1, minWidth: 120 }} component={Box} flexGrow={1}>
          <InputLabel id="years-label">Year</InputLabel>
          <Select
            labelId="years-label"
            value={year}
            onChange={e => {
              const year = e.target.value as string;
              setYear(year);
              setSearchParams({ year });
            }}
            label="Year">
            <MenuItem value="">
              <em>None</em>
            </MenuItem>
            {yearsData?.map(year => (
              <MenuItem key={year} value={year}>
                {year}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
        <Button variant="contained" onClick={onRefetchData}>
          <RefreshIcon />
        </Button>
      </Box>
      <Box key={year} marginBottom={2}>
        {data && data?.length !== 0 ? (
          <Grid container spacing={2}>
            {data?.map(({ name, countOfEmptyBlocks, days }) => (
              <Grid key={name} item xs={12} sm={6} md={4} lg={3}>
                <Paper sx={{ height: '100%' }}>
                  <Box
                    padding={2}
                    textTransform="capitalize"
                    component={Typography}
                    fontWeight={500}>
                    {name}
                  </Box>
                  <Grid container columns={7} padding={2}>
                    {generatedDayOfWeeks}
                    <EmptyBlocks countOfEmptyBlocks={countOfEmptyBlocks} />
                    {days.map(({ number, isoDate }) => (
                      <Grid key={isoDate} item xs={1}>
                        <Box
                          component={Link}
                          to={`/channel/${slug}/archive/${isoDate}`}
                          width="100%"
                          height="100%"
                          display="flex"
                          justifyContent="center"
                          alignItems="center"
                          paddingY={1.5}
                          sx={{ textDecoration: 'none' }}>
                          {number}
                        </Box>
                      </Grid>
                    ))}
                  </Grid>
                </Paper>
              </Grid>
            ))}
          </Grid>
        ) : (
          <Alert severity="warning">
            No saved any days from selected year or not selected any year.
          </Alert>
        )}
      </Box>
    </>
  );
};

export default ChannelYearsPage;
