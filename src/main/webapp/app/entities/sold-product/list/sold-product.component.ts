import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISoldProduct } from '../sold-product.model';
import { SoldProductService } from '../service/sold-product.service';
import { SoldProductDeleteDialogComponent } from '../delete/sold-product-delete-dialog.component';

@Component({
  selector: 'jhi-sold-product',
  templateUrl: './sold-product.component.html',
})
export class SoldProductComponent implements OnInit {
  soldProducts?: ISoldProduct[];
  isLoading = false;

  constructor(protected soldProductService: SoldProductService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.soldProductService.query().subscribe(
      (res: HttpResponse<ISoldProduct[]>) => {
        this.isLoading = false;
        this.soldProducts = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ISoldProduct): number {
    return item.id!;
  }

  delete(soldProduct: ISoldProduct): void {
    const modalRef = this.modalService.open(SoldProductDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.soldProduct = soldProduct;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
