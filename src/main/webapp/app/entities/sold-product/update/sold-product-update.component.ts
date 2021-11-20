import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISoldProduct, SoldProduct } from '../sold-product.model';
import { SoldProductService } from '../service/sold-product.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

@Component({
  selector: 'jhi-sold-product-update',
  templateUrl: './sold-product-update.component.html',
})
export class SoldProductUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  productsSharedCollection: IProduct[] = [];

  editForm = this.fb.group({
    id: [],
    date: [],
    price: [],
    user: [],
    product: [],
  });

  constructor(
    protected soldProductService: SoldProductService,
    protected userService: UserService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ soldProduct }) => {
      this.updateForm(soldProduct);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const soldProduct = this.createFromForm();
    if (soldProduct.id !== undefined) {
      this.subscribeToSaveResponse(this.soldProductService.update(soldProduct));
    } else {
      this.subscribeToSaveResponse(this.soldProductService.create(soldProduct));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackProductById(index: number, item: IProduct): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISoldProduct>>): void {
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

  protected updateForm(soldProduct: ISoldProduct): void {
    this.editForm.patchValue({
      id: soldProduct.id,
      date: soldProduct.date,
      price: soldProduct.price,
      user: soldProduct.user,
      product: soldProduct.product,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, soldProduct.user);
    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing(this.productsSharedCollection, soldProduct.product);
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

  protected createFromForm(): ISoldProduct {
    return {
      ...new SoldProduct(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value,
      price: this.editForm.get(['price'])!.value,
      user: this.editForm.get(['user'])!.value,
      product: this.editForm.get(['product'])!.value,
    };
  }
}
