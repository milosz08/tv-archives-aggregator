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

export default useYearSelect;
