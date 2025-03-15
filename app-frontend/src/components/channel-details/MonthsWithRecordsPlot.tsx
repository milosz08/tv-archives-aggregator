import * as React from 'react';
import { useCallback, useEffect, useState } from 'react';
import { useParams } from 'react-router';
import { useAxios } from '@/api';
import {
  MonthlyProgramChartStackElement,
  MonthlyProgramsChartDto,
} from '@/api/types/tv-channel-chart';
import { RefreshSectionHeader, SuspensePartFallback, YearSelect } from '@/components';
import { useYearSelect } from '@/hooks';
import { Box, FormControlLabel, FormGroup, Paper, Switch } from '@mui/material';
import { BarChart, BarChartProps, LineChart, LineChartProps } from '@mui/x-charts';
import { useQuery } from '@tanstack/react-query';
import { ChartTable } from './ChartTable';

type Chart<T> = Partial<Omit<T, 'series'>>;

const chartProps = (data: MonthlyProgramsChartDto, height: number) => ({
  height,
  slotProps: {
    legend: {
      hidden: true,
    },
  },
  grid: {
    vertical: true,
    horizontal: true,
  },
  xAxis: [
    {
      scaleType: 'band',
      data: data.months,
    },
  ],
  skipAnimation: true,
});

const othersKey = 'others';

const MonthsWithRecordsPlot: React.FC = (): React.ReactElement => {
  const { slug } = useParams();
  const { api } = useAxios();

  const { years, year, setYear, isYearsFetching, refetchYears } = useYearSelect();
  const [chartData, setChartData] = useState<MonthlyProgramChartStackElement[]>([]);

  const { data, isFetching, refetch } = useQuery({
    queryKey: ['monthlyChannelPrograms', slug, year],
    queryFn: async () => await api.fetchMonthlyChannelPrograms(slug, year),
    enabled: !!slug && !!year,
  });

  const computeSeries = useCallback(
    (withoutStackKey: boolean) =>
      chartData.map(({ data: seriesData, color, name }) => {
        const baseObj = {
          data: seriesData,
          color,
          stack: data?.stackKey,
          label: name,
        };
        if (withoutStackKey) {
          return Object.fromEntries(Object.entries(baseObj).filter(([key]) => key !== 'stack'));
        }
        return baseObj;
      }),
    [chartData]
  );

  const checkIsOthers = (name: string): boolean => {
    return name.toLowerCase() === othersKey;
  };

  const checkNoIsOthers = (name: string): boolean => {
    return name.toLowerCase() !== othersKey;
  };

  const onRefresh = async (): Promise<void> => {
    await refetchYears();
    await refetch();
  };

  const setToggleShowingOthers = (checked: boolean): void => {
    const othersRow = data?.series.find(({ name }) => checkIsOthers(name));
    if (othersRow) {
      setChartData(prevState =>
        checked ? [othersRow, ...prevState] : prevState.filter(({ name }) => checkNoIsOthers(name))
      );
    }
  };

  useEffect(() => {
    if (data) {
      setChartData(data.series);
    }
  }, [data]);

  if (!data || isYearsFetching || isFetching) {
    return <SuspensePartFallback />;
  }

  return (
    <Box>
      <RefreshSectionHeader onRefresh={onRefresh}>Persisted monthly records</RefreshSectionHeader>
      <YearSelect years={years} year={year} setYear={setYear} />
      <Box component={Paper} marginTop={2} padding={2}>
        <LineChart
          series={computeSeries(true)}
          {...(chartProps(data, 350) as Chart<LineChartProps>)}
        />
        <BarChart
          series={computeSeries(false)}
          {...(chartProps(data, 550) as Chart<BarChartProps>)}
        />
        <Box margin={2}>
          <FormGroup sx={{ gap: 1 }}>
            <FormControlLabel
              control={
                <Switch
                  checked={chartData.some(({ name }) => checkIsOthers(name))}
                  onChange={e => setToggleShowingOthers(e.target.checked)}
                  disabled={!data.series.some(({ name }) => checkIsOthers(name))}
                />
              }
              label="Hide/show others section on chart"
            />
          </FormGroup>
        </Box>
        <ChartTable data={data} chartData={chartData} setChartData={setChartData} />
      </Box>
    </Box>
  );
};

export { MonthsWithRecordsPlot };
