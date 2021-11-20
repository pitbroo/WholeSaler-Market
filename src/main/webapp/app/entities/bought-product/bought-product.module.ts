import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BoughtProductComponent } from './list/bought-product.component';
import { BoughtProductDetailComponent } from './detail/bought-product-detail.component';
import { BoughtProductUpdateComponent } from './update/bought-product-update.component';
import { BoughtProductDeleteDialogComponent } from './delete/bought-product-delete-dialog.component';
import { BoughtProductRoutingModule } from './route/bought-product-routing.module';

@NgModule({
  imports: [SharedModule, BoughtProductRoutingModule],
  declarations: [BoughtProductComponent, BoughtProductDetailComponent, BoughtProductUpdateComponent, BoughtProductDeleteDialogComponent],
  entryComponents: [BoughtProductDeleteDialogComponent],
})
export class BoughtProductModule {}
