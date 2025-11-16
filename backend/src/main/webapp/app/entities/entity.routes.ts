import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'backendApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'evento',
    data: { pageTitle: 'backendApp.evento.home.title' },
    loadChildren: () => import('./evento/evento.routes'),
  },
  {
    path: 'venta',
    data: { pageTitle: 'backendApp.venta.home.title' },
    loadChildren: () => import('./venta/venta.routes'),
  },
  {
    path: 'asiento-vendido',
    data: { pageTitle: 'backendApp.asientoVendido.home.title' },
    loadChildren: () => import('./asiento-vendido/asiento-vendido.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
