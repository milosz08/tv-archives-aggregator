import * as React from 'react';
import { Grid2 } from '@mui/material';

type Props = {
  countOfEmptyBlocks: number;
};

const EmptyBlocks: React.FC<Props> = ({ countOfEmptyBlocks }): React.ReactElement[] =>
  Array.from({ length: countOfEmptyBlocks }, (_, i) => i).map(i => (
    <Grid2 key={i} size={{ xs: 1 }} padding={1} textAlign="center" />
  ));

export { EmptyBlocks };
