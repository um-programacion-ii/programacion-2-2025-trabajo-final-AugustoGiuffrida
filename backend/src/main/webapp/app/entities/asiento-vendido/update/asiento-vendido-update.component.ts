import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IVenta } from 'app/entities/venta/venta.model';
import { VentaService } from 'app/entities/venta/service/venta.service';
import { IAsientoVendido } from '../asiento-vendido.model';
import { AsientoVendidoService } from '../service/asiento-vendido.service';
import { AsientoVendidoFormGroup, AsientoVendidoFormService } from './asiento-vendido-form.service';

@Component({
  selector: 'jhi-asiento-vendido-update',
  templateUrl: './asiento-vendido-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AsientoVendidoUpdateComponent implements OnInit {
  isSaving = false;
  asientoVendido: IAsientoVendido | null = null;

  ventasSharedCollection: IVenta[] = [];

  protected asientoVendidoService = inject(AsientoVendidoService);
  protected asientoVendidoFormService = inject(AsientoVendidoFormService);
  protected ventaService = inject(VentaService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AsientoVendidoFormGroup = this.asientoVendidoFormService.createAsientoVendidoFormGroup();

  compareVenta = (o1: IVenta | null, o2: IVenta | null): boolean => this.ventaService.compareVenta(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ asientoVendido }) => {
      this.asientoVendido = asientoVendido;
      if (asientoVendido) {
        this.updateForm(asientoVendido);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const asientoVendido = this.asientoVendidoFormService.getAsientoVendido(this.editForm);
    if (asientoVendido.id !== null) {
      this.subscribeToSaveResponse(this.asientoVendidoService.update(asientoVendido));
    } else {
      this.subscribeToSaveResponse(this.asientoVendidoService.create(asientoVendido));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAsientoVendido>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(asientoVendido: IAsientoVendido): void {
    this.asientoVendido = asientoVendido;
    this.asientoVendidoFormService.resetForm(this.editForm, asientoVendido);

    this.ventasSharedCollection = this.ventaService.addVentaToCollectionIfMissing<IVenta>(
      this.ventasSharedCollection,
      asientoVendido.venta,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.ventaService
      .query()
      .pipe(map((res: HttpResponse<IVenta[]>) => res.body ?? []))
      .pipe(map((ventas: IVenta[]) => this.ventaService.addVentaToCollectionIfMissing<IVenta>(ventas, this.asientoVendido?.venta)))
      .subscribe((ventas: IVenta[]) => (this.ventasSharedCollection = ventas));
  }
}
