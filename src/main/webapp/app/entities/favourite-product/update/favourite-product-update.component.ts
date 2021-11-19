import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFavouriteProduct, FavouriteProduct } from '../favourite-product.model';
import { FavouriteProductService } from '../service/favourite-product.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

@Component({
  selector: 'jhi-favourite-product-update',
  templateUrl: './favourite-product-update.component.html',
})
export class FavouriteProductUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  productsSharedCollection: IProduct[] = [];

  editForm = this.fb.group({
    id: [],
    user: [],
    product: [],
  });

  constructor(
    protected favouriteProductService: FavouriteProductService,
    protected userService: UserService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ favouriteProduct }) => {
      this.updateForm(favouriteProduct);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const favouriteProduct = this.createFromForm();
    if (favouriteProduct.id !== undefined) {
      this.subscribeToSaveResponse(this.favouriteProductService.update(favouriteProduct));
    } else {
      this.subscribeToSaveResponse(this.favouriteProductService.create(favouriteProduct));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackProductById(index: number, item: IProduct): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFavouriteProduct>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(favouriteProduct: IFavouriteProduct): void {
    this.editForm.patchValue({
      id: favouriteProduct.id,
      user: favouriteProduct.user,
      product: favouriteProduct.product,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, favouriteProduct.user);
    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing(
      this.productsSharedCollection,
      favouriteProduct.product
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing(products, this.editForm.get('product')!.value))
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }

  protected createFromForm(): IFavouriteProduct {
    return {
      ...new FavouriteProduct(),
      id: this.editForm.get(['id'])!.value,
      user: this.editForm.get(['user'])!.value,
      product: this.editForm.get(['product'])!.value,
    };
  }
}
