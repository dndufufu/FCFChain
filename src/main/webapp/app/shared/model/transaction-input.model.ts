import { ITransaction } from 'app/shared/model/transaction.model';

export interface ITransactionInput {
  id?: number;
  transactionOutputId?: string | null;
  uTXO?: string | null;
  transaction?: ITransaction;
}

export const defaultValue: Readonly<ITransactionInput> = {};
