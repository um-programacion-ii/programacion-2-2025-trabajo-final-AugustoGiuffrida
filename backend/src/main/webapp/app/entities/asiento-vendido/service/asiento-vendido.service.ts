import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAsientoVendido, NewAsientoVendido } from '../asiento-vendido.model';

export type PartialUpdateAsientoVendido = Partial<IAsientoVendido> & Pick<IAsientoVendido, 'id'>;

export type EntityResponseType = HttpResponse<IAsientoVendido>;
export type EntityArrayResponseType = HttpResponse<IAsientoVendido[]>;

@Injectable({ providedIn: 'root' })
export class AsientoVendidoService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/asiento-vendidos');

  create(asientoVendido: NewAsientoVendido): Observable<EntityResponseType> {
    return this.http.post<IAsientoVendido>(this.resourceUrl, asientoVendido, { observe: 'response' });
  }

  update(asientoVendido: IAsientoVendido): Observable<EntityResponseType> {
    return this.http.put<IAsientoVendido>(`${this.resourceUrl}/${this.getAsientoVendidoIdentifier(asientoVendido)}`, asientoVendido, {
      observe: 'response',
    });
  }

  partialUpdate(asientoVendido: PartialUpdateAsientoVendido): Observable<EntityResponseType> {
    return this.http.patch<IAsientoVendido>(`${this.resourceUrl}/${this.getAsientoVendidoIdentifier(asientoVendido)}`, asientoVendido, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAsientoVendido>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAsientoVendido[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAsientoVendidoIdentifier(asientoVendido: Pick<IAsientoVendido, 'id'>): number {
    return asientoVendido.id;
  }

  compareAsientoVendido(o1: Pick<IAsientoVendido, 'id'> | null, o2: Pick<IAsientoVendido, 'id'> | null): boolean {
    return o1 && o2 ? this.getAsientoVendidoIdentifier(o1) === this.getAsientoVendidoIdentifier(o2) : o1 === o2;
  }

  addAsientoVendidoToCollectionIfMissing<Type extends Pick<IAsientoVendido, 'id'>>(
    asientoVendidoCollection: Type[],
    ...asientoVendidosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const asientoVendidos: Type[] = asientoVendidosToCheck.filter(isPresent);
    if (asientoVendidos.length > 0) {
      const asientoVendidoCollectionIdentifiers = asientoVendidoCollection.map(asientoVendidoItem =>
        this.getAsientoVendidoIdentifier(asientoVendidoItem),
      );
      const asientoVendidosToAdd = asientoVendidos.filter(asientoVendidoItem => {
        const asientoVendidoIdentifier = this.getAsientoVendidoIdentifier(asientoVendidoItem);
        if (asientoVendidoCollectionIdentifiers.includes(asientoVendidoIdentifier)) {
          return false;
        }
        asientoVendidoCollectionIdentifiers.push(asientoVendidoIdentifier);
        return true;
      });
      return [...asientoVendidosToAdd, ...asientoVendidoCollection];
    }
    return asientoVendidoCollection;
  }
}
