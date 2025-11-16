import dayjs from 'dayjs/esm';
import { IEvento } from 'app/entities/evento/evento.model';
import { IUser } from 'app/entities/user/user.model';
import { EstadoVenta } from 'app/entities/enumerations/estado-venta.model';

export interface IVenta {
  id: number;
  ventaIdCatedra?: number | null;
  fechaVenta?: dayjs.Dayjs | null;
  precioVenta?: number | null;
  resultado?: boolean | null;
  descripcion?: string | null;
  estadoVenta?: keyof typeof EstadoVenta | null;
  evento?: Pick<IEvento, 'id' | 'titulo'> | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewVenta = Omit<IVenta, 'id'> & { id: null };
