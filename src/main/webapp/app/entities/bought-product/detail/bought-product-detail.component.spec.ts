import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BoughtProductDetailComponent } from './bought-product-detail.component';

describe('BoughtProduct Management Detail Component', () => {
  let comp: BoughtProductDetailComponent;
  let fixture: ComponentFixture<BoughtProductDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BoughtProductDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ boughtProduct: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BoughtProductDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BoughtProductDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load boughtProduct on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.boughtProduct).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
