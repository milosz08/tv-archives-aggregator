export type Program = {
  name: string;
  description?: string;
  programType: string;
  isTvShow: boolean;
  season?: number;
  episode?: number;
  badge?: string;
  hourStart: string;
};

export type ProgramDayDetails = {
  channelName: string;
  listOfPrograms: Program[];
};
