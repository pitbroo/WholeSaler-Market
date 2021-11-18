import { IFavouriteProduct } from 'app/entities/favourite-product/favourite-product.model';

export interface IProduct {
  id?: number;
  name?: string | null;
  price?: number | null;
  favouriteProducts?: IFavouriteProduct[] | null;
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public name?: string | null,
    public price?: number | null,
    public favouriteProducts?: IFavouriteProduct[] | null
  ) {}
}

export function getProductIdentifier(product: IProduct): number | undefined {
  return product.id;
}
