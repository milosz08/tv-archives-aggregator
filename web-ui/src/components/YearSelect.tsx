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
import { Box, FormControl, InputLabel, MenuItem, Select } from '@mui/material';

type Props = {
  years: number[] | undefined;
  year: number;
  setYear: Dispatch<SetStateAction<number>>;
  isFetching?: boolean;
  onSetYearCallback?: (year: string) => void;
};

const YearSelect: React.FC<Props> = ({
  years,
  year,
  isFetching,
  setYear,
  onSetYearCallback,
}): JSX.Element => (
  <FormControl width="100%" component={Box} flexGrow={1}>
    <InputLabel id="years-label" size="small">
      Year
    </InputLabel>
    <Select
      labelId="years-label"
      value={year}
      onChange={e => {
        setYear(e.target.value as number);
        if (onSetYearCallback) {
          onSetYearCallback(e.target.value as string);
        }
      }}
      label="Year"
      size="small"
      sx={{ backgroundColor: 'white' }}
      disabled={isFetching}>
      {years?.map(year => (
        <MenuItem key={year} value={year}>
          {year}
        </MenuItem>
      ))}
    </Select>
  </FormControl>
);

export default YearSelect;
