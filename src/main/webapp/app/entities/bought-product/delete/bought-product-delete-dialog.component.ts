import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBoughtProduct } from '../bought-product.model';
import { BoughtProductService } from '../service/bought-product.service';

@Component({
  templateUrl: './bought-product-delete-dialog.component.html',
})
export class BoughtProductDeleteDialogComponent {
  boughtProduct?: IBoughtProduct;

  constructor(protected boughtProductService: BoughtProductService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.boughtProductService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
