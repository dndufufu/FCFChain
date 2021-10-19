import dayjs from 'dayjs';

export interface IBlock {
  id?: number;
  hash?: string | null;
  previousHash?: string | null;
  merkleRoot?: string | null;
  timestamp?: string | null;
  nonce?: number | null;
  tradingVolume?: number | null;
}

export const defaultValue: Readonly<IBlock> = {};
