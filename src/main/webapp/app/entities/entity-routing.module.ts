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
      {
        path: 'bought-product',
        data: { pageTitle: 'poloMarketApp.boughtProduct.home.title' },
        loadChildren: () => import('./bought-product/bought-product.module').then(m => m.BoughtProductModule),
      },
      {
        path: 'sold-product',
        data: { pageTitle: 'poloMarketApp.soldProduct.home.title' },
        loadChildren: () => import('./sold-product/sold-product.module').then(m => m.SoldProductModule),
      },
      {
        path: 'transaction',
        data: { pageTitle: 'poloMarketApp.transaction.home.title' },
        loadChildren: () => import('./transaction/transaction.module').then(m => m.TransactionModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
