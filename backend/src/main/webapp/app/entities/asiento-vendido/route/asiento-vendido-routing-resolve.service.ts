import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAsientoVendido } from '../asiento-vendido.model';
import { AsientoVendidoService } from '../service/asiento-vendido.service';

const asientoVendidoResolve = (route: ActivatedRouteSnapshot): Observable<null | IAsientoVendido> => {
  const id = route.params.id;
  if (id) {
    return inject(AsientoVendidoService)
      .find(id)
      .pipe(
        mergeMap((asientoVendido: HttpResponse<IAsientoVendido>) => {
          if (asientoVendido.body) {
            return of(asientoVendido.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default asientoVendidoResolve;
