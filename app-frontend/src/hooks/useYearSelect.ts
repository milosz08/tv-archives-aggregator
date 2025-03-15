import { useState } from 'react';
import { useParams } from 'react-router';
import { useAxios } from '@/api';
import { useQuery } from '@tanstack/react-query';

const useYearSelect = () => {
  const { slug } = useParams();
  const { api } = useAxios();

  const [year, setYear] = useState<number>(0);

  const { data, isFetching, refetch } = useQuery({
    queryKey: ['channelPlotYears', slug],
    queryFn: async () => {
      const data = await api.fetchPersistedChannelYears(slug);
      if (year === 0) {
        setYear(data[0]);
      }
      return data;
    },
    enabled: !!slug,
  });

  return {
    year,
    setYear,
    years: data,
    refetchYears: refetch,
    isYearsFetching: isFetching,
  };
};

export { useYearSelect };
