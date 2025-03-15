import * as React from 'react';
import { Link as RouterLink } from 'react-router-dom';
import { PageLink } from '@/types/page-link';
import { AppBar, Box, Button, Toolbar, Typography } from '@mui/material';

type Props = {
  links: PageLink[];
};

const Header: React.FC<Props> = ({ links }): React.ReactElement => (
  <AppBar position="fixed">
    <Toolbar>
      <Typography variant="h6" component="div" marginRight={4}>
        TV Archive aggregator
      </Typography>
      {links.map(({ name, slug }) => (
        <Box key={slug}>
          <Button component={RouterLink} to={slug} style={{ color: 'white' }}>
            {name}
          </Button>
        </Box>
      ))}
    </Toolbar>
  </AppBar>
);

export { Header };
