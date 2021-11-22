import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISoldProduct, SoldProduct } from '../sold-product.model';

import { SoldProductService } from './sold-product.service';

describe('SoldProduct Service', () => {
  let service: SoldProductService;
  let httpMock: HttpTestingController;
  let elemDefault: ISoldProduct;
  let expectedResult: ISoldProduct | ISoldProduct[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SoldProductService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      date: currentDate,
      price: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          date: currentDate.format(DATE_FORMAT),
          price: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a SoldProduct', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          date: currentDate.format(DATE_FORMAT),
          price: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
          price: currentDate,
        },
        returnedFromService
      );

      service.create(new SoldProduct()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SoldProduct', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          date: currentDate.format(DATE_FORMAT),
          price: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
          price: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SoldProduct', () => {
      const patchObject = Object.assign({}, new SoldProduct());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          date: currentDate,
          price: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SoldProduct', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          date: currentDate.format(DATE_FORMAT),
          price: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
          price: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a SoldProduct', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSoldProductToCollectionIfMissing', () => {
      it('should add a SoldProduct to an empty array', () => {
        const soldProduct: ISoldProduct = { id: 123 };
        expectedResult = service.addSoldProductToCollectionIfMissing([], soldProduct);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(soldProduct);
      });

      it('should not add a SoldProduct to an array that contains it', () => {
        const soldProduct: ISoldProduct = { id: 123 };
        const soldProductCollection: ISoldProduct[] = [
          {
            ...soldProduct,
          },
          { id: 456 },
        ];
        expectedResult = service.addSoldProductToCollectionIfMissing(soldProductCollection, soldProduct);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SoldProduct to an array that doesn't contain it", () => {
        const soldProduct: ISoldProduct = { id: 123 };
        const soldProductCollection: ISoldProduct[] = [{ id: 456 }];
        expectedResult = service.addSoldProductToCollectionIfMissing(soldProductCollection, soldProduct);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(soldProduct);
      });

      it('should add only unique SoldProduct to an array', () => {
        const soldProductArray: ISoldProduct[] = [{ id: 123 }, { id: 456 }, { id: 10364 }];
        const soldProductCollection: ISoldProduct[] = [{ id: 123 }];
        expectedResult = service.addSoldProductToCollectionIfMissing(soldProductCollection, ...soldProductArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const soldProduct: ISoldProduct = { id: 123 };
        const soldProduct2: ISoldProduct = { id: 456 };
        expectedResult = service.addSoldProductToCollectionIfMissing([], soldProduct, soldProduct2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(soldProduct);
        expect(expectedResult).toContain(soldProduct2);
      });

      it('should accept null and undefined values', () => {
        const soldProduct: ISoldProduct = { id: 123 };
        expectedResult = service.addSoldProductToCollectionIfMissing([], null, soldProduct, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(soldProduct);
      });

      it('should return initial array if no SoldProduct is added', () => {
        const soldProductCollection: ISoldProduct[] = [{ id: 123 }];
        expectedResult = service.addSoldProductToCollectionIfMissing(soldProductCollection, undefined, null);
        expect(expectedResult).toEqual(soldProductCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
