import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFavouriteProduct } from '../favourite-product.model';
import { FavouriteProductService } from '../service/favourite-product.service';

@Component({
  templateUrl: './favourite-product-delete-dialog.component.html',
})
export class FavouriteProductDeleteDialogComponent {
  favouriteProduct?: IFavouriteProduct;

  constructor(protected favouriteProductService: FavouriteProductService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.favouriteProductService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
