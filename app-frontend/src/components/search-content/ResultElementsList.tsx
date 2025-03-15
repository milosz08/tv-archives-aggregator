import * as React from 'react';
import { v4 as uuidv4 } from 'uuid';
import { SuspensePartFallback } from '@/components';
import { useSearchFilterContext } from '@/context/SearchFilterContext';
import { ROWS_PER_PAGE, isInteger } from '@/utils';
import {
  Alert,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TablePagination,
  TableRow,
} from '@mui/material';
import { ResultElementRow } from './ResultElementRow';

const ResultElementsList: React.FC = (): React.ReactElement => {
  const { isFetching, searchResult, page, pageSize, setPage, setPageSize } =
    useSearchFilterContext();

  if (isFetching) {
    return <SuspensePartFallback />;
  }

  if (!searchResult) {
    return (
      <Alert variant="outlined" severity="info">
        Press &quot;Search content&quot; button to start search.
      </Alert>
    );
  }

  if (!searchResult || searchResult.elements.length === 0) {
    return (
      <Alert variant="outlined" severity="warning">
        Not found any content.
      </Alert>
    );
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
          if (isInteger(pageSize)) {
            setPageSize(parseInt(pageSize));
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
              {searchResult.viewTvShowColumn && <TableCell>Season/episode</TableCell>}
              <TableCell>Hour start</TableCell>
              <TableCell>Date</TableCell>
              <TableCell>Weekday</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {searchResult.elements.map(row => (
              <ResultElementRow
                key={uuidv4()}
                row={row}
                viewTvShowColumn={searchResult.viewTvShowColumn}
              />
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </>
  );
};

export { ResultElementsList };
