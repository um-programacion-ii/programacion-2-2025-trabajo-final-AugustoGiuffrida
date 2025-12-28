import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IAsientoVendido, NewAsientoVendido } from '../asiento-vendido.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAsientoVendido for edit and NewAsientoVendidoFormGroupInput for create.
 */
type AsientoVendidoFormGroupInput = IAsientoVendido | PartialWithRequiredKeyOf<NewAsientoVendido>;

type AsientoVendidoFormDefaults = Pick<NewAsientoVendido, 'id'>;

type AsientoVendidoFormGroupContent = {
  id: FormControl<IAsientoVendido['id'] | NewAsientoVendido['id']>;
  fila: FormControl<IAsientoVendido['fila']>;
  columna: FormControl<IAsientoVendido['columna']>;
  persona: FormControl<IAsientoVendido['persona']>;
  venta: FormControl<IAsientoVendido['venta']>;
};

export type AsientoVendidoFormGroup = FormGroup<AsientoVendidoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AsientoVendidoFormService {
  createAsientoVendidoFormGroup(asientoVendido: AsientoVendidoFormGroupInput = { id: null }): AsientoVendidoFormGroup {
    const asientoVendidoRawValue = {
      ...this.getFormDefaults(),
      ...asientoVendido,
    };
    return new FormGroup<AsientoVendidoFormGroupContent>({
      id: new FormControl(
        { value: asientoVendidoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fila: new FormControl(asientoVendidoRawValue.fila, {
        validators: [Validators.required],
      }),
      columna: new FormControl(asientoVendidoRawValue.columna, {
        validators: [Validators.required],
      }),
      persona: new FormControl(asientoVendidoRawValue.persona, {
        validators: [Validators.required],
      }),
      venta: new FormControl(asientoVendidoRawValue.venta),
    });
  }

  getAsientoVendido(form: AsientoVendidoFormGroup): IAsientoVendido | NewAsientoVendido {
    return form.getRawValue() as IAsientoVendido | NewAsientoVendido;
  }

  resetForm(form: AsientoVendidoFormGroup, asientoVendido: AsientoVendidoFormGroupInput): void {
    const asientoVendidoRawValue = { ...this.getFormDefaults(), ...asientoVendido };
    form.reset(
      {
        ...asientoVendidoRawValue,
        id: { value: asientoVendidoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AsientoVendidoFormDefaults {
    return {
      id: null,
    };
  }
}
