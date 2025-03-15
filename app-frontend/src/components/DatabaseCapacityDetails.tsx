import * as React from 'react';
import { useAxios } from '@/api';
import { RefreshSectionHeader, SuspensePartFallback } from '@/components';
import { formatLargeNumber, parseBytes } from '@/utils';
import { Box, Grid2, Paper, Table, TableBody, TableContainer } from '@mui/material';
import { useQuery } from '@tanstack/react-query';
import { TableContentRow } from './TableContentRow';

type Props = {
  header: string;
  queryKey: unknown[];
  slug?: string;
  enabled?: boolean;
};

const DatabaseCapacityDetails: React.FC<Props> = ({
  header,
  queryKey,
  slug = null,
  enabled = true,
}): React.ReactElement => {
  const { api } = useAxios();

  const { data, isFetching, refetch } = useQuery({
    queryKey,
    queryFn: async () => await api.fetchDatabaseCapacityDetails(slug),
    enabled,
  });

  if (!data || isFetching) {
    return <SuspensePartFallback />;
  }

  return (
    <Box>
      <RefreshSectionHeader onRefresh={() => refetch()}>{header}</RefreshSectionHeader>
      <Grid2 container spacing={2}>
        <Grid2 size={{ sm: 12, md: 6 }} width="100%">
          <TableContainer component={Paper}>
            <Table>
              <TableBody>
                <TableContentRow
                  label="Total persisted days"
                  value={formatLargeNumber(data.persistedDays)}
                />
                <TableContentRow
                  label="Total persisted years"
                  value={formatLargeNumber(data.persistedYears)}
                />
              </TableBody>
            </Table>
          </TableContainer>
        </Grid2>
        <Grid2 size={{ sm: 12, md: 6 }} width="100%">
          <TableContainer component={Paper}>
            <Table>
              <TableBody>
                <TableContentRow
                  label="Total persisted TV programs"
                  value={formatLargeNumber(data.persistedTvPrograms)}
                />
                <TableContentRow label="Data size" value={parseBytes(data.averageDbSize)} />
              </TableBody>
            </Table>
          </TableContainer>
        </Grid2>
      </Grid2>
    </Box>
  );
};

export { DatabaseCapacityDetails };
