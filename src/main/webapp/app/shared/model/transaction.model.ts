import dayjs from 'dayjs';
import { ITransactionInput } from 'app/shared/model/transaction-input.model';
import { ITransactionOutput } from 'app/shared/model/transaction-output.model';
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
  transactionInputs?: ITransactionInput[] | null;
  transactionOutputs?: ITransactionOutput[] | null;
  block?: IBlock;
}

export const defaultValue: Readonly<ITransaction> = {
  status: false,
};
