export interface ITransactionOutput {
  id?: number;
  recipient?: string | null;
  value?: number | null;
  parentTransactionId?: string | null;
}

export const defaultValue: Readonly<ITransactionOutput> = {};
