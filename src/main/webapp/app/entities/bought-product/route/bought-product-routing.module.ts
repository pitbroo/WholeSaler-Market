import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BoughtProductComponent } from '../list/bought-product.component';
import { BoughtProductDetailComponent } from '../detail/bought-product-detail.component';
import { BoughtProductUpdateComponent } from '../update/bought-product-update.component';
import { BoughtProductRoutingResolveService } from './bought-product-routing-resolve.service';

const boughtProductRoute: Routes = [
  {
    path: '',
    component: BoughtProductComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BoughtProductDetailComponent,
    resolve: {
      boughtProduct: BoughtProductRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BoughtProductUpdateComponent,
    resolve: {
      boughtProduct: BoughtProductRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BoughtProductUpdateComponent,
    resolve: {
      boughtProduct: BoughtProductRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(boughtProductRoute)],
  exports: [RouterModule],
})
export class BoughtProductRoutingModule {}
