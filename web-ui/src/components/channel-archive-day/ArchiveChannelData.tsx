/*
 * Copyright (c) 2024 by Miłosz Gilga <https://miloszgilga.pl>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     <http://www.apache.org/licenses/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import { useEffect } from 'react';
import React from 'react';
import { useNavigate, useParams } from 'react-router';
import { useAxios } from '@/api';
import SuspensePartFallback from '@/components/SuspensePartFallback';
import { Alert, Box, Typography } from '@mui/material';
import { useQuery } from '@tanstack/react-query';
import ArchiveElement from './ArchiveElement';

const ArchiveChannelData: React.FC = (): JSX.Element => {
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

export default ArchiveChannelData;
