import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { FavouriteProductService } from '../service/favourite-product.service';

import { FavouriteProductComponent } from './favourite-product.component';

describe('FavouriteProduct Management Component', () => {
  let comp: FavouriteProductComponent;
  let fixture: ComponentFixture<FavouriteProductComponent>;
  let service: FavouriteProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FavouriteProductComponent],
    })
      .overrideTemplate(FavouriteProductComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FavouriteProductComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(FavouriteProductService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.favouriteProducts?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
