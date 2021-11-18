import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FavouriteProductComponent } from './list/favourite-product.component';
import { FavouriteProductDetailComponent } from './detail/favourite-product-detail.component';
import { FavouriteProductUpdateComponent } from './update/favourite-product-update.component';
import { FavouriteProductDeleteDialogComponent } from './delete/favourite-product-delete-dialog.component';
import { FavouriteProductRoutingModule } from './route/favourite-product-routing.module';

@NgModule({
  imports: [SharedModule, FavouriteProductRoutingModule],
  declarations: [
    FavouriteProductComponent,
    FavouriteProductDetailComponent,
    FavouriteProductUpdateComponent,
    FavouriteProductDeleteDialogComponent,
  ],
  entryComponents: [FavouriteProductDeleteDialogComponent],
})
export class FavouriteProductModule {}
