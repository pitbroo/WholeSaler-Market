import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';

export interface ITransaction {
  id?: number;
  price?: number | null;
  date?: dayjs.Dayjs | null;
  client?: string | null;
  seller?: string | null;
  user?: IUser | null;
}

export class Transaction implements ITransaction {
  constructor(
    public id?: number,
    public price?: number | null,
    public date?: dayjs.Dayjs | null,
    public client?: string | null,
    public seller?: string | null,
    public user?: IUser | null
  ) {}
}

export function getTransactionIdentifier(transaction: ITransaction): number | undefined {
  return transaction.id;
}
