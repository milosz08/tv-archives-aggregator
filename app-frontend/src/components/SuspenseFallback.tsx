import * as React from 'react';
import { Box, CircularProgress } from '@mui/material';

const SuspenseFallback: React.FC = (): React.ReactElement => (
  <Box display="flex" justifyContent="center" alignItems="center" minHeight="100vh">
    <CircularProgress />
  </Box>
);

export { SuspenseFallback };
