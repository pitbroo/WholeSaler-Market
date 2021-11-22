import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISoldProduct, SoldProduct } from '../sold-product.model';
import { SoldProductService } from '../service/sold-product.service';

@Injectable({ providedIn: 'root' })
export class SoldProductRoutingResolveService implements Resolve<ISoldProduct> {
  constructor(protected service: SoldProductService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISoldProduct> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((soldProduct: HttpResponse<SoldProduct>) => {
          if (soldProduct.body) {
            return of(soldProduct.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SoldProduct());
  }
}
