import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FavouriteProductComponent } from '../list/favourite-product.component';
import { FavouriteProductDetailComponent } from '../detail/favourite-product-detail.component';
import { FavouriteProductUpdateComponent } from '../update/favourite-product-update.component';
import { FavouriteProductRoutingResolveService } from './favourite-product-routing-resolve.service';

const favouriteProductRoute: Routes = [
  {
    path: '',
    component: FavouriteProductComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FavouriteProductDetailComponent,
    resolve: {
      favouriteProduct: FavouriteProductRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FavouriteProductUpdateComponent,
    resolve: {
      favouriteProduct: FavouriteProductRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FavouriteProductUpdateComponent,
    resolve: {
      favouriteProduct: FavouriteProductRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(favouriteProductRoute)],
  exports: [RouterModule],
})
export class FavouriteProductRoutingModule {}
