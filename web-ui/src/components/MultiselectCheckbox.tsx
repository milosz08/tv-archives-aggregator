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
import { SelectRecord } from '@/api/types/search-content';
import CheckBoxIcon from '@mui/icons-material/CheckBox';
import CheckBoxOutlineBlankIcon from '@mui/icons-material/CheckBoxOutlineBlank';
import {
  Autocomplete,
  Checkbox,
  CircularProgress,
  TextField,
} from '@mui/material';
import { QueryObserverResult } from '@tanstack/react-query';

type Props = {
  id: string;
  label: string;
  elements: SelectRecord[];
  onChange: (selectedElements: SelectRecord[]) => void;
  refetch: () => Promise<QueryObserverResult<SelectRecord[], Error>>;
};

const MultiselectCheckbox: React.FC<Props> = ({
  id,
  label,
  elements,
  onChange,
  refetch,
}): JSX.Element => {
  const [open, setOpen] = useState(false);
  const loading = open && elements.length === 0;

  useEffect(() => {
    if (open) {
      refetch();
    }
  }, [open]);

  return (
    <Autocomplete
      size="small"
      multiple
      open={open}
      onOpen={() => setOpen(true)}
      onClose={() => setOpen(false)}
      loading={loading}
      id={id}
      limitTags={10}
      options={[...elements]}
      disableCloseOnSelect
      getOptionLabel={option => option.value}
      isOptionEqualToValue={(option, value) => option.id === value.id}
      renderOption={(props, option, { selected }) => {
        const { key, ...rest } = props as React.HTMLAttributes<HTMLElement> & {
          key: string;
        };
        return (
          <li key={key} {...rest}>
            <Checkbox
              icon={<CheckBoxOutlineBlankIcon fontSize="small" />}
              checkedIcon={<CheckBoxIcon fontSize="small" />}
              style={{ marginRight: 4 }}
              checked={selected}
            />
            {option.value}
          </li>
        );
      }}
      onChange={(_, value) => onChange(value)}
      renderInput={params => (
        <TextField
          {...params}
          label={label}
          InputProps={{
            ...params.InputProps,
            endAdornment: (
              <>
                {loading ? (
                  <CircularProgress color="inherit" size={20} />
                ) : null}
                {params.InputProps.endAdornment}
              </>
            ),
          }}
        />
      )}
    />
  );
};

export default MultiselectCheckbox;
