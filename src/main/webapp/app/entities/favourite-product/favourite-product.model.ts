import { IProduct } from 'app/entities/product/product.model';

export interface IFavouriteProduct {
  id?: number;
  userId?: number | null;
  product?: IProduct | null;
}

export class FavouriteProduct implements IFavouriteProduct {
  constructor(public id?: number, public userId?: number | null, public product?: IProduct | null) {}
}

export function getFavouriteProductIdentifier(favouriteProduct: IFavouriteProduct): number | undefined {
  return favouriteProduct.id;
}
