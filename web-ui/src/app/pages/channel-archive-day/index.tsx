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
import dayjs, { Dayjs } from 'dayjs';
import { Link as RouterLink, useNavigate, useParams } from 'react-router-dom';
import ArchiveChannelData from '@/components/channel-archive-day/ArchiveChannelData';
import { Box, Grid, Link, Paper } from '@mui/material';
import { StaticDatePicker } from '@mui/x-date-pickers';

const ChannelArchiveDayPage: React.FC = (): JSX.Element => {
  const { slug, date } = useParams();
  const navigate = useNavigate();

  const onChangeDate = (value: Dayjs | null): void => {
    if (value) {
      navigate(`/channel/${slug}/archive/${value.format('YYYY-MM-DD')}`);
    }
  };

  return (
    <Box display="flex" flexDirection="column" rowGap={3}>
      <Link
        component={RouterLink}
        to={`/channel/${slug}/years?year=${dayjs(date).year()}`}>
        Return to channel years
      </Link>
      <Grid container spacing={2}>
        <Grid item xs={12} lg={8} xl={9}>
          <Box component={Paper} padding={2}>
            <ArchiveChannelData />
          </Box>
        </Grid>
        <Grid item xs={12} lg={4} xl={3}>
          <Box component={Paper} padding={2}>
            <StaticDatePicker
              defaultValue={dayjs(date)}
              onAccept={onChangeDate}
            />
          </Box>
        </Grid>
      </Grid>
    </Box>
  );
};

export default ChannelArchiveDayPage;
