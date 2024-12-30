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
import { Dispatch, SetStateAction } from 'react';
import { v4 as uuidv4 } from 'uuid';
import {
  MonthlyProgramChartStackElement,
  MonthlyProgramsChartDto,
} from '@/api/types/tv-channel-chart';
import {
  Checkbox,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
} from '@mui/material';

type Props = {
  data: MonthlyProgramsChartDto;
  chartData: MonthlyProgramChartStackElement[];
  setChartData: Dispatch<SetStateAction<MonthlyProgramChartStackElement[]>>;
};

const ChartTable: React.FC<Props> = ({
  data,
  chartData,
  setChartData,
}): JSX.Element => {
  const onToggleAllVisibility = (checked: boolean): void => {
    let series: MonthlyProgramChartStackElement[] = [];
    if (data && checked) {
      series = data.series;
    }
    setChartData(series);
  };

  const onToggleElementVisibility = (
    checked: boolean,
    row: MonthlyProgramChartStackElement
  ): void => {
    setChartData(prevState =>
      checked
        ? [...prevState, row]
        : prevState.filter(({ name }) => name !== row.name)
    );
  };

  return (
    <TableContainer sx={{ maxHeight: 540 }}>
      <Table stickyHeader size="small">
        <TableHead>
          <TableRow>
            <TableCell width={5}>
              <Checkbox
                checked={chartData.length === data.series.length}
                onChange={e => onToggleAllVisibility(e.target.checked)}
              />
            </TableCell>
            <TableCell>Type of TV channel program (per year)</TableCell>
            {data.months.map((month, index) => (
              <TableCell key={month} align="center">
                {month}
                <br />({data.allPerMonths[index]})
              </TableCell>
            ))}
          </TableRow>
        </TableHead>
        <TableBody>
          {data.table.map(row => (
            <TableRow key={row.name} sx={{ backgroundColor: row.color }}>
              <TableCell width={5}>
                {row.existInChart && (
                  <Checkbox
                    color="info"
                    checked={chartData.some(({ name }) => name === row.name)}
                    onChange={e =>
                      onToggleElementVisibility(e.target.checked, row)
                    }
                  />
                )}
              </TableCell>
              <TableCell component="th" scope="row">
                <Typography fontWeight={500} color="black">
                  {row.name} ({row.total})
                </Typography>
              </TableCell>
              {row.data.map(freq => (
                <TableCell key={uuidv4()} align="center">
                  <Typography fontWeight={500} color="black">
                    {freq}
                  </Typography>
                </TableCell>
              ))}
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
};

export default ChartTable;
