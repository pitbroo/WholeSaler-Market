import { IBoughtProduct } from 'app/entities/bought-product/bought-product.model';
import { ISoldProduct } from 'app/entities/sold-product/sold-product.model';

export interface IProduct {
  id?: number;
  name?: string | null;
  price?: number | null;
  seller?: string | null;
  boughtProducts?: IBoughtProduct[] | null;
  soldProducts?: ISoldProduct[] | null;
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public name?: string | null,
    public price?: number | null,
    public seller?: string | null,
    public boughtProducts?: IBoughtProduct[] | null,
    public soldProducts?: ISoldProduct[] | null
  ) {}
}

export function getProductIdentifier(product: IProduct): number | undefined {
  return product.id;
}
