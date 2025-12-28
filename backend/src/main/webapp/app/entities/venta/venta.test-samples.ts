import dayjs from 'dayjs/esm';

import { IVenta, NewVenta } from './venta.model';

export const sampleWithRequiredData: IVenta = {
  id: 21245,
  fechaVenta: dayjs('2025-11-09T08:09'),
  precioVenta: 8279.35,
  resultado: false,
};

export const sampleWithPartialData: IVenta = {
  id: 9728,
  ventaIdCatedra: 5293,
  fechaVenta: dayjs('2025-11-09T06:14'),
  precioVenta: 493.81,
  resultado: false,
};

export const sampleWithFullData: IVenta = {
  id: 8723,
  ventaIdCatedra: 26179,
  fechaVenta: dayjs('2025-11-10T01:16'),
  precioVenta: 19619.07,
  resultado: true,
  descripcion: 'lest good-natured',
  estadoVenta: 'RECHAZADA',
};

export const sampleWithNewData: NewVenta = {
  fechaVenta: dayjs('2025-11-09T04:06'),
  precioVenta: 17775.18,
  resultado: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
