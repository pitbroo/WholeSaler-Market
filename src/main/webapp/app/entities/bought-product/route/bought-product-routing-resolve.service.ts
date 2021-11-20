import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBoughtProduct, BoughtProduct } from '../bought-product.model';
import { BoughtProductService } from '../service/bought-product.service';

@Injectable({ providedIn: 'root' })
export class BoughtProductRoutingResolveService implements Resolve<IBoughtProduct> {
  constructor(protected service: BoughtProductService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBoughtProduct> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((boughtProduct: HttpResponse<BoughtProduct>) => {
          if (boughtProduct.body) {
            return of(boughtProduct.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BoughtProduct());
  }
}
