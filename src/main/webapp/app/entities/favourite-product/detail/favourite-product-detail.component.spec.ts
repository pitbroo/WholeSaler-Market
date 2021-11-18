import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FavouriteProductDetailComponent } from './favourite-product-detail.component';

describe('FavouriteProduct Management Detail Component', () => {
  let comp: FavouriteProductDetailComponent;
  let fixture: ComponentFixture<FavouriteProductDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FavouriteProductDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ favouriteProduct: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FavouriteProductDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FavouriteProductDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load favouriteProduct on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.favouriteProduct).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
