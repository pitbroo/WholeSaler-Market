import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFavouriteProduct, getFavouriteProductIdentifier } from '../favourite-product.model';

export type EntityResponseType = HttpResponse<IFavouriteProduct>;
export type EntityArrayResponseType = HttpResponse<IFavouriteProduct[]>;

@Injectable({ providedIn: 'root' })
export class FavouriteProductService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/favourite-products');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(favouriteProduct: IFavouriteProduct): Observable<EntityResponseType> {
    return this.http.post<IFavouriteProduct>(this.resourceUrl, favouriteProduct, { observe: 'response' });
  }

  update(favouriteProduct: IFavouriteProduct): Observable<EntityResponseType> {
    return this.http.put<IFavouriteProduct>(
      `${this.resourceUrl}/${getFavouriteProductIdentifier(favouriteProduct) as number}`,
      favouriteProduct,
      { observe: 'response' }
    );
  }

  partialUpdate(favouriteProduct: IFavouriteProduct): Observable<EntityResponseType> {
    return this.http.patch<IFavouriteProduct>(
      `${this.resourceUrl}/${getFavouriteProductIdentifier(favouriteProduct) as number}`,
      favouriteProduct,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFavouriteProduct>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFavouriteProduct[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFavouriteProductToCollectionIfMissing(
    favouriteProductCollection: IFavouriteProduct[],
    ...favouriteProductsToCheck: (IFavouriteProduct | null | undefined)[]
  ): IFavouriteProduct[] {
    const favouriteProducts: IFavouriteProduct[] = favouriteProductsToCheck.filter(isPresent);
    if (favouriteProducts.length > 0) {
      const favouriteProductCollectionIdentifiers = favouriteProductCollection.map(
        favouriteProductItem => getFavouriteProductIdentifier(favouriteProductItem)!
      );
      const favouriteProductsToAdd = favouriteProducts.filter(favouriteProductItem => {
        const favouriteProductIdentifier = getFavouriteProductIdentifier(favouriteProductItem);
        if (favouriteProductIdentifier == null || favouriteProductCollectionIdentifiers.includes(favouriteProductIdentifier)) {
          return false;
        }
        favouriteProductCollectionIdentifiers.push(favouriteProductIdentifier);
        return true;
      });
      return [...favouriteProductsToAdd, ...favouriteProductCollection];
    }
    return favouriteProductCollection;
  }
}
