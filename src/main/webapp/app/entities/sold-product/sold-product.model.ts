import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IProduct } from 'app/entities/product/product.model';

export interface ISoldProduct {
  id?: number;
  date?: dayjs.Dayjs | null;
  price?: dayjs.Dayjs | null;
  user?: IUser | null;
  product?: IProduct | null;
}

export class SoldProduct implements ISoldProduct {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public price?: dayjs.Dayjs | null,
    public user?: IUser | null,
    public product?: IProduct | null
  ) {}
}

export function getSoldProductIdentifier(soldProduct: ISoldProduct): number | undefined {
  return soldProduct.id;
}
