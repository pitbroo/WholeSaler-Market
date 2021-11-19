import { IUser } from 'app/entities/user/user.model';
import { IProduct } from 'app/entities/product/product.model';

export interface IFavouriteProduct {
  id?: number;
  user?: IUser | null;
  product?: IProduct | null;
}

export class FavouriteProduct implements IFavouriteProduct {
  constructor(public id?: number, public user?: IUser | null, public product?: IProduct | null) {}
}

export function getFavouriteProductIdentifier(favouriteProduct: IFavouriteProduct): number | undefined {
  return favouriteProduct.id;
}
