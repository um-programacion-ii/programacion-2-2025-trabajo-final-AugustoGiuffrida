import { IVenta } from 'app/entities/venta/venta.model';

export interface IAsientoVendido {
  id: number;
  fila?: number | null;
  columna?: number | null;
  persona?: string | null;
  venta?: Pick<IVenta, 'id'> | null;
}

export type NewAsientoVendido = Omit<IAsientoVendido, 'id'> & { id: null };
