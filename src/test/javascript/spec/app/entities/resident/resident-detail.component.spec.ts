import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DemoTestModule } from '../../../test.module';
import { ResidentDetailComponent } from 'app/entities/resident/resident-detail.component';
import { Resident } from 'app/shared/model/resident.model';

describe('Component Tests', () => {
  describe('Resident Management Detail Component', () => {
    let comp: ResidentDetailComponent;
    let fixture: ComponentFixture<ResidentDetailComponent>;
    const route = ({ data: of({ resident: new Resident(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DemoTestModule],
        declarations: [ResidentDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ResidentDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ResidentDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load resident on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.resident).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
