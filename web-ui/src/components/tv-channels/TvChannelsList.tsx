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
import { TvChannelsAlphabet } from '@/api/types/tv-channel';
import { Alert, Box, Grid, Typography } from '@mui/material';
import SuspensePartFallback from '../SuspensePartFallback';
import TvChannelElement from './TvChannelElement';

type Props = {
  data: TvChannelsAlphabet | undefined;
  isFetching: boolean;
};

const TvChannelsList: React.FC<Props> = ({ data, isFetching }): JSX.Element => {
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
          <Typography
            variant="h5"
            component="div"
            fontWeight={500}
            marginBottom={1}>
            {key.toUpperCase()} - ({data[key].length})
          </Typography>
          <Grid container spacing={2}>
            {data[key].map(tvChannel => (
              <TvChannelElement key={tvChannel.slug} tvChannel={tvChannel} />
            ))}
          </Grid>
        </Box>
      ))}
    </Box>
  );
};

export default TvChannelsList;
