import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFavouriteProduct, FavouriteProduct } from '../favourite-product.model';

import { FavouriteProductService } from './favourite-product.service';

describe('FavouriteProduct Service', () => {
  let service: FavouriteProductService;
  let httpMock: HttpTestingController;
  let elemDefault: IFavouriteProduct;
  let expectedResult: IFavouriteProduct | IFavouriteProduct[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FavouriteProductService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a FavouriteProduct', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new FavouriteProduct()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FavouriteProduct', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FavouriteProduct', () => {
      const patchObject = Object.assign({}, new FavouriteProduct());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FavouriteProduct', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a FavouriteProduct', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFavouriteProductToCollectionIfMissing', () => {
      it('should add a FavouriteProduct to an empty array', () => {
        const favouriteProduct: IFavouriteProduct = { id: 123 };
        expectedResult = service.addFavouriteProductToCollectionIfMissing([], favouriteProduct);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(favouriteProduct);
      });

      it('should not add a FavouriteProduct to an array that contains it', () => {
        const favouriteProduct: IFavouriteProduct = { id: 123 };
        const favouriteProductCollection: IFavouriteProduct[] = [
          {
            ...favouriteProduct,
          },
          { id: 456 },
        ];
        expectedResult = service.addFavouriteProductToCollectionIfMissing(favouriteProductCollection, favouriteProduct);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FavouriteProduct to an array that doesn't contain it", () => {
        const favouriteProduct: IFavouriteProduct = { id: 123 };
        const favouriteProductCollection: IFavouriteProduct[] = [{ id: 456 }];
        expectedResult = service.addFavouriteProductToCollectionIfMissing(favouriteProductCollection, favouriteProduct);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(favouriteProduct);
      });

      it('should add only unique FavouriteProduct to an array', () => {
        const favouriteProductArray: IFavouriteProduct[] = [{ id: 123 }, { id: 456 }, { id: 67052 }];
        const favouriteProductCollection: IFavouriteProduct[] = [{ id: 123 }];
        expectedResult = service.addFavouriteProductToCollectionIfMissing(favouriteProductCollection, ...favouriteProductArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const favouriteProduct: IFavouriteProduct = { id: 123 };
        const favouriteProduct2: IFavouriteProduct = { id: 456 };
        expectedResult = service.addFavouriteProductToCollectionIfMissing([], favouriteProduct, favouriteProduct2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(favouriteProduct);
        expect(expectedResult).toContain(favouriteProduct2);
      });

      it('should accept null and undefined values', () => {
        const favouriteProduct: IFavouriteProduct = { id: 123 };
        expectedResult = service.addFavouriteProductToCollectionIfMissing([], null, favouriteProduct, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(favouriteProduct);
      });

      it('should return initial array if no FavouriteProduct is added', () => {
        const favouriteProductCollection: IFavouriteProduct[] = [{ id: 123 }];
        expectedResult = service.addFavouriteProductToCollectionIfMissing(favouriteProductCollection, undefined, null);
        expect(expectedResult).toEqual(favouriteProductCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
