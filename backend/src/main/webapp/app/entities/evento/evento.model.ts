import dayjs from 'dayjs/esm';

export interface IEvento {
  id: number;
  eventoIdCatedra?: number | null;
  titulo?: string | null;
  resumen?: string | null;
  descripcion?: string | null;
  fecha?: dayjs.Dayjs | null;
  direccion?: string | null;
  imagen?: string | null;
  filaAsientos?: number | null;
  columnAsientos?: number | null;
  precioEntrada?: number | null;
}

export type NewEvento = Omit<IEvento, 'id'> & { id: null };
