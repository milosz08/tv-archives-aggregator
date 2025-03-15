import * as React from 'react';
import { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router';
import { useAxios } from '@/api';
import { SuspensePartFallback } from '@/components';
import { Alert, Box, Typography } from '@mui/material';
import { useQuery } from '@tanstack/react-query';
import { ArchiveElement } from './ArchiveElement';

const ArchiveChannelData: React.FC = (): React.ReactElement => {
  const { slug, date } = useParams();
  const navigate = useNavigate();
  const { api } = useAxios();

  const { data, isFetching, isError } = useQuery({
    queryKey: ['channelArchivedDay', slug, date],
    queryFn: async () => await api.fetchArchiveProgramPerDay(slug, date),
    enabled: !!slug && !!date,
  });

  useEffect(() => {
    if (isError) {
      navigate(`/channel/${slug}/years`);
    }
  }, [isError]);

  if (!data || isFetching) {
    return <SuspensePartFallback />;
  }

  let content;
  if (data.listOfPrograms.length === 0) {
    content = (
      <Alert severity="warning" variant="outlined">
        Not found any data for selected day.
      </Alert>
    );
  } else {
    content = (
      <Box marginTop={2} display="flex" flexDirection="column" rowGap={2}>
        {data?.listOfPrograms?.map(details => (
          <ArchiveElement key={details.hourStart} details={details} />
        ))}
      </Box>
    );
  }

  return (
    <>
      <Typography variant="h4" marginBottom={1}>
        {data?.channelName}
      </Typography>
      {content}
    </>
  );
};

export { ArchiveChannelData };
