import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IProduct, Product } from '../product.model';
import { ProductService } from '../service/product.service';
import { ProductDeleteDialogComponent } from '../delete/product-delete-dialog.component';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { ITransaction, Transaction } from '../../transaction/transaction.model';
import { TransactionService } from '../../transaction/service/transaction.service';
import DateTimeFormat = Intl.DateTimeFormat;
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'jhi-product',
  templateUrl: './product.component.html',
})
export class ProductComponent implements OnInit {
  products?: IProduct[];
  account!: Account;
  transaction!: Transaction;
  isSaving = false;

  isLoading = false;
  test: any | null | undefined;
  prod: any | null | undefined;

  constructor(
    protected productService: ProductService,
    protected modalService: NgbModal,
    protected accountService: AccountService,
    protected transactionService: TransactionService
  ) {
    this.test = 'test';
    this.prod = 'produkt';
  }

  loadAll(): void {
    this.isLoading = true;
    this.productService.query().subscribe(
      (res: HttpResponse<IProduct[]>) => {
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
    this.accountService.identity().subscribe(account => {
      if (account) {
        this.account = account;
      }
    });
  }

  trackId(index: number, item: IProduct): number {
    return item.id!;
  }

  delete(product: IProduct): void {
    const modalRef = this.modalService.open(ProductDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.product = product;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }

  //funkcja kuppowania przez usrea
  goToTransactions(): void {
    window.location.replace("/transaction");
  }

  buy(account: Account | undefined | null, product: Product): void {
    const transaction = new Transaction();
    //transaction.client = account?.login;
    transaction.seller = product.user;
    //transaction.client = product.seller;
    transaction.product = product;
    transaction.price = product.price;
    transaction.user = account;
    this.subscribeToSaveResponse(this.transactionService.create(transaction));
  }
  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransaction>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }
  protected onSaveFinalize(): void {
    this.isSaving = false;
  }
  protected onSaveSuccess(): void {
    this.goToTransactions();
  }
  protected onSaveError(): void {
    // Api for inheritance.
  }
}
