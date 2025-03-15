import * as React from 'react';
import dayjs, { Dayjs } from 'dayjs';
import { Link as RouterLink, useNavigate, useParams } from 'react-router-dom';
import { ArchiveChannelData } from '@/components/channel-archive-day';
import { Box, Grid2, Link, Paper } from '@mui/material';
import { StaticDatePicker } from '@mui/x-date-pickers';

const ChannelArchiveDayPage: React.FC = (): React.ReactElement => {
  const { slug, date } = useParams();
  const navigate = useNavigate();

  const onChangeDate = (value: Dayjs | null): void => {
    if (value) {
      navigate(`/channel/${slug}/archive/${value.format('YYYY-MM-DD')}`);
    }
  };

  return (
    <Box display="flex" flexDirection="column" rowGap={3}>
      <Link component={RouterLink} to={`/channel/${slug}/years?year=${dayjs(date).year()}`}>
        Return to channel years
      </Link>
      <Grid2 container spacing={2}>
        <Grid2 size={{ xs: 12, lg: 8, xl: 9 }}>
          <Box component={Paper} padding={2}>
            <ArchiveChannelData />
          </Box>
        </Grid2>
        <Grid2 size={{ xs: 12, lg: 4, xl: 3 }}>
          <Box component={Paper} padding={2}>
            <StaticDatePicker defaultValue={dayjs(date)} onAccept={onChangeDate} />
          </Box>
        </Grid2>
      </Grid2>
    </Box>
  );
};

export default ChannelArchiveDayPage;
