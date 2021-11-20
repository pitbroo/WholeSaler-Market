import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SoldProductService } from '../service/sold-product.service';

import { SoldProductComponent } from './sold-product.component';

describe('SoldProduct Management Component', () => {
  let comp: SoldProductComponent;
  let fixture: ComponentFixture<SoldProductComponent>;
  let service: SoldProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SoldProductComponent],
    })
      .overrideTemplate(SoldProductComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SoldProductComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SoldProductService);

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
    expect(comp.soldProducts?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
