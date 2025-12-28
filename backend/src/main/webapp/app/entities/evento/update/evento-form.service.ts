import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEvento, NewEvento } from '../evento.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEvento for edit and NewEventoFormGroupInput for create.
 */
type EventoFormGroupInput = IEvento | PartialWithRequiredKeyOf<NewEvento>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEvento | NewEvento> = Omit<T, 'fecha'> & {
  fecha?: string | null;
};

type EventoFormRawValue = FormValueOf<IEvento>;

type NewEventoFormRawValue = FormValueOf<NewEvento>;

type EventoFormDefaults = Pick<NewEvento, 'id' | 'fecha'>;

type EventoFormGroupContent = {
  id: FormControl<EventoFormRawValue['id'] | NewEvento['id']>;
  eventoIdCatedra: FormControl<EventoFormRawValue['eventoIdCatedra']>;
  titulo: FormControl<EventoFormRawValue['titulo']>;
  resumen: FormControl<EventoFormRawValue['resumen']>;
  descripcion: FormControl<EventoFormRawValue['descripcion']>;
  fecha: FormControl<EventoFormRawValue['fecha']>;
  direccion: FormControl<EventoFormRawValue['direccion']>;
  imagen: FormControl<EventoFormRawValue['imagen']>;
  filaAsientos: FormControl<EventoFormRawValue['filaAsientos']>;
  columnAsientos: FormControl<EventoFormRawValue['columnAsientos']>;
  precioEntrada: FormControl<EventoFormRawValue['precioEntrada']>;
};

export type EventoFormGroup = FormGroup<EventoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EventoFormService {
  createEventoFormGroup(evento: EventoFormGroupInput = { id: null }): EventoFormGroup {
    const eventoRawValue = this.convertEventoToEventoRawValue({
      ...this.getFormDefaults(),
      ...evento,
    });
    return new FormGroup<EventoFormGroupContent>({
      id: new FormControl(
        { value: eventoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      eventoIdCatedra: new FormControl(eventoRawValue.eventoIdCatedra, {
        validators: [Validators.required],
      }),
      titulo: new FormControl(eventoRawValue.titulo, {
        validators: [Validators.required],
      }),
      resumen: new FormControl(eventoRawValue.resumen),
      descripcion: new FormControl(eventoRawValue.descripcion),
      fecha: new FormControl(eventoRawValue.fecha, {
        validators: [Validators.required],
      }),
      direccion: new FormControl(eventoRawValue.direccion),
      imagen: new FormControl(eventoRawValue.imagen),
      filaAsientos: new FormControl(eventoRawValue.filaAsientos, {
        validators: [Validators.required],
      }),
      columnAsientos: new FormControl(eventoRawValue.columnAsientos, {
        validators: [Validators.required],
      }),
      precioEntrada: new FormControl(eventoRawValue.precioEntrada, {
        validators: [Validators.required],
      }),
    });
  }

  getEvento(form: EventoFormGroup): IEvento | NewEvento {
    return this.convertEventoRawValueToEvento(form.getRawValue() as EventoFormRawValue | NewEventoFormRawValue);
  }

  resetForm(form: EventoFormGroup, evento: EventoFormGroupInput): void {
    const eventoRawValue = this.convertEventoToEventoRawValue({ ...this.getFormDefaults(), ...evento });
    form.reset(
      {
        ...eventoRawValue,
        id: { value: eventoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EventoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fecha: currentTime,
    };
  }

  private convertEventoRawValueToEvento(rawEvento: EventoFormRawValue | NewEventoFormRawValue): IEvento | NewEvento {
    return {
      ...rawEvento,
      fecha: dayjs(rawEvento.fecha, DATE_TIME_FORMAT),
    };
  }

  private convertEventoToEventoRawValue(
    evento: IEvento | (Partial<NewEvento> & EventoFormDefaults),
  ): EventoFormRawValue | PartialWithRequiredKeyOf<NewEventoFormRawValue> {
    return {
      ...evento,
      fecha: evento.fecha ? evento.fecha.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
