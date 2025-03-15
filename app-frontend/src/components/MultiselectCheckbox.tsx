import * as React from 'react';
import { useEffect, useState } from 'react';
import { SelectRecord } from '@/api/types/search-content';
import CheckBoxIcon from '@mui/icons-material/CheckBox';
import CheckBoxOutlineBlankIcon from '@mui/icons-material/CheckBoxOutlineBlank';
import { Autocomplete, Checkbox, CircularProgress, TextField } from '@mui/material';
import { QueryObserverResult } from '@tanstack/react-query';

type Props = {
  id: string;
  label: string;
  elements: SelectRecord[];
  limitTags?: number;
  onChange: (selectedElements: SelectRecord[]) => void;
  refetch: () => Promise<QueryObserverResult<SelectRecord[], Error>>;
};

const MultiselectCheckbox: React.FC<Props> = ({
  id,
  label,
  elements,
  limitTags = 10,
  onChange,
  refetch,
}): React.ReactElement => {
  const [open, setOpen] = useState(false);
  const loading = open && elements.length === 0;

  useEffect(() => {
    if (open) {
      refetch().then(r => r);
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
      limitTags={limitTags}
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
          slotProps={{
            input: {
              ...params.InputProps,
              endAdornment: (
                <>
                  {loading ? <CircularProgress color="inherit" size={20} /> : null}
                  {params.InputProps.endAdornment}
                </>
              ),
            },
          }}
        />
      )}
    />
  );
};

export { MultiselectCheckbox };
