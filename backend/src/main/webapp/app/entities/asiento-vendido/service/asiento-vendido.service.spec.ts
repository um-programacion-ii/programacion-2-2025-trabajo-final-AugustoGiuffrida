import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAsientoVendido } from '../asiento-vendido.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../asiento-vendido.test-samples';

import { AsientoVendidoService } from './asiento-vendido.service';

const requireRestSample: IAsientoVendido = {
  ...sampleWithRequiredData,
};

describe('AsientoVendido Service', () => {
  let service: AsientoVendidoService;
  let httpMock: HttpTestingController;
  let expectedResult: IAsientoVendido | IAsientoVendido[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AsientoVendidoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a AsientoVendido', () => {
      const asientoVendido = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(asientoVendido).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AsientoVendido', () => {
      const asientoVendido = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(asientoVendido).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AsientoVendido', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AsientoVendido', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AsientoVendido', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAsientoVendidoToCollectionIfMissing', () => {
      it('should add a AsientoVendido to an empty array', () => {
        const asientoVendido: IAsientoVendido = sampleWithRequiredData;
        expectedResult = service.addAsientoVendidoToCollectionIfMissing([], asientoVendido);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(asientoVendido);
      });

      it('should not add a AsientoVendido to an array that contains it', () => {
        const asientoVendido: IAsientoVendido = sampleWithRequiredData;
        const asientoVendidoCollection: IAsientoVendido[] = [
          {
            ...asientoVendido,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAsientoVendidoToCollectionIfMissing(asientoVendidoCollection, asientoVendido);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AsientoVendido to an array that doesn't contain it", () => {
        const asientoVendido: IAsientoVendido = sampleWithRequiredData;
        const asientoVendidoCollection: IAsientoVendido[] = [sampleWithPartialData];
        expectedResult = service.addAsientoVendidoToCollectionIfMissing(asientoVendidoCollection, asientoVendido);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(asientoVendido);
      });

      it('should add only unique AsientoVendido to an array', () => {
        const asientoVendidoArray: IAsientoVendido[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const asientoVendidoCollection: IAsientoVendido[] = [sampleWithRequiredData];
        expectedResult = service.addAsientoVendidoToCollectionIfMissing(asientoVendidoCollection, ...asientoVendidoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const asientoVendido: IAsientoVendido = sampleWithRequiredData;
        const asientoVendido2: IAsientoVendido = sampleWithPartialData;
        expectedResult = service.addAsientoVendidoToCollectionIfMissing([], asientoVendido, asientoVendido2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(asientoVendido);
        expect(expectedResult).toContain(asientoVendido2);
      });

      it('should accept null and undefined values', () => {
        const asientoVendido: IAsientoVendido = sampleWithRequiredData;
        expectedResult = service.addAsientoVendidoToCollectionIfMissing([], null, asientoVendido, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(asientoVendido);
      });

      it('should return initial array if no AsientoVendido is added', () => {
        const asientoVendidoCollection: IAsientoVendido[] = [sampleWithRequiredData];
        expectedResult = service.addAsientoVendidoToCollectionIfMissing(asientoVendidoCollection, undefined, null);
        expect(expectedResult).toEqual(asientoVendidoCollection);
      });
    });

    describe('compareAsientoVendido', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAsientoVendido(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 31999 };
        const entity2 = null;

        const compareResult1 = service.compareAsientoVendido(entity1, entity2);
        const compareResult2 = service.compareAsientoVendido(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 31999 };
        const entity2 = { id: 25261 };

        const compareResult1 = service.compareAsientoVendido(entity1, entity2);
        const compareResult2 = service.compareAsientoVendido(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 31999 };
        const entity2 = { id: 31999 };

        const compareResult1 = service.compareAsientoVendido(entity1, entity2);
        const compareResult2 = service.compareAsientoVendido(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
