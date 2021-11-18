import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'product',
        data: { pageTitle: 'poloMarketApp.product.home.title' },
        loadChildren: () => import('./product/product.module').then(m => m.ProductModule),
      },
      {
        path: 'favourite-product',
        data: { pageTitle: 'poloMarketApp.favouriteProduct.home.title' },
        loadChildren: () => import('./favourite-product/favourite-product.module').then(m => m.FavouriteProductModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
