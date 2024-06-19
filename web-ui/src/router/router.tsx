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
import { createElement, lazy } from 'react';
import { createBrowserRouter } from 'react-router-dom';
import ChannelDetailsPage from '@/domain/channel-details';
import ChannelYearsPage from '@/domain/channel-years';
import MainLayout from './MainLayout';

const router = createBrowserRouter([
  {
    path: '/',
    element: <MainLayout />,
    children: [
      {
        path: '/',
        element: createElement(lazy(() => import('@/domain/tv-channels'))),
      },
      {
        path: '/channel/:slug',
        element: createElement(lazy(() => import('@/domain/tv-channel'))),
        children: [
          {
            path: 'details',
            element: <ChannelDetailsPage />,
          },
          {
            path: 'years',
            element: <ChannelYearsPage />,
          },
        ],
      },
      {
        path: '/channel/:slug/archive/:date',
        element: createElement(
          lazy(() => import('@/domain/channel-archive-day'))
        ),
      },
      {
        path: '/search',
        element: createElement(lazy(() => import('@/domain/search-content'))),
      },
      {
        path: '*',
        element: createElement(lazy(() => import('@/domain/not-found'))),
      },
    ],
  },
]);

export default router;
