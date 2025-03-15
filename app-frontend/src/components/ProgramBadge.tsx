import * as React from 'react';
import { Box, Typography } from '@mui/material';
import { orange } from '@mui/material/colors';

type Props = {
  badge: string | undefined;
};

const ProgramBadge: React.FC<Props> = ({ badge }): React.ReactElement => (
  <>
    {badge && (
      <Box
        component={Typography}
        color="white"
        marginTop={2}
        paddingX={1}
        paddingY={0.7}
        gap={3}
        sx={{
          bgcolor: orange[500],
          borderRadius: 1,
        }}
        maxWidth="fit-content">
        {badge}
      </Box>
    )}
  </>
);

export { ProgramBadge };
