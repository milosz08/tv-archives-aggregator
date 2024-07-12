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
import axios from './axios';
import { ProgramDayDetails } from './types/archive-day-program';
import { TvChannelDetails, TvChannelsAlphabet } from './types/tv-channel';
import { CalendarMonth } from './types/tv-channel-years';

export const fetchTvChannels = async (channelName: string) => {
  const { data } = await axios.get<TvChannelsAlphabet>(
    '/api/v1/tv-channel/all/search',
    { params: { phrase: channelName } }
  );
  return data;
};

export const fetchTvChannelDetails = async (
  channelSlug: string | undefined
) => {
  const { data } = await axios.get<TvChannelDetails>(
    `/api/v1/tv-channel/details/${channelSlug}`
  );
  return data;
};

export const fetchTvChannelYearMonths = async (
  channelSlug: string | undefined,
  year: string
) => {
  const { data } = await axios.get<CalendarMonth[]>(
    `/api/v1/calendar/struct/channel/${channelSlug}/year/${year}`
  );
  return data;
};

export const fetchPersistedChannelYears = async (
  channelSlug: string | undefined
) => {
  const { data } = await axios.get<number[]>(
    `/api/v1/calendar/years/channel/${channelSlug}`
  );
  return data;
};

export const fetchArchiveProgramPerDay = async (
  channelSlug: string | undefined,
  date: string | undefined
) => {
  const { data } = await axios.get<ProgramDayDetails>(
    `/api/v1/program/all/channel/${channelSlug}/date/${date}`
  );
  return data;
};
