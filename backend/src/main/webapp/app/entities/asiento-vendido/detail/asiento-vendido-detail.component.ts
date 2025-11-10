import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IAsientoVendido } from '../asiento-vendido.model';

@Component({
  selector: 'jhi-asiento-vendido-detail',
  templateUrl: './asiento-vendido-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class AsientoVendidoDetailComponent {
  asientoVendido = input<IAsientoVendido | null>(null);

  previousState(): void {
    window.history.back();
  }
}
