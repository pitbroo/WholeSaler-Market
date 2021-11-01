import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProducts } from '../products.model';
import { ProductsService } from '../service/products.service';
import { ProductsDeleteDialogComponent } from '../delete/products-delete-dialog.component';

@Component({
  selector: 'jhi-products',
  templateUrl: './products.component.html',
})
export class ProductsComponent implements OnInit {
  products?: IProducts[];
  isLoading = false;

  constructor(protected productsService: ProductsService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.productsService.query().subscribe(
      (res: HttpResponse<IProducts[]>) => {
        this.isLoading = false;
        this.products = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IProducts): number {
    return item.id!;
  }

  delete(products: IProducts): void {
    const modalRef = this.modalService.open(ProductsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.products = products;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
