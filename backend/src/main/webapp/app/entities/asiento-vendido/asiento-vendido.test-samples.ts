import { IAsientoVendido, NewAsientoVendido } from './asiento-vendido.model';

export const sampleWithRequiredData: IAsientoVendido = {
  id: 19834,
  fila: 11116,
  columna: 28370,
  persona: 'mmm',
};

export const sampleWithPartialData: IAsientoVendido = {
  id: 32490,
  fila: 28914,
  columna: 21935,
  persona: 'certainly lady second',
};

export const sampleWithFullData: IAsientoVendido = {
  id: 8646,
  fila: 18926,
  columna: 13188,
  persona: 'prestigious ownership sleepily',
};

export const sampleWithNewData: NewAsientoVendido = {
  fila: 6259,
  columna: 8492,
  persona: 'unbearably swear via',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
