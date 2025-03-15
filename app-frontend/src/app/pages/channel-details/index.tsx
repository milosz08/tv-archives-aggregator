import * as React from 'react';
import { useParams } from 'react-router';
import { DatabaseCapacityDetails } from '@/components';
import { MonthsWithRecordsPlot } from '@/components/channel-details';
import { Divider } from '@mui/material';

const ChannelDetailsPage: React.FC = (): React.ReactElement => {
  const { slug } = useParams();

  return (
    <>
      <DatabaseCapacityDetails
        header="Channel capacity info"
        queryKey={['databaseCapacityDetails', slug]}
        slug={slug}
        enabled={!!slug}
      />
      <Divider />
      <MonthsWithRecordsPlot />
    </>
  );
};

export default ChannelDetailsPage;
