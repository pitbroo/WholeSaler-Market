import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISoldProduct } from '../sold-product.model';

@Component({
  selector: 'jhi-sold-product-detail',
  templateUrl: './sold-product-detail.component.html',
})
export class SoldProductDetailComponent implements OnInit {
  soldProduct: ISoldProduct | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ soldProduct }) => {
      this.soldProduct = soldProduct;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
