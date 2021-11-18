jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FavouriteProductService } from '../service/favourite-product.service';
import { IFavouriteProduct, FavouriteProduct } from '../favourite-product.model';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

import { FavouriteProductUpdateComponent } from './favourite-product-update.component';

describe('FavouriteProduct Management Update Component', () => {
  let comp: FavouriteProductUpdateComponent;
  let fixture: ComponentFixture<FavouriteProductUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let favouriteProductService: FavouriteProductService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FavouriteProductUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(FavouriteProductUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FavouriteProductUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    favouriteProductService = TestBed.inject(FavouriteProductService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Product query and add missing value', () => {
      const favouriteProduct: IFavouriteProduct = { id: 456 };
      const product: IProduct = { id: 7525 };
      favouriteProduct.product = product;

      const productCollection: IProduct[] = [{ id: 17999 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ favouriteProduct });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(productCollection, ...additionalProducts);
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const favouriteProduct: IFavouriteProduct = { id: 456 };
      const product: IProduct = { id: 90695 };
      favouriteProduct.product = product;

      activatedRoute.data = of({ favouriteProduct });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(favouriteProduct));
      expect(comp.productsSharedCollection).toContain(product);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FavouriteProduct>>();
      const favouriteProduct = { id: 123 };
      jest.spyOn(favouriteProductService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ favouriteProduct });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: favouriteProduct }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(favouriteProductService.update).toHaveBeenCalledWith(favouriteProduct);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FavouriteProduct>>();
      const favouriteProduct = new FavouriteProduct();
      jest.spyOn(favouriteProductService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ favouriteProduct });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: favouriteProduct }));
      saveSubject.complete();

      // THEN
      expect(favouriteProductService.create).toHaveBeenCalledWith(favouriteProduct);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FavouriteProduct>>();
      const favouriteProduct = { id: 123 };
      jest.spyOn(favouriteProductService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ favouriteProduct });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(favouriteProductService.update).toHaveBeenCalledWith(favouriteProduct);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackProductById', () => {
      it('Should return tracked Product primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackProductById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
