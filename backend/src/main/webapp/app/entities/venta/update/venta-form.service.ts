import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IVenta, NewVenta } from '../venta.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVenta for edit and NewVentaFormGroupInput for create.
 */
type VentaFormGroupInput = IVenta | PartialWithRequiredKeyOf<NewVenta>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IVenta | NewVenta> = Omit<T, 'fechaVenta'> & {
  fechaVenta?: string | null;
};

type VentaFormRawValue = FormValueOf<IVenta>;

type NewVentaFormRawValue = FormValueOf<NewVenta>;

type VentaFormDefaults = Pick<NewVenta, 'id' | 'fechaVenta' | 'resultado'>;

type VentaFormGroupContent = {
  id: FormControl<VentaFormRawValue['id'] | NewVenta['id']>;
  ventaIdCatedra: FormControl<VentaFormRawValue['ventaIdCatedra']>;
  fechaVenta: FormControl<VentaFormRawValue['fechaVenta']>;
  precioVenta: FormControl<VentaFormRawValue['precioVenta']>;
  resultado: FormControl<VentaFormRawValue['resultado']>;
  descripcion: FormControl<VentaFormRawValue['descripcion']>;
  estadoVenta: FormControl<VentaFormRawValue['estadoVenta']>;
  evento: FormControl<VentaFormRawValue['evento']>;
  user: FormControl<VentaFormRawValue['user']>;
};

export type VentaFormGroup = FormGroup<VentaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VentaFormService {
  createVentaFormGroup(venta: VentaFormGroupInput = { id: null }): VentaFormGroup {
    const ventaRawValue = this.convertVentaToVentaRawValue({
      ...this.getFormDefaults(),
      ...venta,
    });
    return new FormGroup<VentaFormGroupContent>({
      id: new FormControl(
        { value: ventaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      ventaIdCatedra: new FormControl(ventaRawValue.ventaIdCatedra),
      fechaVenta: new FormControl(ventaRawValue.fechaVenta, {
        validators: [Validators.required],
      }),
      precioVenta: new FormControl(ventaRawValue.precioVenta, {
        validators: [Validators.required],
      }),
      resultado: new FormControl(ventaRawValue.resultado, {
        validators: [Validators.required],
      }),
      descripcion: new FormControl(ventaRawValue.descripcion),
      estadoVenta: new FormControl(ventaRawValue.estadoVenta),
      evento: new FormControl(ventaRawValue.evento, {
        validators: [Validators.required],
      }),
      user: new FormControl(ventaRawValue.user, {
        validators: [Validators.required],
      }),
    });
  }

  getVenta(form: VentaFormGroup): IVenta | NewVenta {
    return this.convertVentaRawValueToVenta(form.getRawValue() as VentaFormRawValue | NewVentaFormRawValue);
  }

  resetForm(form: VentaFormGroup, venta: VentaFormGroupInput): void {
    const ventaRawValue = this.convertVentaToVentaRawValue({ ...this.getFormDefaults(), ...venta });
    form.reset(
      {
        ...ventaRawValue,
        id: { value: ventaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): VentaFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fechaVenta: currentTime,
      resultado: false,
    };
  }

  private convertVentaRawValueToVenta(rawVenta: VentaFormRawValue | NewVentaFormRawValue): IVenta | NewVenta {
    return {
      ...rawVenta,
      fechaVenta: dayjs(rawVenta.fechaVenta, DATE_TIME_FORMAT),
    };
  }

  private convertVentaToVentaRawValue(
    venta: IVenta | (Partial<NewVenta> & VentaFormDefaults),
  ): VentaFormRawValue | PartialWithRequiredKeyOf<NewVentaFormRawValue> {
    return {
      ...venta,
      fechaVenta: venta.fechaVenta ? venta.fechaVenta.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
