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
import { useNavigate, useParams } from 'react-router';
import { Link, useSearchParams } from 'react-router-dom';
import { useAxios } from '@/api';
import SuspensePartFallback from '@/components/SuspensePartFallback';
import YearSelect from '@/components/YearSelect';
import EmptyBlocks from '@/components/channel-years/EmptyBlocks';
import useYearSelect from '@/hooks/useYearSelect';
import RefreshIcon from '@mui/icons-material/Refresh';
import { Box, Button, Grid, Paper, Typography } from '@mui/material';
import { useQuery } from '@tanstack/react-query';

const dayOfWeeks = ['MO', 'TU', 'WE', 'TH', 'FR', 'SA', 'SU'];

const ChannelYearsPage: React.FC = (): JSX.Element => {
  const { slug } = useParams();
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams();
  const { api } = useAxios();

  const { years, year, isYearsFetching, setYear, refetchYears } =
    useYearSelect();

  const { data, isFetching, refetch } = useQuery({
    queryKey: ['tvChannelYears', slug, year],
    queryFn: async () => await api.fetchTvChannelYearMonths(slug, year),
    enabled: !!slug && !!year,
  });

  const generatedDayOfWeeks: JSX.Element[] = dayOfWeeks.map(dayOfWeek => (
    <Grid key={dayOfWeek} item xs={1} padding={1} textAlign="center">
      {dayOfWeek}
    </Grid>
  ));

  const onRefetchData = (): void => {
    if (year) {
      refetchYears();
      refetch();
      navigate(`/channel/${slug}/years?year=${year}`);
    }
  };

  useEffect(() => {
    const year = searchParams.get('year');
    if (year && typeof year === 'number') {
      setYear(year as number);
    }
  }, []);

  if (isFetching || isYearsFetching) {
    return <SuspensePartFallback />;
  }

  return (
    <>
      <Box display="flex" columnGap={2}>
        <YearSelect
          years={years}
          year={year}
          setYear={setYear}
          isFetching={isYearsFetching}
          onSetYearCallback={year => setSearchParams({ year })}
        />
        <Button variant="contained" onClick={onRefetchData}>
          <RefreshIcon />
        </Button>
      </Box>
      <Box key={year} marginBottom={2}>
        {data && data?.length !== 0 && (
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
        )}
      </Box>
    </>
  );
};

export default ChannelYearsPage;
