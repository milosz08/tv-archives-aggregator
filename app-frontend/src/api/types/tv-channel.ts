export type TvChannel = {
  name: string;
  slug: string;
  persistedDays: number;
};

export type TvChannelsAlphabet = {
  [symbol: string]: TvChannel[];
};

export type TvChannelDetails = {
  name: string;
  hasPersistedDays: number;
};
