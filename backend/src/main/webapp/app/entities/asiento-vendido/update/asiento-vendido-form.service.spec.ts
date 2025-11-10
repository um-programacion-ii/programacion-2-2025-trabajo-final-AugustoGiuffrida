import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../asiento-vendido.test-samples';

import { AsientoVendidoFormService } from './asiento-vendido-form.service';

describe('AsientoVendido Form Service', () => {
  let service: AsientoVendidoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AsientoVendidoFormService);
  });

  describe('Service methods', () => {
    describe('createAsientoVendidoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAsientoVendidoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fila: expect.any(Object),
            columna: expect.any(Object),
            persona: expect.any(Object),
            venta: expect.any(Object),
          }),
        );
      });

      it('passing IAsientoVendido should create a new form with FormGroup', () => {
        const formGroup = service.createAsientoVendidoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fila: expect.any(Object),
            columna: expect.any(Object),
            persona: expect.any(Object),
            venta: expect.any(Object),
          }),
        );
      });
    });

    describe('getAsientoVendido', () => {
      it('should return NewAsientoVendido for default AsientoVendido initial value', () => {
        const formGroup = service.createAsientoVendidoFormGroup(sampleWithNewData);

        const asientoVendido = service.getAsientoVendido(formGroup) as any;

        expect(asientoVendido).toMatchObject(sampleWithNewData);
      });

      it('should return NewAsientoVendido for empty AsientoVendido initial value', () => {
        const formGroup = service.createAsientoVendidoFormGroup();

        const asientoVendido = service.getAsientoVendido(formGroup) as any;

        expect(asientoVendido).toMatchObject({});
      });

      it('should return IAsientoVendido', () => {
        const formGroup = service.createAsientoVendidoFormGroup(sampleWithRequiredData);

        const asientoVendido = service.getAsientoVendido(formGroup) as any;

        expect(asientoVendido).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAsientoVendido should not enable id FormControl', () => {
        const formGroup = service.createAsientoVendidoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAsientoVendido should disable id FormControl', () => {
        const formGroup = service.createAsientoVendidoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
