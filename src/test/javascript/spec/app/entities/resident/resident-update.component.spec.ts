import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { DemoTestModule } from '../../../test.module';
import { ResidentUpdateComponent } from 'app/entities/resident/resident-update.component';
import { ResidentService } from 'app/entities/resident/resident.service';
import { Resident } from 'app/shared/model/resident.model';

describe('Component Tests', () => {
  describe('Resident Management Update Component', () => {
    let comp: ResidentUpdateComponent;
    let fixture: ComponentFixture<ResidentUpdateComponent>;
    let service: ResidentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DemoTestModule],
        declarations: [ResidentUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ResidentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ResidentUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ResidentService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Resident(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Resident();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
