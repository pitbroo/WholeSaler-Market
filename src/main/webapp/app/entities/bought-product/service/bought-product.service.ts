import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBoughtProduct, getBoughtProductIdentifier } from '../bought-product.model';

export type EntityResponseType = HttpResponse<IBoughtProduct>;
export type EntityArrayResponseType = HttpResponse<IBoughtProduct[]>;

@Injectable({ providedIn: 'root' })
export class BoughtProductService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bought-products');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(boughtProduct: IBoughtProduct): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(boughtProduct);
    return this.http
      .post<IBoughtProduct>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(boughtProduct: IBoughtProduct): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(boughtProduct);
    return this.http
      .put<IBoughtProduct>(`${this.resourceUrl}/${getBoughtProductIdentifier(boughtProduct) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(boughtProduct: IBoughtProduct): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(boughtProduct);
    return this.http
      .patch<IBoughtProduct>(`${this.resourceUrl}/${getBoughtProductIdentifier(boughtProduct) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IBoughtProduct>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBoughtProduct[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBoughtProductToCollectionIfMissing(
    boughtProductCollection: IBoughtProduct[],
    ...boughtProductsToCheck: (IBoughtProduct | null | undefined)[]
  ): IBoughtProduct[] {
    const boughtProducts: IBoughtProduct[] = boughtProductsToCheck.filter(isPresent);
    if (boughtProducts.length > 0) {
      const boughtProductCollectionIdentifiers = boughtProductCollection.map(
        boughtProductItem => getBoughtProductIdentifier(boughtProductItem)!
      );
      const boughtProductsToAdd = boughtProducts.filter(boughtProductItem => {
        const boughtProductIdentifier = getBoughtProductIdentifier(boughtProductItem);
        if (boughtProductIdentifier == null || boughtProductCollectionIdentifiers.includes(boughtProductIdentifier)) {
          return false;
        }
        boughtProductCollectionIdentifiers.push(boughtProductIdentifier);
        return true;
      });
      return [...boughtProductsToAdd, ...boughtProductCollection];
    }
    return boughtProductCollection;
  }

  protected convertDateFromClient(boughtProduct: IBoughtProduct): IBoughtProduct {
    return Object.assign({}, boughtProduct, {
      date: boughtProduct.date?.isValid() ? boughtProduct.date.format(DATE_FORMAT) : undefined,
      price: boughtProduct.price?.isValid() ? boughtProduct.price.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((boughtProduct: IBoughtProduct) => {
        boughtProduct.date = boughtProduct.date ? dayjs(boughtProduct.date) : undefined;
        boughtProduct.price = boughtProduct.price ? dayjs(boughtProduct.price) : undefined;
      });
    }
    return res;
  }
}
