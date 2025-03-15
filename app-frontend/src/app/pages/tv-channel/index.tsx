import * as React from 'react';
import { Suspense, useEffect, useState } from 'react';
import { Outlet, Link as RouterLink, useLocation, useNavigate, useParams } from 'react-router-dom';
import { useAxios } from '@/api';
import { SuspensePartFallback } from '@/components';
import { ChannelDetailsProvider } from '@/context';
import { Box, CircularProgress, Link, Tab, Tabs, Typography } from '@mui/material';
import { useQuery } from '@tanstack/react-query';

const TvChannelPage: React.FC = (): React.ReactElement => {
  const [value, setValue] = useState(0);
  const { slug } = useParams();
  const navigate = useNavigate();
  const location = useLocation();
  const { api } = useAxios();

  const { data, isFetching, isError } = useQuery({
    queryKey: ['tvChannelDetails', slug],
    queryFn: async () => await api.fetchTvChannelDetails(slug),
    enabled: !!slug,
  });

  const onToggleCard = (newValue: number): void => {
    let redirect = 'years';
    if (newValue == 0) {
      redirect = 'details';
    }
    setValue(newValue);
    navigate(`/channel/${slug}/${redirect}`);
  };

  useEffect(() => {
    setValue(location.pathname.includes('details') ? 0 : 1);
  }, []);

  useEffect(() => {
    if (isError || (data && !data.hasPersistedDays)) {
      navigate('/');
    }
  }, [isError]);

  if (isFetching) {
    return <SuspensePartFallback />;
  }

  return (
    <ChannelDetailsProvider details={data!}>
      <Box display="flex" flexDirection="column" rowGap={3}>
        <Link component={RouterLink} to="/">
          Return to channels
        </Link>
        {data && (
          <Typography variant="h4" fontWeight={500}>
            {data.name}
          </Typography>
        )}
        <Tabs
          value={value}
          onChange={(_, value) => onToggleCard(value)}
          variant="scrollable"
          scrollButtons="auto">
          <Tab label="Channel details" />
          <Tab label="Archived years" />
        </Tabs>
        <Suspense
          fallback={
            <Box display="flex" justifyContent="center">
              <CircularProgress />
            </Box>
          }>
          <Outlet />
        </Suspense>
      </Box>
    </ChannelDetailsProvider>
  );
};

export default TvChannelPage;
