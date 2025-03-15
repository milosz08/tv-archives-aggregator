import * as React from 'react';
import { Link as RouterLink } from 'react-router-dom';
import { TvChannel } from '@/api/types/tv-channel';
import { formatLargeNumber } from '@/utils';
import { Box, Button, Grid2, Paper, Typography } from '@mui/material';

type Props = {
  tvChannel: TvChannel;
};

const TvChannelElement: React.FC<Props> = ({ tvChannel }): React.ReactElement => (
  <Grid2 key={tvChannel.slug} size={{ xs: 12, md: 6, lg: 4 }}>
    <Paper>
      <Button
        component={RouterLink}
        to={`/channel/${tvChannel.slug}/details`}
        fullWidth={true}
        sx={{ padding: 2 }}
        disabled={tvChannel.persistedDays === 0}>
        <Box display="flex" flexDirection="column" alignItems="center">
          <Typography variant="h6" component="div">
            {tvChannel.name}
          </Typography>
          <Typography variant="body1" color="gray" fontSize={14}>
            {formatLargeNumber(tvChannel.persistedDays)} days
          </Typography>
        </Box>
      </Button>
    </Paper>
  </Grid2>
);

export { TvChannelElement };
