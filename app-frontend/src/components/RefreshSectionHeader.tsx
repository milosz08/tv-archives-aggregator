import * as React from 'react';
import RefreshIcon from '@mui/icons-material/Refresh';
import { Box, Button, Typography } from '@mui/material';

type Props = {
  children: React.ReactNode;
  onRefresh: () => void;
};

const RefreshSectionHeader: React.FC<Props> = ({ children, onRefresh }): React.ReactElement => (
  <Box display="flex" justifyContent="space-between" marginBottom={2}>
    <Typography fontSize={20}>{children}</Typography>
    <Button variant="contained" onClick={onRefresh}>
      <RefreshIcon />
    </Button>
  </Box>
);

export { RefreshSectionHeader };
