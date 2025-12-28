import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AsientoVendidoResolve from './route/asiento-vendido-routing-resolve.service';

const asientoVendidoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/asiento-vendido.component').then(m => m.AsientoVendidoComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/asiento-vendido-detail.component').then(m => m.AsientoVendidoDetailComponent),
    resolve: {
      asientoVendido: AsientoVendidoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/asiento-vendido-update.component').then(m => m.AsientoVendidoUpdateComponent),
    resolve: {
      asientoVendido: AsientoVendidoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/asiento-vendido-update.component').then(m => m.AsientoVendidoUpdateComponent),
    resolve: {
      asientoVendido: AsientoVendidoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default asientoVendidoRoute;
