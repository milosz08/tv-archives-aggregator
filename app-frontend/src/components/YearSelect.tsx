import * as React from 'react';
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
}): React.ReactElement => (
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
      disabled={isFetching}>
      {years?.map(year => (
        <MenuItem key={year} value={year}>
          {year}
        </MenuItem>
      ))}
    </Select>
  </FormControl>
);

export { YearSelect };
