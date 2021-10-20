import { IUser } from 'app/shared/model/user.model';

export interface IWallet {
  id?: number;
  privateKey?: string | null;
  publicKey?: string | null;
  internalUser?: IUser;
}

export const defaultValue: Readonly<IWallet> = {};
