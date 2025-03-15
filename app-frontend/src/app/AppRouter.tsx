import * as React from 'react';
import { lazy } from 'react';
import { RouterProvider, createBrowserRouter } from 'react-router-dom';
import { MainLayout } from './MainLayout';

const TvChannelsPage = lazy(() => import('@/app/pages/tv-channels'));
const TvChannelPage = lazy(() => import('@/app/pages/tv-channel'));
const ChannelDetailsPage = lazy(() => import('@/app/pages/channel-details'));
const ChannelYearsPage = lazy(() => import('@/app/pages/channel-years'));
const ChannelArchiveDayPage = lazy(() => import('@/app/pages/channel-archive-day'));
const SearchContentPage = lazy(() => import('@/app/pages/search-content'));
const NotFoundPage = lazy(() => import('@/app/pages/not-found'));

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

const AppRouter: React.FC = (): React.ReactElement => <RouterProvider router={router} />;

export { AppRouter };
