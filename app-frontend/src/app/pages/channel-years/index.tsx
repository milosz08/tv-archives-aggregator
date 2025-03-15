import * as React from 'react';
import { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router';
import { Link as RouterLink, useSearchParams } from 'react-router-dom';
import { useAxios } from '@/api';
import { SuspensePartFallback, YearSelect } from '@/components';
import { EmptyBlocks } from '@/components/channel-years';
import { useYearSelect } from '@/hooks';
import { isInteger } from '@/utils';
import RefreshIcon from '@mui/icons-material/Refresh';
import { Box, Button, Grid2, Paper, Typography } from '@mui/material';
import { useQuery } from '@tanstack/react-query';

const dayOfWeeks = ['MO', 'TU', 'WE', 'TH', 'FR', 'SA', 'SU'];

const ChannelYearsPage: React.FC = (): React.ReactElement => {
  const { slug } = useParams();
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams();
  const { api } = useAxios();

  const { years, year, isYearsFetching, setYear, refetchYears } = useYearSelect();

  const { data, isFetching, refetch } = useQuery({
    queryKey: ['tvChannelYears', slug, year],
    queryFn: async () => await api.fetchTvChannelYearMonths(slug, year),
    enabled: !!slug && !!year,
  });

  const generatedDayOfWeeks: React.ReactElement[] = dayOfWeeks.map(dayOfWeek => (
    <Grid2 key={dayOfWeek} size={{ xs: 1 }} padding={1} textAlign="center">
      {dayOfWeek}
    </Grid2>
  ));

  const onRefetchData = async (): Promise<void> => {
    if (year) {
      await refetchYears();
      await refetch();
      navigate(`/channel/${slug}/years?year=${year}`);
    }
  };

  useEffect(() => {
    const year = searchParams.get('year');
    if (year && isInteger(year)) {
      setYear(parseInt(year));
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
          <Grid2 container spacing={2}>
            {data?.map(({ name, countOfEmptyBlocks, days }) => (
              <Grid2 key={name} size={{ xs: 12, sm: 6, md: 4, lg: 3 }}>
                <Paper sx={{ height: '100%' }}>
                  <Box
                    padding={2}
                    textTransform="capitalize"
                    component={Typography}
                    fontWeight={500}>
                    {name}
                  </Box>
                  <Grid2 container columns={7} padding={2}>
                    {generatedDayOfWeeks}
                    <EmptyBlocks countOfEmptyBlocks={countOfEmptyBlocks} />
                    {days.map(({ number, isoDate }) => (
                      <Grid2 key={isoDate} size={{ xs: 1 }}>
                        <Button
                          component={RouterLink}
                          to={`/channel/${slug}/archive/${isoDate}`}
                          sx={{ minWidth: '10px', width: '100%' }}>
                          {number}
                        </Button>
                      </Grid2>
                    ))}
                  </Grid2>
                </Paper>
              </Grid2>
            ))}
          </Grid2>
        )}
      </Box>
    </>
  );
};

export default ChannelYearsPage;
