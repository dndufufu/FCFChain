export interface IWallet {
  id?: number;
  privateKey?: string | null;
  publicKey?: string | null;
}

export const defaultValue: Readonly<IWallet> = {};
