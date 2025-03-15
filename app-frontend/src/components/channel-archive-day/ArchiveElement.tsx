import * as React from 'react';
import { Program } from '@/api/types/archive-day-program';
import { ExternalLinks } from '@/components/ExternalLinks';
import { ProgramBadge } from '@/components/ProgramBadge';
import { Divider, Grid2, Typography } from '@mui/material';

type Props = {
  details: Program;
};

const ArchiveElement: React.FC<Props> = ({ details }): React.ReactElement => (
  <>
    <Divider />
    <Grid2 container spacing={4}>
      <Grid2 size={{ xs: 2 }} fontSize={20} textAlign="right">
        {details.hourStart}
      </Grid2>
      <Grid2 size={{ xs: 10 }}>
        <Typography component="div" marginBottom={1} fontWeight={600} color="primary">
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
      </Grid2>
    </Grid2>
  </>
);

export { ArchiveElement };
