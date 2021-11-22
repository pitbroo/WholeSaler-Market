import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IProduct } from 'app/entities/product/product.model';

export interface IBoughtProduct {
  id?: number;
  date?: dayjs.Dayjs | null;
  price?: dayjs.Dayjs | null;
  user?: IUser | null;
  product?: IProduct | null;
}

export class BoughtProduct implements IBoughtProduct {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public price?: dayjs.Dayjs | null,
    public user?: IUser | null,
    public product?: IProduct | null
  ) {}
}

export function getBoughtProductIdentifier(boughtProduct: IBoughtProduct): number | undefined {
  return boughtProduct.id;
}
