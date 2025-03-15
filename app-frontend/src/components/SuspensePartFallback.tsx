import * as React from 'react';
import { Box, CircularProgress } from '@mui/material';

const SuspensePartFallback: React.FC = (): React.ReactElement => (
  <Box display="flex" justifyContent="center" marginTop={4}>
    <CircularProgress />
  </Box>
);

export { SuspensePartFallback };
