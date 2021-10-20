import { ITransaction } from 'app/shared/model/transaction.model';

export interface ITransactionOutput {
  id?: number;
  recipient?: string | null;
  value?: number | null;
  parentTransactionId?: string | null;
  transaction?: ITransaction;
}

export const defaultValue: Readonly<ITransactionOutput> = {};
