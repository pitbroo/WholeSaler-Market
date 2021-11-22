import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISoldProduct, getSoldProductIdentifier } from '../sold-product.model';

export type EntityResponseType = HttpResponse<ISoldProduct>;
export type EntityArrayResponseType = HttpResponse<ISoldProduct[]>;

@Injectable({ providedIn: 'root' })
export class SoldProductService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sold-products');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(soldProduct: ISoldProduct): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(soldProduct);
    return this.http
      .post<ISoldProduct>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(soldProduct: ISoldProduct): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(soldProduct);
    return this.http
      .put<ISoldProduct>(`${this.resourceUrl}/${getSoldProductIdentifier(soldProduct) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(soldProduct: ISoldProduct): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(soldProduct);
    return this.http
      .patch<ISoldProduct>(`${this.resourceUrl}/${getSoldProductIdentifier(soldProduct) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISoldProduct>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISoldProduct[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSoldProductToCollectionIfMissing(
    soldProductCollection: ISoldProduct[],
    ...soldProductsToCheck: (ISoldProduct | null | undefined)[]
  ): ISoldProduct[] {
    const soldProducts: ISoldProduct[] = soldProductsToCheck.filter(isPresent);
    if (soldProducts.length > 0) {
      const soldProductCollectionIdentifiers = soldProductCollection.map(soldProductItem => getSoldProductIdentifier(soldProductItem)!);
      const soldProductsToAdd = soldProducts.filter(soldProductItem => {
        const soldProductIdentifier = getSoldProductIdentifier(soldProductItem);
        if (soldProductIdentifier == null || soldProductCollectionIdentifiers.includes(soldProductIdentifier)) {
          return false;
        }
        soldProductCollectionIdentifiers.push(soldProductIdentifier);
        return true;
      });
      return [...soldProductsToAdd, ...soldProductCollection];
    }
    return soldProductCollection;
  }

  protected convertDateFromClient(soldProduct: ISoldProduct): ISoldProduct {
    return Object.assign({}, soldProduct, {
      date: soldProduct.date?.isValid() ? soldProduct.date.format(DATE_FORMAT) : undefined,
      price: soldProduct.price?.isValid() ? soldProduct.price.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
      res.body.price = res.body.price ? dayjs(res.body.price) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((soldProduct: ISoldProduct) => {
        soldProduct.date = soldProduct.date ? dayjs(soldProduct.date) : undefined;
        soldProduct.price = soldProduct.price ? dayjs(soldProduct.price) : undefined;
      });
    }
    return res;
  }
}
