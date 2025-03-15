import * as React from 'react';
import { TableCell, TableRow, Typography } from '@mui/material';

type Props = {
  label: string;
  value: string | number;
};

const TableContentRow: React.FC<Props> = ({ label, value }): React.ReactElement => (
  <TableRow>
    <TableCell>{label}</TableCell>
    <TableCell>
      <Typography fontSize={15} fontWeight={500}>
        {value}
      </Typography>
    </TableCell>
  </TableRow>
);

export { TableContentRow };
