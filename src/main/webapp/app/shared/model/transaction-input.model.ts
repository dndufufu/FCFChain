export interface ITransactionInput {
  id?: number;
  transactionOutputId?: string | null;
  uTXO?: string | null;
}

export const defaultValue: Readonly<ITransactionInput> = {};
