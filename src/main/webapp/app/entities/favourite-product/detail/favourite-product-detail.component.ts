import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFavouriteProduct } from '../favourite-product.model';

@Component({
  selector: 'jhi-favourite-product-detail',
  templateUrl: './favourite-product-detail.component.html',
})
export class FavouriteProductDetailComponent implements OnInit {
  favouriteProduct: IFavouriteProduct | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ favouriteProduct }) => {
      this.favouriteProduct = favouriteProduct;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
