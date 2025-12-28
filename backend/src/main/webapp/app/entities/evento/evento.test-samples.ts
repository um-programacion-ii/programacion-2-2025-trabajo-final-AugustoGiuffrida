import dayjs from 'dayjs/esm';

import { IEvento, NewEvento } from './evento.model';

export const sampleWithRequiredData: IEvento = {
  id: 21654,
  eventoIdCatedra: 11605,
  titulo: 'politely',
  fecha: dayjs('2025-11-09T12:17'),
  filaAsientos: 9098,
  columnAsientos: 8055,
  precioEntrada: 5714.67,
};

export const sampleWithPartialData: IEvento = {
  id: 2250,
  eventoIdCatedra: 15996,
  titulo: 'retrospectivity',
  resumen: 'masculinize cruelly',
  descripcion: 'cultivated who',
  fecha: dayjs('2025-11-09T04:16'),
  imagen: 'whirlwind',
  filaAsientos: 29540,
  columnAsientos: 20414,
  precioEntrada: 19947.77,
};

export const sampleWithFullData: IEvento = {
  id: 26545,
  eventoIdCatedra: 14949,
  titulo: 'airbrush',
  resumen: 'trick testing',
  descripcion: 'via',
  fecha: dayjs('2025-11-09T13:14'),
  direccion: 'orientate',
  imagen: 'goat likable leading',
  filaAsientos: 25471,
  columnAsientos: 23391,
  precioEntrada: 1808,
};

export const sampleWithNewData: NewEvento = {
  eventoIdCatedra: 19822,
  titulo: 'hmph gracefully stupendous',
  fecha: dayjs('2025-11-09T08:42'),
  filaAsientos: 19075,
  columnAsientos: 19404,
  precioEntrada: 14143.39,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
