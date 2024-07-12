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
import { TvChannel } from '@/api/types/tv-channel';
import { Box, Grid, Typography } from '@mui/material';
import TvChannelElement from './TvChannelElement';

type Props = {
  letter: string;
  tvChannels: TvChannel[];
};

const TvChannelsWithLetter: React.FC<Props> = ({
  letter,
  tvChannels,
}): JSX.Element => (
  <Box marginBottom={2}>
    <Typography variant="h5" component="div" fontWeight={500} marginBottom={1}>
      {letter.toUpperCase()} - ({tvChannels.length})
    </Typography>
    <Grid container spacing={2}>
      {tvChannels.map(tvChannel => (
        <TvChannelElement key={tvChannel.slug} tvChannel={tvChannel} />
      ))}
    </Grid>
  </Box>
);

export default TvChannelsWithLetter;
