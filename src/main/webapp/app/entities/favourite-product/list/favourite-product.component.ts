import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFavouriteProduct } from '../favourite-product.model';
import { FavouriteProductService } from '../service/favourite-product.service';
import { FavouriteProductDeleteDialogComponent } from '../delete/favourite-product-delete-dialog.component';

@Component({
  selector: 'jhi-favourite-product',
  templateUrl: './favourite-product.component.html',
})
export class FavouriteProductComponent implements OnInit {
  favouriteProducts?: IFavouriteProduct[];
  isLoading = false;

  constructor(protected favouriteProductService: FavouriteProductService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.favouriteProductService.query().subscribe(
      (res: HttpResponse<IFavouriteProduct[]>) => {
        this.isLoading = false;
        this.favouriteProducts = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IFavouriteProduct): number {
    return item.id!;
  }

  delete(favouriteProduct: IFavouriteProduct): void {
    const modalRef = this.modalService.open(FavouriteProductDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.favouriteProduct = favouriteProduct;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
