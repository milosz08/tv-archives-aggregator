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
import React, { useCallback, useEffect, useState } from 'react';
import { useParams } from 'react-router';
import { useAxios } from '@/api';
import {
  MonthlyProgramChartStackElement,
  MonthlyProgramsChartDto,
} from '@/api/types/tv-channel-chart';
import RefreshSectionHeader from '@/components/RefreshSectionHeader';
import SuspensePartFallback from '@/components/SuspensePartFallback';
import YearSelect from '@/components/YearSelect';
import useYearSelect from '@/hooks/useYearSelect';
import { Box, FormControlLabel, FormGroup, Paper, Switch } from '@mui/material';
import { BarChart, LineChart, LineChartProps } from '@mui/x-charts';
import { useQuery } from '@tanstack/react-query';
import ChartTable from './ChartTable';

const chartProps = (
  data: MonthlyProgramsChartDto,
  height: number
): Partial<Omit<LineChartProps, 'series'>> => ({
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

const MonthsWithRecordsPlot: React.FC = (): JSX.Element => {
  const { slug } = useParams();
  const { api } = useAxios();

  const { years, year, setYear, isYearsFetching, refetchYears } =
    useYearSelect();

  const [chartData, setChartData] = useState<MonthlyProgramChartStackElement[]>(
    []
  );

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
          return Object.fromEntries(
            Object.entries(baseObj).filter(([key]) => key !== 'stack')
          );
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

  const onRefresh = (): void => {
    refetchYears();
    refetch();
  };

  const setToggleShowingOthers = (checked: boolean): void => {
    const othersRow = data?.series.find(({ name }) => checkIsOthers(name));
    if (othersRow) {
      setChartData(prevState =>
        checked
          ? [othersRow, ...prevState]
          : prevState.filter(({ name }) => checkNoIsOthers(name))
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
      <RefreshSectionHeader onRefresh={onRefresh}>
        Persisted monthly records
      </RefreshSectionHeader>
      <YearSelect years={years} year={year} setYear={setYear} />
      <Box component={Paper} marginTop={2} padding={2}>
        <LineChart series={computeSeries(true)} {...chartProps(data, 350)} />
        <BarChart series={computeSeries(false)} {...chartProps(data, 550)} />
        <Box margin={2}>
          <FormGroup sx={{ gap: 1 }}>
            <FormControlLabel
              control={
                <Switch
                  checked={chartData.some(({ name }) => checkIsOthers(name))}
                  onChange={e => setToggleShowingOthers(e.target.checked)}
                  disabled={
                    !data.series.some(({ name }) => checkIsOthers(name))
                  }
                />
              }
              label="Hide/show others section on chart"
            />
          </FormGroup>
        </Box>
        <ChartTable
          data={data}
          chartData={chartData}
          setChartData={setChartData}
        />
      </Box>
    </Box>
  );
};

export default MonthsWithRecordsPlot;
