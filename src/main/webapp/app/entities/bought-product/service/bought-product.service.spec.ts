import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IBoughtProduct, BoughtProduct } from '../bought-product.model';

import { BoughtProductService } from './bought-product.service';

describe('BoughtProduct Service', () => {
  let service: BoughtProductService;
  let httpMock: HttpTestingController;
  let elemDefault: IBoughtProduct;
  let expectedResult: IBoughtProduct | IBoughtProduct[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BoughtProductService);
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

    it('should create a BoughtProduct', () => {
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

      service.create(new BoughtProduct()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BoughtProduct', () => {
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

    it('should partial update a BoughtProduct', () => {
      const patchObject = Object.assign(
        {
          price: currentDate.format(DATE_FORMAT),
        },
        new BoughtProduct()
      );

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

    it('should return a list of BoughtProduct', () => {
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

    it('should delete a BoughtProduct', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBoughtProductToCollectionIfMissing', () => {
      it('should add a BoughtProduct to an empty array', () => {
        const boughtProduct: IBoughtProduct = { id: 123 };
        expectedResult = service.addBoughtProductToCollectionIfMissing([], boughtProduct);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(boughtProduct);
      });

      it('should not add a BoughtProduct to an array that contains it', () => {
        const boughtProduct: IBoughtProduct = { id: 123 };
        const boughtProductCollection: IBoughtProduct[] = [
          {
            ...boughtProduct,
          },
          { id: 456 },
        ];
        expectedResult = service.addBoughtProductToCollectionIfMissing(boughtProductCollection, boughtProduct);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BoughtProduct to an array that doesn't contain it", () => {
        const boughtProduct: IBoughtProduct = { id: 123 };
        const boughtProductCollection: IBoughtProduct[] = [{ id: 456 }];
        expectedResult = service.addBoughtProductToCollectionIfMissing(boughtProductCollection, boughtProduct);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(boughtProduct);
      });

      it('should add only unique BoughtProduct to an array', () => {
        const boughtProductArray: IBoughtProduct[] = [{ id: 123 }, { id: 456 }, { id: 26368 }];
        const boughtProductCollection: IBoughtProduct[] = [{ id: 123 }];
        expectedResult = service.addBoughtProductToCollectionIfMissing(boughtProductCollection, ...boughtProductArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const boughtProduct: IBoughtProduct = { id: 123 };
        const boughtProduct2: IBoughtProduct = { id: 456 };
        expectedResult = service.addBoughtProductToCollectionIfMissing([], boughtProduct, boughtProduct2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(boughtProduct);
        expect(expectedResult).toContain(boughtProduct2);
      });

      it('should accept null and undefined values', () => {
        const boughtProduct: IBoughtProduct = { id: 123 };
        expectedResult = service.addBoughtProductToCollectionIfMissing([], null, boughtProduct, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(boughtProduct);
      });

      it('should return initial array if no BoughtProduct is added', () => {
        const boughtProductCollection: IBoughtProduct[] = [{ id: 123 }];
        expectedResult = service.addBoughtProductToCollectionIfMissing(boughtProductCollection, undefined, null);
        expect(expectedResult).toEqual(boughtProductCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
