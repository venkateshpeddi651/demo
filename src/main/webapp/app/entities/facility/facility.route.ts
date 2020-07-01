import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IFacility, Facility } from 'app/shared/model/facility.model';
import { FacilityService } from './facility.service';
import { FacilityComponent } from './facility.component';
import { FacilityDetailComponent } from './facility-detail.component';
import { FacilityUpdateComponent } from './facility-update.component';

@Injectable({ providedIn: 'root' })
export class FacilityResolve implements Resolve<IFacility> {
  constructor(private service: FacilityService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFacility> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((facility: HttpResponse<Facility>) => {
          if (facility.body) {
            return of(facility.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Facility());
  }
}

export const facilityRoute: Routes = [
  {
    path: '',
    component: FacilityComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams,
    },
    data: {
      authorities: [Authority.FACILITY_ADMIN],
      defaultSort: 'id,asc',
      pageTitle: 'Facilities',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FacilityDetailComponent,
    resolve: {
      facility: FacilityResolve,
    },
    data: {
      authorities: [Authority.FACILITY_ADMIN],
      pageTitle: 'Facilities',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FacilityUpdateComponent,
    resolve: {
      facility: FacilityResolve,
    },
    data: {
      authorities: [Authority.FACILITY_ADMIN],
      pageTitle: 'Facilities',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FacilityUpdateComponent,
    resolve: {
      facility: FacilityResolve,
    },
    data: {
      authorities: [Authority.FACILITY_ADMIN],
      pageTitle: 'Facilities',
    },
    canActivate: [UserRouteAccessService],
  },
];
