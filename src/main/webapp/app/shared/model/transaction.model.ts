import dayjs from 'dayjs';

export interface ITransaction {
  id?: number;
  hash?: string | null;
  sender?: string | null;
  reciepent?: string | null;
  value?: number | null;
  signature?: string | null;
  timestamp?: string | null;
  status?: boolean | null;
}

export const defaultValue: Readonly<ITransaction> = {
  status: false,
};
