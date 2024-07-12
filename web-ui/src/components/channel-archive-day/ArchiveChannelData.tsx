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
import {
  Alert,
  Box,
  CircularProgress,
  Divider,
  Grid,
  Typography,
} from '@mui/material';
import { orange } from '@mui/material/colors';
import { useQuery } from '@tanstack/react-query';

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

  if (isFetching) {
    return (
      <Box display="flex" justifyContent="center" marginTop={4}>
        <CircularProgress />
      </Box>
    );
  }

  return (
    <>
      <Typography variant="h4" marginBottom={1}>
        {data?.channelName}
      </Typography>
      {data?.listOfPrograms && data?.listOfPrograms?.length > 0 ? (
        <Box marginTop={2} display="flex" flexDirection="column" rowGap={2}>
          {data?.listOfPrograms?.map(details => (
            <React.Fragment key={details.hourStart}>
              <Divider />
              <Grid container spacing={4}>
                <Grid item xs={2} fontSize={20} textAlign="right">
                  {details.hourStart}
                </Grid>
                <Grid item xs={10}>
                  <Typography
                    component="div"
                    marginBottom={1}
                    fontWeight={600}
                    color="primary">
                    {details.name}
                  </Typography>
                  <Typography component="div" marginBottom={3}>
                    {details.programType}
                    {details.season && <span>, season: {details.season}</span>}
                    {details.episode && (
                      <span>, episode: {details.episode}</span>
                    )}
                  </Typography>
                  <Typography component="div" color="gray">
                    {details.description}
                  </Typography>
                  {details.badge && (
                    <Box
                      component={Typography}
                      color="white"
                      marginTop={3}
                      paddingX={1}
                      paddingY={0.7}
                      gap={3}
                      sx={{
                        bgcolor: orange[500],
                        borderRadius: 1,
                      }}
                      maxWidth="fit-content">
                      {details.badge}
                    </Box>
                  )}
                </Grid>
              </Grid>
            </React.Fragment>
          ))}
        </Box>
      ) : (
        <Alert severity="warning">Not found any data for selected day.</Alert>
      )}
    </>
  );
};

export default ArchiveChannelData;
