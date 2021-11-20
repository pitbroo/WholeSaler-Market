jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BoughtProductService } from '../service/bought-product.service';
import { IBoughtProduct, BoughtProduct } from '../bought-product.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

import { BoughtProductUpdateComponent } from './bought-product-update.component';

describe('BoughtProduct Management Update Component', () => {
  let comp: BoughtProductUpdateComponent;
  let fixture: ComponentFixture<BoughtProductUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let boughtProductService: BoughtProductService;
  let userService: UserService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [BoughtProductUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(BoughtProductUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BoughtProductUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    boughtProductService = TestBed.inject(BoughtProductService);
    userService = TestBed.inject(UserService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const boughtProduct: IBoughtProduct = { id: 456 };
      const user: IUser = { id: 92211 };
      boughtProduct.user = user;

      const userCollection: IUser[] = [{ id: 61865 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ boughtProduct });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Product query and add missing value', () => {
      const boughtProduct: IBoughtProduct = { id: 456 };
      const product: IProduct = { id: 35921 };
      boughtProduct.product = product;

      const productCollection: IProduct[] = [{ id: 87565 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ boughtProduct });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(productCollection, ...additionalProducts);
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const boughtProduct: IBoughtProduct = { id: 456 };
      const user: IUser = { id: 59926 };
      boughtProduct.user = user;
      const product: IProduct = { id: 64301 };
      boughtProduct.product = product;

      activatedRoute.data = of({ boughtProduct });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(boughtProduct));
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.productsSharedCollection).toContain(product);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BoughtProduct>>();
      const boughtProduct = { id: 123 };
      jest.spyOn(boughtProductService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ boughtProduct });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: boughtProduct }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(boughtProductService.update).toHaveBeenCalledWith(boughtProduct);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BoughtProduct>>();
      const boughtProduct = new BoughtProduct();
      jest.spyOn(boughtProductService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ boughtProduct });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: boughtProduct }));
      saveSubject.complete();

      // THEN
      expect(boughtProductService.create).toHaveBeenCalledWith(boughtProduct);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BoughtProduct>>();
      const boughtProduct = { id: 123 };
      jest.spyOn(boughtProductService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ boughtProduct });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(boughtProductService.update).toHaveBeenCalledWith(boughtProduct);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackUserById', () => {
      it('Should return tracked User primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackProductById', () => {
      it('Should return tracked Product primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackProductById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
