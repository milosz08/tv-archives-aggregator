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
import { lazy } from 'react';
import { RouterProvider, createBrowserRouter } from 'react-router-dom';
import MainLayout from './MainLayout';

/* eslint-disable */
const TvChannelsPage = lazy(() => import('@/app/pages/tv-channels'));
const TvChannelPage = lazy(() => import('@/app/pages/tv-channel'));
const ChannelDetailsPage = lazy(() => import('@/app/pages/channel-details'));
const ChannelYearsPage = lazy(() => import('@/app/pages/channel-years'));
const ChannelArchiveDayPage = lazy(() => import('@/app/pages/channel-archive-day'));
const SearchContentPage = lazy(() => import('@/app/pages/search-content'));
const NotFoundPage = lazy(() => import('@/app/pages/not-found'));
/* eslint-enable */

const router = createBrowserRouter([
  {
    path: '/',
    element: <MainLayout />,
    children: [
      { path: '/', element: <TvChannelsPage /> },
      {
        path: '/channel/:slug',
        element: <TvChannelPage />,
        children: [
          { path: 'details', element: <ChannelDetailsPage /> },
          { path: 'years', element: <ChannelYearsPage /> },
        ],
      },
      {
        path: '/channel/:slug/archive/:date',
        element: <ChannelArchiveDayPage />,
      },
      { path: '/search', element: <SearchContentPage /> },
      { path: '*', element: <NotFoundPage /> },
    ],
  },
]);

const AppRouter: React.FC = (): JSX.Element => (
  <RouterProvider router={router} />
);

export default AppRouter;
