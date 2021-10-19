import dayjs from 'dayjs';
import { IBlock } from 'app/shared/model/block.model';

export interface ITransaction {
  id?: number;
  hash?: string | null;
  sender?: string | null;
  recipient?: string | null;
  value?: number | null;
  signature?: string | null;
  timestamp?: string | null;
  status?: boolean | null;
  block?: IBlock;
}

export const defaultValue: Readonly<ITransaction> = {
  status: false,
};
