jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFavouriteProduct, FavouriteProduct } from '../favourite-product.model';
import { FavouriteProductService } from '../service/favourite-product.service';

import { FavouriteProductRoutingResolveService } from './favourite-product-routing-resolve.service';

describe('FavouriteProduct routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: FavouriteProductRoutingResolveService;
  let service: FavouriteProductService;
  let resultFavouriteProduct: IFavouriteProduct | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(FavouriteProductRoutingResolveService);
    service = TestBed.inject(FavouriteProductService);
    resultFavouriteProduct = undefined;
  });

  describe('resolve', () => {
    it('should return IFavouriteProduct returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFavouriteProduct = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFavouriteProduct).toEqual({ id: 123 });
    });

    it('should return new IFavouriteProduct if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFavouriteProduct = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultFavouriteProduct).toEqual(new FavouriteProduct());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as FavouriteProduct })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFavouriteProduct = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFavouriteProduct).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
