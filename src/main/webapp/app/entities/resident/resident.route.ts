import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IResident, Resident } from 'app/shared/model/resident.model';
import { ResidentService } from './resident.service';
import { ResidentComponent } from './resident.component';
import { ResidentDetailComponent } from './resident-detail.component';
import { ResidentUpdateComponent } from './resident-update.component';

@Injectable({ providedIn: 'root' })
export class ResidentResolve implements Resolve<IResident> {
  constructor(private service: ResidentService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IResident> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((resident: HttpResponse<Resident>) => {
          if (resident.body) {
            return of(resident.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Resident());
  }
}

export const residentRoute: Routes = [
  {
    path: '',
    component: ResidentComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams,
    },
    data: {
      authorities: [Authority.FACILITY_ADMIN],
      defaultSort: 'id,asc',
      pageTitle: 'Residents',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ResidentDetailComponent,
    resolve: {
      resident: ResidentResolve,
    },
    data: {
      authorities: [Authority.FACILITY_ADMIN],
      pageTitle: 'Residents',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ResidentUpdateComponent,
    resolve: {
      resident: ResidentResolve,
    },
    data: {
      authorities: [Authority.FACILITY_ADMIN],
      pageTitle: 'Residents',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ResidentUpdateComponent,
    resolve: {
      resident: ResidentResolve,
    },
    data: {
      authorities: [Authority.FACILITY_ADMIN],
      pageTitle: 'Residents',
    },
    canActivate: [UserRouteAccessService],
  },
];
