import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { BoughtProductService } from '../service/bought-product.service';

import { BoughtProductComponent } from './bought-product.component';

describe('BoughtProduct Management Component', () => {
  let comp: BoughtProductComponent;
  let fixture: ComponentFixture<BoughtProductComponent>;
  let service: BoughtProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [BoughtProductComponent],
    })
      .overrideTemplate(BoughtProductComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BoughtProductComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(BoughtProductService);

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
    expect(comp.boughtProducts?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
