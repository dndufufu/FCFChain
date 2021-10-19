import dayjs from 'dayjs';
import { ITransaction } from 'app/shared/model/transaction.model';

export interface IBlock {
  id?: number;
  hash?: string | null;
  previousHash?: string | null;
  merkleRoot?: string | null;
  timestamp?: string | null;
  nonce?: number | null;
  tradingVolume?: number | null;
  transactions?: ITransaction[] | null;
}

export const defaultValue: Readonly<IBlock> = {};
