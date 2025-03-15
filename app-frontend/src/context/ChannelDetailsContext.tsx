import * as React from 'react';
import { TvChannelDetails } from '@/api/types/tv-channel';

type ChannelDetailsContextType = {
  details: TvChannelDetails;
};

const ChannelDetailsContext = React.createContext<ChannelDetailsContextType | null>(null);

type Props = {
  details: TvChannelDetails;
  children: React.ReactNode;
};

const ChannelDetailsProvider: React.FC<Props> = ({ details, children }) => (
  <ChannelDetailsContext.Provider value={{ details }}>{children}</ChannelDetailsContext.Provider>
);

export { ChannelDetailsProvider };
