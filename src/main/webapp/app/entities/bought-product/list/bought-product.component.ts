import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBoughtProduct } from '../bought-product.model';
import { BoughtProductService } from '../service/bought-product.service';
import { BoughtProductDeleteDialogComponent } from '../delete/bought-product-delete-dialog.component';

@Component({
  selector: 'jhi-bought-product',
  templateUrl: './bought-product.component.html',
})
export class BoughtProductComponent implements OnInit {
  boughtProducts?: IBoughtProduct[];
  isLoading = false;

  constructor(protected boughtProductService: BoughtProductService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.boughtProductService.query().subscribe(
      (res: HttpResponse<IBoughtProduct[]>) => {
        this.isLoading = false;
        this.boughtProducts = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IBoughtProduct): number {
    return item.id!;
  }

  delete(boughtProduct: IBoughtProduct): void {
    const modalRef = this.modalService.open(BoughtProductDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.boughtProduct = boughtProduct;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
