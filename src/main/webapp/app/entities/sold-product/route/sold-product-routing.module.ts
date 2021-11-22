import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SoldProductComponent } from '../list/sold-product.component';
import { SoldProductDetailComponent } from '../detail/sold-product-detail.component';
import { SoldProductUpdateComponent } from '../update/sold-product-update.component';
import { SoldProductRoutingResolveService } from './sold-product-routing-resolve.service';

const soldProductRoute: Routes = [
  {
    path: '',
    component: SoldProductComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SoldProductDetailComponent,
    resolve: {
      soldProduct: SoldProductRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SoldProductUpdateComponent,
    resolve: {
      soldProduct: SoldProductRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SoldProductUpdateComponent,
    resolve: {
      soldProduct: SoldProductRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(soldProductRoute)],
  exports: [RouterModule],
})
export class SoldProductRoutingModule {}
