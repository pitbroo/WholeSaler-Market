import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IProduct } from 'app/entities/product/product.model';

export interface ITransaction {
  id?: number;
  price?: number | null;
  date?: dayjs.Dayjs | null;
  client?: string | null;
  seller?: IUser | null;
  user?: IUser | null;
  product?: IProduct | null;
}

export class Transaction implements ITransaction {
  constructor(
    public id?: number,
    public price?: number | null,
    public date?: dayjs.Dayjs | null,
    public client?: string | null,
    public seller?: IUser | null,
    public user?: IUser | null,
    public product?: IProduct | null
  ) {}
}

export function getTransactionIdentifier(transaction: ITransaction): number | undefined {
  return transaction.id;
}
