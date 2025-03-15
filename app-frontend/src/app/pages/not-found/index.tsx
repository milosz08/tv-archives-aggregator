import * as React from 'react';
import { Link as RouterLink } from 'react-router-dom';
import { Box, Button, Typography } from '@mui/material';

const NotFoundPage: React.FC = (): React.ReactElement => (
  <Box display="flex" flexDirection="column" alignItems="center" justifyContent="center">
    <Typography variant="h1" align="center">
      404
    </Typography>
    <Typography align="center">Page not found</Typography>
    <Box marginTop={4}>
      <Button component={RouterLink} to="/" variant="contained">
        Return to home
      </Button>
    </Box>
  </Box>
);

export default NotFoundPage;
