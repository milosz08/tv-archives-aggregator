import * as React from 'react';
import { useState } from 'react';
import { SearchRecord } from '@/api/types/search-content';
import { ExternalLinks } from '@/components/ExternalLinks';
import { ProgramBadge } from '@/components/ProgramBadge';
import { formatSeasonAndEpisode } from '@/utils';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp';
import { Box, Collapse, IconButton, TableCell, TableRow, Typography } from '@mui/material';

type Props = {
  row: SearchRecord;
  viewTvShowColumn: boolean;
};

const ResultElementRow: React.FC<Props> = ({ row, viewTvShowColumn }): React.ReactElement => {
  const [open, setOpen] = useState(false);

  return (
    <>
      <TableRow>
        <TableCell sx={{ padding: 0 }}>
          <IconButton size="small" onClick={() => setOpen(!open)}>
            {open ? <KeyboardArrowUpIcon /> : <KeyboardArrowDownIcon />}
          </IconButton>
        </TableCell>
        <TableCell>{row.name}</TableCell>
        <TableCell>{row.tvChannelName}</TableCell>
        <TableCell>{row.programType}</TableCell>
        {viewTvShowColumn && <TableCell>{formatSeasonAndEpisode(row)}</TableCell>}
        <TableCell>{row.hourStart}</TableCell>
        <TableCell>{row.scheduleDate}</TableCell>
        <TableCell>{row.weekday}</TableCell>
      </TableRow>
      <TableRow>
        <TableCell style={{ paddingBottom: 0, paddingTop: 0 }} colSpan={8}>
          <Collapse in={open} timeout="auto" unmountOnExit>
            <Box padding={2}>
              <Typography>{row.description ?? <i>No description</i>}</Typography>
              <ExternalLinks name={row.name} />
              <ProgramBadge badge={row.badge} />
            </Box>
          </Collapse>
        </TableCell>
      </TableRow>
    </>
  );
};

export { ResultElementRow };
