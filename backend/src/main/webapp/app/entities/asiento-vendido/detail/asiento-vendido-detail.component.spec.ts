import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AsientoVendidoDetailComponent } from './asiento-vendido-detail.component';

describe('AsientoVendido Management Detail Component', () => {
  let comp: AsientoVendidoDetailComponent;
  let fixture: ComponentFixture<AsientoVendidoDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AsientoVendidoDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./asiento-vendido-detail.component').then(m => m.AsientoVendidoDetailComponent),
              resolve: { asientoVendido: () => of({ id: 31999 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AsientoVendidoDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AsientoVendidoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load asientoVendido on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AsientoVendidoDetailComponent);

      // THEN
      expect(instance.asientoVendido()).toEqual(expect.objectContaining({ id: 31999 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
