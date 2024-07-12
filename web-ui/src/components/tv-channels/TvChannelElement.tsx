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
import { Link as RouterLink } from 'react-router-dom';
import { TvChannel } from '@/api/types/tv-channel';
import { Box, Button, Grid, Paper, Typography } from '@mui/material';

type Props = {
  tvChannel: TvChannel;
};

const TvChannelElement: React.FC<Props> = ({ tvChannel }): JSX.Element => (
  <Grid key={tvChannel.slug} item xs={12} md={6} lg={4}>
    <Paper>
      <Button
        component={RouterLink}
        to={`/channel/${tvChannel.slug}/details`}
        fullWidth={true}
        sx={{ padding: 2 }}>
        <Box display="flex" flexDirection="column" alignItems="center">
          <Typography variant="h6" component="div">
            {tvChannel.name}
          </Typography>
          <Typography variant="body1" color="gray" fontSize={14}>
            {tvChannel.persistedDays} days
          </Typography>
        </Box>
      </Button>
    </Paper>
  </Grid>
);

export default TvChannelElement;
