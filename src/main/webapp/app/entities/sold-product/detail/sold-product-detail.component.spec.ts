import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SoldProductDetailComponent } from './sold-product-detail.component';

describe('SoldProduct Management Detail Component', () => {
  let comp: SoldProductDetailComponent;
  let fixture: ComponentFixture<SoldProductDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SoldProductDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ soldProduct: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SoldProductDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SoldProductDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load soldProduct on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.soldProduct).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
