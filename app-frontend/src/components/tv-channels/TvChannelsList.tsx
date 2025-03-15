import * as React from 'react';
import { TvChannelsAlphabet } from '@/api/types/tv-channel';
import { SuspensePartFallback } from '@/components';
import { Alert, Box, Grid2, Typography } from '@mui/material';
import { TvChannelElement } from './TvChannelElement';

type Props = {
  data: TvChannelsAlphabet | undefined;
  isFetching: boolean;
};

const TvChannelsList: React.FC<Props> = ({ data, isFetching }): React.ReactElement => {
  if (!data || isFetching) {
    return <SuspensePartFallback />;
  }

  if (Object.keys(data).length === 0) {
    return (
      <Alert severity="warning" variant="outlined">
        Not found any channels.
      </Alert>
    );
  }

  return (
    <Box display="flex" flexDirection="column" rowGap={2} marginTop={5}>
      {Object.keys(data).map(key => (
        <Box key={key} marginBottom={2}>
          <Typography variant="h5" component="div" fontWeight={500} marginBottom={1}>
            {key.toUpperCase()} - ({data[key].length})
          </Typography>
          <Grid2 container spacing={2}>
            {data[key].map(tvChannel => (
              <TvChannelElement key={tvChannel.slug} tvChannel={tvChannel} />
            ))}
          </Grid2>
        </Box>
      ))}
    </Box>
  );
};

export { TvChannelsList };
