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
import { useState } from 'react';
import { SearchRecord } from '@/api/types/search-content';
import ExternalLinks from '@/components/ExternalLinks';
import ProgramBadge from '@/components/ProgramBadge';
import { formatSeasonAndEpisode } from '@/utils';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp';
import {
  Box,
  Collapse,
  IconButton,
  TableCell,
  TableRow,
  Typography,
} from '@mui/material';

type Props = {
  row: SearchRecord;
};

const ResultElementRow: React.FC<Props> = ({ row }): JSX.Element => {
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
        <TableCell>{formatSeasonAndEpisode(row)}</TableCell>
        <TableCell>{row.hourStart}</TableCell>
        <TableCell>{row.scheduleDate}</TableCell>
        <TableCell>{row.weekday}</TableCell>
      </TableRow>
      <TableRow>
        <TableCell style={{ paddingBottom: 0, paddingTop: 0 }} colSpan={8}>
          <Collapse in={open} timeout="auto" unmountOnExit>
            <Box padding={2}>
              <Typography>
                {row.description ?? <i>No description</i>}
              </Typography>
              <ExternalLinks name={row.name} />
              <ProgramBadge badge={row.badge} />
            </Box>
          </Collapse>
        </TableCell>
      </TableRow>
    </>
  );
};

export default ResultElementRow;
