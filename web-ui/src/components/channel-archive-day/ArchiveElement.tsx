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
import React from 'react';
import { Program } from '@/api/types/archive-day-program';
import ExternalLinks from '@/components/ExternalLinks';
import ProgramBadge from '@/components/ProgramBadge';
import { Divider, Grid, Typography } from '@mui/material';

type Props = {
  details: Program;
};

const ArchiveElement: React.FC<Props> = ({ details }): JSX.Element => (
  <>
    <Divider />
    <Grid container spacing={4}>
      <Grid item xs={2} fontSize={20} textAlign="right">
        {details.hourStart}
      </Grid>
      <Grid item xs={10}>
        <Typography
          component="div"
          marginBottom={1}
          fontWeight={600}
          color="primary">
          {details.name}
        </Typography>
        <Typography component="div" marginBottom={3}>
          {details.programType}
          {details.season && <span>, season: {details.season}</span>}
          {details.episode && <span>, episode: {details.episode}</span>}
        </Typography>
        <Typography component="div" color="gray">
          {details.description ?? <i>No description</i>}
        </Typography>
        <ProgramBadge badge={details.badge} />
        <ExternalLinks name={details.name} />
      </Grid>
    </Grid>
  </>
);

export default ArchiveElement;
