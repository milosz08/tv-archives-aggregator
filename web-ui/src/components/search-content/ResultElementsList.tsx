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
import { v4 as uuidv4 } from 'uuid';
import { useSearchFilterContext } from '@/context/SearchFilterContext';
import { ROWS_PER_PAGE } from '@/utils';
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TablePagination,
  TableRow,
} from '@mui/material';
import SuspensePartFallback from '../SuspensePartFallback';
import ResultElementRow from './ResultElementRow';

const ResultElementsList: React.FC = (): JSX.Element => {
  const { isFetching, searchResult, page, pageSize, setPage, setPageSize } =
    useSearchFilterContext();

  if (!searchResult || isFetching) {
    return <SuspensePartFallback />;
  }

  return (
    <>
      <TablePagination
        rowsPerPageOptions={ROWS_PER_PAGE}
        component="div"
        count={searchResult.totalElements}
        rowsPerPage={pageSize}
        page={page - 1}
        onPageChange={(_, newPage) => setPage(newPage + 1)}
        onRowsPerPageChange={e => {
          const pageSize = e.target.value;
          if (typeof pageSize === 'number') {
            setPageSize(pageSize as number);
          }
          setPage(1);
        }}
      />
      <TableContainer>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell />
              <TableCell>Name</TableCell>
              <TableCell>Tv channel</TableCell>
              <TableCell>Program type</TableCell>
              <TableCell>Season/episode</TableCell>
              <TableCell>Hour start</TableCell>
              <TableCell>Date</TableCell>
              <TableCell>Weekday</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {searchResult.elements.map(row => (
              <ResultElementRow key={uuidv4()} row={row} />
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </>
  );
};

export default ResultElementsList;
