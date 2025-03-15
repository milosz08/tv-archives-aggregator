import * as React from 'react';
import { Suspense } from 'react';
import { Outlet } from 'react-router';
import { Header, SuspenseFallback } from '@/components';
import { PageLink } from '@/types/page-link';
import { Box, Container } from '@mui/material';
import { LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';

const routerLinks: PageLink[] = [
  {
    name: 'TV channels',
    slug: '/',
  },
  {
    name: 'Search',
    slug: '/search',
  },
];

const MainLayout: React.FC = (): React.ReactElement => (
  <Suspense fallback={<SuspenseFallback />}>
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <Header links={routerLinks} />
      <Container maxWidth="xl">
        <Box margin={3} marginTop={13}>
          <Outlet />
        </Box>
      </Container>
    </LocalizationProvider>
  </Suspense>
);

export { MainLayout };
