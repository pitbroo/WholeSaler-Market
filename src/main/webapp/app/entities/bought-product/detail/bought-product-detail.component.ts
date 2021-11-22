import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBoughtProduct } from '../bought-product.model';

@Component({
  selector: 'jhi-bought-product-detail',
  templateUrl: './bought-product-detail.component.html',
})
export class BoughtProductDetailComponent implements OnInit {
  boughtProduct: IBoughtProduct | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ boughtProduct }) => {
      this.boughtProduct = boughtProduct;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
