import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IVenta } from 'app/entities/venta/venta.model';
import { VentaService } from 'app/entities/venta/service/venta.service';
import { AsientoVendidoService } from '../service/asiento-vendido.service';
import { IAsientoVendido } from '../asiento-vendido.model';
import { AsientoVendidoFormService } from './asiento-vendido-form.service';

import { AsientoVendidoUpdateComponent } from './asiento-vendido-update.component';

describe('AsientoVendido Management Update Component', () => {
  let comp: AsientoVendidoUpdateComponent;
  let fixture: ComponentFixture<AsientoVendidoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let asientoVendidoFormService: AsientoVendidoFormService;
  let asientoVendidoService: AsientoVendidoService;
  let ventaService: VentaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AsientoVendidoUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AsientoVendidoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AsientoVendidoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    asientoVendidoFormService = TestBed.inject(AsientoVendidoFormService);
    asientoVendidoService = TestBed.inject(AsientoVendidoService);
    ventaService = TestBed.inject(VentaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Venta query and add missing value', () => {
      const asientoVendido: IAsientoVendido = { id: 25261 };
      const venta: IVenta = { id: 10395 };
      asientoVendido.venta = venta;

      const ventaCollection: IVenta[] = [{ id: 10395 }];
      jest.spyOn(ventaService, 'query').mockReturnValue(of(new HttpResponse({ body: ventaCollection })));
      const additionalVentas = [venta];
      const expectedCollection: IVenta[] = [...additionalVentas, ...ventaCollection];
      jest.spyOn(ventaService, 'addVentaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ asientoVendido });
      comp.ngOnInit();

      expect(ventaService.query).toHaveBeenCalled();
      expect(ventaService.addVentaToCollectionIfMissing).toHaveBeenCalledWith(
        ventaCollection,
        ...additionalVentas.map(expect.objectContaining),
      );
      expect(comp.ventasSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const asientoVendido: IAsientoVendido = { id: 25261 };
      const venta: IVenta = { id: 10395 };
      asientoVendido.venta = venta;

      activatedRoute.data = of({ asientoVendido });
      comp.ngOnInit();

      expect(comp.ventasSharedCollection).toContainEqual(venta);
      expect(comp.asientoVendido).toEqual(asientoVendido);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAsientoVendido>>();
      const asientoVendido = { id: 31999 };
      jest.spyOn(asientoVendidoFormService, 'getAsientoVendido').mockReturnValue(asientoVendido);
      jest.spyOn(asientoVendidoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ asientoVendido });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: asientoVendido }));
      saveSubject.complete();

      // THEN
      expect(asientoVendidoFormService.getAsientoVendido).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(asientoVendidoService.update).toHaveBeenCalledWith(expect.objectContaining(asientoVendido));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAsientoVendido>>();
      const asientoVendido = { id: 31999 };
      jest.spyOn(asientoVendidoFormService, 'getAsientoVendido').mockReturnValue({ id: null });
      jest.spyOn(asientoVendidoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ asientoVendido: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: asientoVendido }));
      saveSubject.complete();

      // THEN
      expect(asientoVendidoFormService.getAsientoVendido).toHaveBeenCalled();
      expect(asientoVendidoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAsientoVendido>>();
      const asientoVendido = { id: 31999 };
      jest.spyOn(asientoVendidoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ asientoVendido });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(asientoVendidoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareVenta', () => {
      it('should forward to ventaService', () => {
        const entity = { id: 10395 };
        const entity2 = { id: 27589 };
        jest.spyOn(ventaService, 'compareVenta');
        comp.compareVenta(entity, entity2);
        expect(ventaService.compareVenta).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
