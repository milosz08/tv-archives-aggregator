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
import byteSize from 'byte-size';
import { useParams } from 'react-router';
import { useAxios } from '@/api';
import {
  Box,
  Grid,
  Paper,
  Table,
  TableBody,
  TableContainer,
} from '@mui/material';
import { useQuery } from '@tanstack/react-query';
import RefreshSectionHeader from '../RefreshSectionHeader';
import SuspensePartFallback from '../SuspensePartFallback';
import TableContentRow from './TableContentRow';

const parseBytes = (bytes: number): string => {
  const { value, unit } = byteSize(bytes);
  return `${value} ${unit}`;
};

const formatLargeNumber = (num: number): string => {
  return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ' ');
};

const PersistedChannelInfo: React.FC = (): JSX.Element => {
  const { slug } = useParams();
  const { api } = useAxios();

  const { data, isFetching, refetch } = useQuery({
    queryKey: ['tvChannelPersistenceDetails', slug],
    queryFn: async () => await api.fetchTvChannelPersistenceDetails(slug),
    enabled: !!slug,
  });

  if (!data || isFetching) {
    return <SuspensePartFallback />;
  }

  return (
    <Box>
      <RefreshSectionHeader onRefresh={() => refetch()}>
        Channel persisted info
      </RefreshSectionHeader>
      <Grid container spacing={2}>
        <Grid item sm={12} md={6} width="100%">
          <TableContainer component={Paper}>
            <Table>
              <TableBody>
                <TableContentRow
                  label="Total persisted days"
                  value={formatLargeNumber(data.persistedDays)}
                />
                <TableContentRow
                  label="Total persisted years"
                  value={data.persistedYears}
                />
              </TableBody>
            </Table>
          </TableContainer>
        </Grid>
        <Grid item sm={12} md={6} width="100%">
          <TableContainer component={Paper}>
            <Table>
              <TableBody>
                <TableContentRow
                  label="Total persisted TV programs"
                  value={formatLargeNumber(data.persistedTvPrograms)}
                />
                <TableContentRow
                  label="Data size"
                  value={parseBytes(data.averageDbSize)}
                />
              </TableBody>
            </Table>
          </TableContainer>
        </Grid>
      </Grid>
    </Box>
  );
};

export default PersistedChannelInfo;
