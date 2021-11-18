import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFavouriteProduct, FavouriteProduct } from '../favourite-product.model';
import { FavouriteProductService } from '../service/favourite-product.service';

@Injectable({ providedIn: 'root' })
export class FavouriteProductRoutingResolveService implements Resolve<IFavouriteProduct> {
  constructor(protected service: FavouriteProductService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFavouriteProduct> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((favouriteProduct: HttpResponse<FavouriteProduct>) => {
          if (favouriteProduct.body) {
            return of(favouriteProduct.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FavouriteProduct());
  }
}
