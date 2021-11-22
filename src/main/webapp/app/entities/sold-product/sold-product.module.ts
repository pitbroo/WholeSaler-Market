import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SoldProductComponent } from './list/sold-product.component';
import { SoldProductDetailComponent } from './detail/sold-product-detail.component';
import { SoldProductUpdateComponent } from './update/sold-product-update.component';
import { SoldProductDeleteDialogComponent } from './delete/sold-product-delete-dialog.component';
import { SoldProductRoutingModule } from './route/sold-product-routing.module';

@NgModule({
  imports: [SharedModule, SoldProductRoutingModule],
  declarations: [SoldProductComponent, SoldProductDetailComponent, SoldProductUpdateComponent, SoldProductDeleteDialogComponent],
  entryComponents: [SoldProductDeleteDialogComponent],
})
export class SoldProductModule {}
