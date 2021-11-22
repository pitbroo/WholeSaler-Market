import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISoldProduct } from '../sold-product.model';
import { SoldProductService } from '../service/sold-product.service';

@Component({
  templateUrl: './sold-product-delete-dialog.component.html',
})
export class SoldProductDeleteDialogComponent {
  soldProduct?: ISoldProduct;

  constructor(protected soldProductService: SoldProductService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.soldProductService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
